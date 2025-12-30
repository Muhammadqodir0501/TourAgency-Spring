package org.example.touragency.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.touragency.model.entity.Booking;
import org.example.touragency.model.entity.Tour;
import org.example.touragency.model.entity.User;
import org.example.touragency.repository.BookingRepository;
import org.example.touragency.repository.TourRepository;
import org.example.touragency.repository.UserRepository;
import org.example.touragency.service.abstractions.BookingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final TourServiceImpl tourServiceImpl;

    @Override
    public Booking addBooking(UUID userId, UUID tourId) {

        Optional<Tour> existTour = tourRepository.findById(tourId);
        if(existTour.isEmpty()) {
            throw new RuntimeException("Tour not found");
        }

        Optional<User> existUser = userRepository.findById(userId);
        if(existUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        if(!existTour.get().isAvailable()) {
            throw new RuntimeException("Tour not available");
        }
        Booking newBooking = Booking.builder()
                .user(existUser.get())
                .tour(existTour.get())
                .build();


        tourServiceImpl.tourIsBooked(existTour.orElse(null));
        return bookingRepository.save(newBooking);
    }

    @Override
    public List<Booking> getUsersBookings(UUID userId) {
        return bookingRepository.findAllBookingsByUserId(userId);
    }

    @Override
    public void cancelBooking(UUID userId, UUID tourId) {
        tourServiceImpl.tourBookingIsCanceled(tourId);
        bookingRepository.deleteByUserIdAndTourId(userId, tourId);
    }
}
