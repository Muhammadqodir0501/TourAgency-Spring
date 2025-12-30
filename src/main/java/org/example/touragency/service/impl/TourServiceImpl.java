package org.example.touragency.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.touragency.dto.request.TourAddDto;
import org.example.touragency.dto.response.TourResponseDto;
import org.example.touragency.dto.response.TourUpdateDto;
import org.example.touragency.model.Role;
import org.example.touragency.model.entity.Tour;
import org.example.touragency.model.entity.User;
import org.example.touragency.repository.FavTourRepository;
import org.example.touragency.repository.TourRepository;
import org.example.touragency.repository.UserRepository;
import org.example.touragency.service.abstractions.TourService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final UserRepository userRepository;
    private final FavTourRepository favTourRepository;


    @Override
    public Tour addNewTour(UUID agencyId, TourAddDto tourAddDto) {

        Optional<User> agency = userRepository.findById(agencyId);

        if (agency.isEmpty()) {
            throw new RuntimeException("Agency not found");
        }

        if (!agency.get().getRole().equals(Role.AGENCY)) {
            throw new RuntimeException("User is not an agency");
        }

        int nights = calculatingNights(tourAddDto.getStartDate(), tourAddDto.getReturnDate());

        User existAgency = userRepository.findById(agencyId).orElse(null);

        Tour newTour = Tour.builder()
                .title(tourAddDto.getTitle())
                .agency(existAgency)
                .city(tourAddDto.getCity())
                .hotel(tourAddDto.getHotel())
                .description(tourAddDto.getDescription())
                .startDate(tourAddDto.getStartDate())
                .returnDate(tourAddDto.getReturnDate())
                .nights(nights)
                .price(tourAddDto.getPrice())
                .seatsTotal(tourAddDto.getSeatsTotal())
                .seatsAvailable(tourAddDto.getSeatsTotal())
                .isAvailable(true)
                .views(0L)
                .build();

        return tourRepository.save(newTour);

    }


    @Override
    public void deleteTour(UUID agencyId, UUID tourId) {

        Optional<User> agency = userRepository.findById(agencyId);

        if(agency.get().getRole().equals(Role.AGENCY) && tourId != null) {
            favTourRepository.deleteAllIfTourDeleted(tourId);
            tourRepository.deleteById(tourId);
        }
    }

    @Override
    public Tour updateTour(UUID agencyId, UUID tourId, TourUpdateDto tourUpdateDto) {

        Optional<Tour> existingTour = tourRepository.findById(tourId);

        if(existingTour.isEmpty()) {
            throw new RuntimeException("Tour not found");
        }

        if(!existingTour.get().getAgency().getId().equals(agencyId)) {
            throw new RuntimeException("Agency not found");
        }
        Integer nights = calculatingNights(tourUpdateDto.getStartDate(), tourUpdateDto.getReturnDate());

        existingTour.get().setTitle(tourUpdateDto.getTitle());
        existingTour.get().setDescription(tourUpdateDto.getDescription());
        existingTour.get().setCity(tourUpdateDto.getCity());
        existingTour.get().setPrice(tourUpdateDto.getPrice());
        existingTour.get().setSeatsTotal(tourUpdateDto.getSeatsTotal());
        existingTour.get().setSeatsAvailable(tourUpdateDto.getSeatsTotal());
        existingTour.get().setStartDate(tourUpdateDto.getStartDate());
        existingTour.get().setReturnDate(tourUpdateDto.getReturnDate());
        existingTour.get().setNights(nights);
        existingTour.get().setHotel(tourUpdateDto.getHotel());
        return existingTour.orElse(null);
    }

    @Override
    public List<TourResponseDto> getAllTours() {
        return tourRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public TourResponseDto getTourById(UUID userId, UUID tourId) {
        Optional<Tour> tour = tourRepository.findById(tourId);
        if(tour.isEmpty()) {
            throw new RuntimeException("Tour not found");
        }

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        tour.get().setViews(tour.get().getViews() + 1L);
        return toResponseDto(tour.orElse(null));
    }

    @Override
    public List<TourResponseDto> getAllToursByAgencyId(UUID agencyId) {
        return tourRepository.findAll().stream()
                .filter(tour -> tour.getAgency().getId().equals(agencyId))
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public void tourIsBooked(Tour tour) {
        if(tour.isAvailable()) {
            tour.setSeatsAvailable(tour.getSeatsAvailable() - 1);
            if(tour.getSeatsAvailable() == 0){
                tour.setAvailable(false);
            }
        }
    }
    @Override
    public void tourBookingIsCanceled(UUID tourId) {
        Optional<Tour> tour = tourRepository.findById(tourId);
        tour.get().setSeatsAvailable(tour.get().getSeatsAvailable() + 1);
        if(tour.get().getSeatsAvailable() == 0){
            tour.get().setAvailable(true);
        }
    }

    @Override
    public TourResponseDto addDiscount(UUID agencyId, UUID tourId, Integer discountPercent) {
        Optional<User> admin =  userRepository.findById(agencyId);
        Optional<Tour> tour = tourRepository.findById(tourId);

        if(tour.isEmpty() || admin.isEmpty()) {
            throw new RuntimeException("Tour or User not found");
        }
        if(discountPercent < 0 || discountPercent > 100) {
            throw new RuntimeException("Invalid discount percent");
        }
        if(!tour.get().getAgency().getId().equals(agencyId)) {
            throw new RuntimeException("Agency not found");
        }

        tour.get().setDiscountPercent(discountPercent);
        tour.get().setPriceWithDiscount(
                tour.get().getPrice().multiply(
                        BigDecimal.valueOf(100 - discountPercent)
                ).divide(BigDecimal.valueOf(100))
        );
        return toResponseDto(tour.orElse(null));

    }

    private TourResponseDto toResponseDto(Tour tour) {
        Optional<User> agency = userRepository.findById(tour.getAgency().getId());
        return TourResponseDto.builder()
                .id(tour.getId())
                .agencyName(agency.get().getFullName())
                .agencyId(agency.get().getId())
                .title(tour.getTitle())
                .description(tour.getDescription())
                .nights(calculatingNights(tour.getStartDate(), tour.getReturnDate()))
                .startDate(tour.getStartDate())
                .returnDate(tour.getReturnDate())
                .price(tour.getPrice())
                .priceWithDiscount(tour.getPriceWithDiscount())
                .hotel(tour.getHotel())
                .city(tour.getCity())
                .seatsTotal(tour.getSeatsTotal())
                .seatsAvailable(tour.getSeatsAvailable())
                .views(tour.getViews())
                .rating(tour.getRating())
                .discountPercent(tour.getDiscountPercent())
                .build();
    }

    private Integer calculatingNights(LocalDate startDate, LocalDate returnDate) {
        if (returnDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Qaytish kuni ketish kunidan vohli bo'lmasligi kerak!");
        }

        int nights = (int) ChronoUnit.DAYS.between(startDate, returnDate);

        if (nights <= 0) {
            throw new IllegalArgumentException("Tur kamida 1 kun davom etishi kerak!");
        }
        return nights;
    }

}
