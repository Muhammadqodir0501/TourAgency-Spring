package org.example.touragency.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.touragency.dto.response.FavTourResponseDto;
import org.example.touragency.model.entity.FavouriteTour;
import org.example.touragency.model.entity.Tour;
import org.example.touragency.model.entity.User;
import org.example.touragency.repository.FavTourRepository;
import org.example.touragency.repository.TourRepository;
import org.example.touragency.repository.UserRepository;
import org.example.touragency.service.abstractions.FavouriteTourService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FavouriteTourServiceImpl implements FavouriteTourService {

    private final FavTourRepository favTourRepository;
    private final TourRepository tourRepository;
    private  final UserRepository userRepository;


    @Override
    public FavTourResponseDto addFavouriteTour(UUID  userId, UUID tourId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        FavouriteTour favouriteTour = FavouriteTour.builder()
                .tour(tour)
                .user(user)
                .build();

        favTourRepository.save(favouriteTour);

        return new FavTourResponseDto(
                favouriteTour.getId(),
                user.getId(),
                tour.getId()
        );
    }

    @Override
    public void deleteFavouriteTour(UUID userId, UUID tourId) {
        favTourRepository.deleteByUserIdAndTourId(userId,tourId);
    }

    @Override
    public List<FavTourResponseDto> getUserFavouriteTours(UUID userId) {
        List<FavouriteTour> favouriteTours = favTourRepository.findAllByUserId(userId);

        return favouriteTours.stream()
                .map(f -> new FavTourResponseDto(
                        f.getId(),
                        f.getUser().getId(),
                        f.getTour().getId()
                ))
                .toList();
    }
}
