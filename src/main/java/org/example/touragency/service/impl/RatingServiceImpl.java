package org.example.touragency.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.touragency.repository.Database;
import org.example.touragency.dto.request.RatingDto;
import org.example.touragency.model.enity.Rating;
import org.example.touragency.model.enity.RatingCounter;
import org.example.touragency.model.enity.Tour;
import org.example.touragency.service.abstractions.RatingService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final Database db;

    @Override
    public void addRating(RatingDto ratingDto) {
        UUID tourId = UUID.fromString(ratingDto.getTourId().toString());
        UUID userId = UUID.fromString(ratingDto.getUserId().toString());

        Rating existRating = findRatingByUserAndTour(userId, tourId);

        if (existRating != null) {
            updateExistRating(ratingDto, existRating);
        } else {
            Rating rating = Rating.builder()
                    .tourId(tourId)
                    .userId(userId)
                    .rating(ratingDto.getRating())
                    .build();

            db.ratings.put(rating.getId(), rating);
            ratingCount(ratingDto);
        }
        syncTourRatingFromCounter(tourId);
    }

    private Rating findRatingByUserAndTour(UUID userId, UUID tourId) {
        return db.ratings.values().stream()
                .filter(r -> r.getUserId().equals(userId) && r.getTourId().equals(tourId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void ratingCount(RatingDto ratingDto) {
        UUID tourId = UUID.fromString(ratingDto.getTourId().toString());
        RatingCounter counter = db.ratingCounters.get(tourId);

        if (counter != null) {
            float newAvg = (counter.getAverageRating() * counter.getRatingAmount() + ratingDto.getRating())
                    / (counter.getRatingAmount() + 1);

            counter.setAverageRating(newAvg);
            counter.setRatingAmount(counter.getRatingAmount() + 1);
        } else {
            counter = RatingCounter.builder()
                    .tourId(tourId)
                    .averageRating(ratingDto.getRating())
                    .ratingAmount(1F)
                    .build();
            db.ratingCounters.put(tourId, counter);
        }

    }

    @Override
    public void updateExistRating(RatingDto ratingDto, Rating existRating) {
        UUID tourId = existRating.getTourId();
        RatingCounter counter = db.ratingCounters.get(tourId);

        if (counter != null) {
            float newAvg = (counter.getAverageRating() * counter.getRatingAmount()
                    - existRating.getRating() + ratingDto.getRating())
                    / counter.getRatingAmount();

            counter.setAverageRating(newAvg);
            existRating.setRating(ratingDto.getRating());
        }
    }

    private void syncTourRatingFromCounter(UUID tourId) {
        Tour tour = db.tours.get(tourId);
        RatingCounter counter = db.ratingCounters.get(tourId);

        if (tour == null) return;

        if (counter != null) {
            tour.setRating(counter.getAverageRating());
        } else {
            tour.setRating(null);
        }
    }
}