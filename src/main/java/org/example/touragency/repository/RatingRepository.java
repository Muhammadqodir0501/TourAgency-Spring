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

    private void addRating(Rating rating) {
        ratings.put(rating.getId(), rating);
    }

    private void addRatingCounter(RatingCounter ratingCounter) {
        ratingCounters.put(ratingCounter.getTourId(), ratingCounter);
    }

    private void updateRating(Rating rating) {
        ratings.remove(rating.getId());
        ratings.put(rating.getId(), rating);
    }

    private void updateRatingCounter(RatingCounter ratingCounter) {
        ratingCounters.remove(ratingCounter.getTourId());
        ratingCounters.put(ratingCounter.getTourId(), ratingCounter);
    }

    private RatingCounter getRatingCounter(UUID tourId) {
        return ratingCounters.get(tourId);
    }



}
