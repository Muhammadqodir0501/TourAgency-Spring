package org.example.touragency.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.touragency.dto.request.FavouriteTourDto;
import org.example.touragency.model.enity.FavouriteTour;
import org.example.touragency.model.enity.Tour;
import org.example.touragency.repository.FavTourRepository;
import org.example.touragency.repository.TourRepository;
import org.example.touragency.service.abstractions.FavouriteTourService;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FavouriteTourServiceImpl implements FavouriteTourService {

    private final FavTourRepository favTourRepository;
    private final TourRepository tourRepository;


    @Override
    public void addFavouriteTour(FavouriteTourDto favouriteTourDto) {

        FavouriteTour favouriteTour = FavouriteTour.builder()
                .tourId(favouriteTourDto.getTourId())
                .userId(favouriteTourDto.getUserId())
                .build();
        favTourRepository.addFavouriteTour(favouriteTour);
    }

    @Override
    public void deleteFavouriteTour(FavouriteTourDto favouriteTourDto) {
        favTourRepository.deleteFavouriteTourByUserId(favouriteTourDto.getUserId(),favouriteTourDto.getTourId());
    }

    @Override
    public List<Tour> getUserFavouriteTours(UUID userId) {
        List<UUID> favToursId =  favTourRepository.getFavouriteTour(userId);
        List<Tour> tours = new ArrayList<>();

        for(UUID favTourId : favToursId) {
            Tour favTourById = tourRepository.getTourById(favTourId);
            tours.add(favTourById);
        }
        return tours;
    }
}
