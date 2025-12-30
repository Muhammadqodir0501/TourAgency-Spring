package org.example.touragency.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.touragency.model.entity.FavouriteTour;
import org.example.touragency.model.entity.Tour;
import org.example.touragency.model.entity.User;
import org.example.touragency.repository.FavTourRepository;
import org.example.touragency.repository.TourRepository;
import org.example.touragency.repository.UserRepository;
import org.example.touragency.service.abstractions.FavouriteTourService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FavouriteTourServiceImpl implements FavouriteTourService {

    private final FavTourRepository favTourRepository;
    private final TourRepository tourRepository;
    private  final UserRepository userRepository;


    @Override
    public FavouriteTour addFavouriteTour(UUID  userId, UUID tourId) {
        Optional<Tour> existTour = tourRepository.findById(tourId);
        Optional<User> existUser = userRepository.findById(userId);
        if (existTour.isPresent() && existUser.isPresent()) {
            FavouriteTour favouriteTour = FavouriteTour.builder()
                    .tour(existTour.get())
                    .user(existUser.get())
                    .build();

            return favTourRepository.save(favouriteTour);
        }
        return null;
    }

    @Override
    public void deleteFavouriteTour(UUID userId, UUID tourId) {
        favTourRepository.deleteFavouriteTourByUserId(userId,tourId);
    }

    @Override
    public List<Tour> getUserFavouriteTours(UUID userId) {
        List<FavouriteTour> favTours =  favTourRepository.findAllByUserId(userId);
        List<Tour> tours = new ArrayList<>();

        for(FavouriteTour favTour : favTours) {
            tours.add(favTour.getTour());
        }
        return tours;
    }
}
