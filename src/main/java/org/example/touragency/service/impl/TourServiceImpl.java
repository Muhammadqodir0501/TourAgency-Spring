package org.example.touragency.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.touragency.dto.request.TourAddDto;
import org.example.touragency.dto.request.TourDeleteDto;
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

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final UserRepository userRepository;
    private final FavTourRepository favTourRepository;


    @Override
    public Tour addNewTour(TourAddDto tourAddDto) {

        Integer nights = calculatingNights(tourAddDto.getStartDate(), tourAddDto.getReturnDate());
        User agency = userRepository.getUserById(tourAddDto.getAgencyId());

        if(agency.getRole().equals(Role.AGENCY)) {
            Tour newTour = Tour.builder()
                    .title(tourAddDto.getTitle())
                    .agencyId(tourAddDto.getAgencyId())
                    /*.city(tourAddDto.getCity(city)) */
                    .hotel(tourAddDto.getHotel())
                    .description(tourAddDto.getDescription())
                    .startDate(tourAddDto.getStartDate())
                    .returnDate(tourAddDto.getReturnDate())
                    .nights(nights)
                    .price(tourAddDto.getPrice())
                    .seatsTotal(tourAddDto.getSeatsTotal())
                    .seatsAvailable(tourAddDto.getSeatsTotal())
                    .build();
            tourRepository.addTour(newTour);
            return newTour;
        }
        return null;

    }

    @Override
    public void deleteTour(TourDeleteDto tourDeleteDto) {

        User agency = userRepository.getUserById(tourDeleteDto.getAgencyId());

        if(agency.getRole().equals(Role.AGENCY) && tourDeleteDto.getTourId() != null) {
            favTourRepository.deleteAllFavToursIfTourDeleted(tourDeleteDto.getTourId());
            tourRepository.deleteTour(tourDeleteDto.getTourId());
        }
    }

    @Override
    public Tour updateTour(TourUpdateDto tourUpdateDto) {
        Tour existingTour = tourRepository.getTourById(tourUpdateDto.getTourId());

        if(existingTour != null) {
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
        return null;
    }

    @Override
    public List<TourResponseDto> getAllTours() {
        return tourRepository.getAllTours().stream()
                .map(this::toResponseDto)
                .toList();
    }

    private TourResponseDto toResponseDto(Tour tour) {
        User agency = userRepository.getUserById(tour.getAgencyId());
        return TourResponseDto.builder()
                .id(tour.getId())
                .agencyName(agency.getFullName())
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
