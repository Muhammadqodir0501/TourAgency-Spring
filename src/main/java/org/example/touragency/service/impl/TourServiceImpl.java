package org.example.touragency.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.touragency.dto.request.TourAddDto;
import org.example.touragency.dto.response.TourResponseDto;
import org.example.touragency.dto.response.TourUpdateDto;
import org.example.touragency.model.Role;
import org.example.touragency.model.enity.Tour;
import org.example.touragency.model.enity.User;
import org.example.touragency.repository.FavTourRepository;
import org.example.touragency.repository.TourRepository;
import org.example.touragency.repository.UserRepository;
import org.example.touragency.service.abstractions.TourService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final UserRepository userRepository;
    private final FavTourRepository favTourRepository;


    @Override
    public Tour addNewTour(UUID agencyId, TourAddDto tourAddDto) {

        User agency = userRepository.getUserById(agencyId);

        if (agency == null) {
            throw new RuntimeException("Agency not found");
        }

        if (!agency.getRole().equals(Role.AGENCY)) {
            throw new RuntimeException("User is not an agency");
        }

        int nights = calculatingNights(tourAddDto.getStartDate(), tourAddDto.getReturnDate());

        Tour newTour = Tour.builder()
                .title(tourAddDto.getTitle())
                .agencyId(agencyId)
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

        tourRepository.addTour(newTour);
        return newTour;
    }


    @Override
    public void deleteTour(UUID agencyId, UUID tourId) {

        User agency = userRepository.getUserById(agencyId);

        if(agency.getRole().equals(Role.AGENCY) && tourId != null) {
            favTourRepository.deleteAllFavToursIfTourDeleted(tourId);
            tourRepository.deleteTour(tourId);
        }
    }

    @Override
    public Tour updateTour(UUID agencyId, UUID tourId, TourUpdateDto tourUpdateDto) {

        Tour existingTour = tourRepository.getTourById(tourId);

        if(existingTour == null) {
            throw new RuntimeException("Tour not found");
        }

        if(!existingTour.getAgencyId().equals(agencyId)) {
            throw new RuntimeException("Agency not found");
        }
        Integer nights = calculatingNights(tourUpdateDto.getStartDate(), tourUpdateDto.getReturnDate());

        existingTour.setTitle(tourUpdateDto.getTitle());
        existingTour.setDescription(tourUpdateDto.getDescription());
        existingTour.setCity(tourUpdateDto.getCity());
        existingTour.setPrice(tourUpdateDto.getPrice());
        existingTour.setSeatsTotal(tourUpdateDto.getSeatsTotal());
        existingTour.setSeatsAvailable(tourUpdateDto.getSeatsTotal());
        existingTour.setStartDate(tourUpdateDto.getStartDate());
        existingTour.setReturnDate(tourUpdateDto.getReturnDate());
        existingTour.setNights(nights);
        existingTour.setHotel(tourUpdateDto.getHotel());
        return  existingTour;
    }

    @Override
    public List<TourResponseDto> getAllTours() {
        return tourRepository.getAllTours().stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public TourResponseDto getTourById(UUID userId, UUID tourId) {
        Tour tour = tourRepository.getTourById(tourId);
        if(tour == null) {
            throw new RuntimeException("Tour not found");
        }

        User user = userRepository.getUserById(userId);
        if(user == null) {
            throw new RuntimeException("User not found");
        }

        tour.setViews(tour.getViews() + 1L);
        return toResponseDto(tour);
    }

    @Override
    public List<TourResponseDto> getAllToursByAgencyId(UUID agencyId) {
        return tourRepository.getAllTours().stream()
                .filter(tour -> tour.getAgencyId().equals(agencyId))
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
        Tour tour = tourRepository.getTourById(tourId);
        tour.setSeatsAvailable(tour.getSeatsAvailable() + 1);
        if(tour.getSeatsAvailable() == 0){
            tour.setAvailable(true);
        }
    }

    @Override
    public Tour addRatingTour(UUID tourId, Float rating) {
        Tour existTour = tourRepository.getTourById(tourId);
        if(existTour == null) {
            throw new RuntimeException("Tour not found while giving rating");
        }
        existTour.setRating(rating);
        return existTour;
    }

    private TourResponseDto toResponseDto(Tour tour) {
        User agency = userRepository.getUserById(tour.getAgencyId());
        return TourResponseDto.builder()
                .id(tour.getId())
                .agencyName(agency.getFullName())
                .agencyId(agency.getId())
                .title(tour.getTitle())
                .description(tour.getDescription())
                .nights(calculatingNights(tour.getStartDate(), tour.getReturnDate()))
                .startDate(tour.getStartDate())
                .returnDate(tour.getReturnDate())
                .price(tour.getPrice())
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
