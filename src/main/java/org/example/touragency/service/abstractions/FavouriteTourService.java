package org.example.touragency.service.abstractions;

import org.example.touragency.dto.request.FavouriteTourDto;
import org.example.touragency.model.enity.Tour;

import java.util.List;
import java.util.UUID;

public interface FavouriteTourService {
    void addFavouriteTour(FavouriteTourDto favouriteTourDto);

    void deleteFavouriteTour(FavouriteTourDto favouriteTourDto);

    List<Tour> getUserFavouriteTours(UUID userId);
}
