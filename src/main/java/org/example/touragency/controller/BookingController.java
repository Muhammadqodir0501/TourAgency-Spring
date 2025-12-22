package org.example.touragency.controller;

import lombok.RequiredArgsConstructor;
import org.example.touragency.exception.ApiResponse;
import org.example.touragency.model.enity.Booking;
import org.example.touragency.service.abstractions.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings/{userId}")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/{tourId}")
    public ResponseEntity<ApiResponse<?>>  addBooking(@PathVariable UUID userId, @PathVariable UUID tourId) {
        Booking booking = bookingService.addBooking(userId,  tourId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(booking));
    }

    @DeleteMapping("/{tourId}")
    public ResponseEntity<ApiResponse<?>>  cancelBooking(@PathVariable UUID userId, @PathVariable UUID tourId) {
        bookingService.cancelBooking(userId, tourId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Booking>>> getBookings(@PathVariable UUID userId) {
        List<Booking> userBookings = bookingService.getUsersBookings(userId);
        return ResponseEntity.ok(new ApiResponse<>(userBookings));
    }
}
