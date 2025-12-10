package org.example.touragency.repository;

import org.example.touragency.model.enity.Rating;
import org.example.touragency.model.enity.RatingCounter;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RatingRepository {
    private final Map<UUID, Rating> ratings = new ConcurrentHashMap<>();
    private final Map<UUID, RatingCounter> ratingCounters = new ConcurrentHashMap<>();

    public void addRating(Rating rating) {
        ratings.put(rating.getId(), rating);
    }

    public void addRatingCounter(RatingCounter ratingCounter) {
        ratingCounters.put(ratingCounter.getId(), ratingCounter);
    }

    public void updateRating(Rating rating) {
        ratings.remove(rating.getId());
        ratings.put(rating.getId(), rating);
    }

    public void updateRatingCounter(RatingCounter ratingCounter) {
        ratingCounters.remove(ratingCounter.getTourId());
        ratingCounters.put(ratingCounter.getTourId(), ratingCounter);
    }

    public RatingCounter getRatingCounter(UUID tourId) {
        return ratingCounters.get(tourId);
    }

    public Rating findRatingByUserAndTourIds(UUID userId, UUID tourId) {
        return ratings.values().stream()
                .filter(r -> r.getTourId().equals(tourId) && r.getTourId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public RatingCounter findRatingCounterByTourId(UUID tourId) {
        return  ratingCounters.values().stream()
                .filter(r -> r.getTourId().equals(tourId))
                .findFirst()
                .orElse(null);
    }



}
