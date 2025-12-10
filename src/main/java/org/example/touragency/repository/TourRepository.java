package org.example.touragency.repository;

import org.example.touragency.dto.response.TourResponseDto;
import org.example.touragency.model.enity.Tour;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TourRepository {

    private final Map<UUID, Tour> tours = new ConcurrentHashMap<>();

    public Tour addTour(Tour tour) {
        tours.put(tour.getId(), tour);
        return tour;
    }

    public void deleteTour(UUID id) {
        tours.remove(id);
    }

    public Tour getTourById(UUID id) {
        return (tours.get(id));
    }

    public List<Tour> getToursByAgencyId(UUID agencyId) {
        return tours.values().stream()
                .filter(tour -> tour.getAgencyId().equals(agencyId))
                .toList();
    }

    public List<Tour> getAllTours() {
        return new ArrayList<>(tours.values());
    }

    public void deleteAgencyAllTours(UUID id) {
        tours.values().removeIf(tour -> tour.getAgencyId().equals(id));
    }






}
