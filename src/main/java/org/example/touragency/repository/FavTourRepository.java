package org.example.touragency.repository;

import lombok.RequiredArgsConstructor;
import org.example.touragency.dto.request.FavouriteTourDto;
import org.example.touragency.model.enity.FavouriteTour;
import org.example.touragency.model.enity.Tour;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FavTourRepository {

    private final Map<UUID, FavouriteTour> favorites = new ConcurrentHashMap<>();


    public FavouriteTour addFavouriteTour(FavouriteTour favouriteTour) {
        favorites.put(favouriteTour.getId(), favouriteTour);
        return favouriteTour;
    }

    public void deleteFavouriteTourByUserId(UUID userId, UUID tourId) {
        favorites.values().removeIf(fav -> fav.getUserId().equals(userId)
                && fav.getTourId().equals(tourId));
    }

    public void deleteAllFavouriteToursByUserId(UUID userId) {
        favorites.values().removeIf(fav -> fav.getUserId().equals(userId));
    }

    public List<UUID> getFavouriteTour(UUID userId) {
        return favorites.values().stream()
                .filter(fav -> fav.getUserId().equals(userId))
                .map(FavouriteTour::getTourId)
                .toList();
    }

    public void deleteAllFavToursIfTourDeleted(UUID tourId) {
        favorites.values().removeIf(fav -> fav.getTourId().equals(tourId));
    }

}
