package org.example.touragency.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.touragency.dto.request.RatingDto;
import org.example.touragency.model.enity.Rating;
import org.example.touragency.model.enity.RatingCounter;
import org.example.touragency.model.enity.Tour;
import org.example.touragency.repository.RatingRepository;
import org.example.touragency.repository.TourRepository;
import org.example.touragency.service.abstractions.RatingService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final TourRepository tourRepository;
    private final TourServiceImpl tourServiceImpl;

    @Override
    public Rating addRating(RatingDto ratingDto) {
        UUID tourId = ratingDto.getTourId();
        UUID userId = ratingDto.getUserId();
        if(tourId == null){
            throw new RuntimeException("Tour not found");
        }
        if(userId == null){
            throw new RuntimeException("User not found");
        }

        Rating existRating = ratingRepository.findRatingByUserAndTourIds(userId, tourId);
        if(existRating != null){
            updateExistRating(ratingDto, existRating);
        }else{
            Rating rating = Rating.builder()
                    .tourId(tourId)
                    .userId(userId)
                    .rating(ratingDto.getRating())
                    .build();
            ratingRepository.addRating(rating);
            tourServiceImpl.addRatingTour(tourId,ratingDto.getRating());
            ratingCount(ratingDto);
            return rating;
        }
        syncTourRatingFromCounter(tourId);
        return null;
    }

    @Override
    public void ratingCount(RatingDto ratingDto) {
        UUID tourId = ratingDto.getTourId();
        RatingCounter counter = ratingRepository.findRatingCounterByTourId(tourId);

        if(counter != null){
            float newAvg = (counter.getAverageRating() * counter.getRatingAmount() + ratingDto.getRating())
                    / (counter.getRatingAmount() + 1);

            counter.setAverageRating(newAvg);
            counter.setRatingAmount(counter.getRatingAmount() + 1);
        }else{
            counter = RatingCounter.builder()
                    .tourId(tourId)
                    .averageRating(ratingDto.getRating())
                    .ratingAmount(1F)
                    .build();
            ratingRepository.addRatingCounter(counter);
        }
    }

    @Override
    public void updateExistRating(RatingDto ratingDto, Rating existRating) {

        UUID tourId = existRating.getTourId();

        if(tourId == null){
            throw new RuntimeException("Tour not found");
        }

        RatingCounter counter = ratingRepository.findRatingCounterByTourId(tourId);

        if(counter != null){
            float newAvg = (counter.getAverageRating() * counter.getRatingAmount()
                    - existRating.getRating() + ratingDto.getRating())
                    / counter.getRatingAmount();

            counter.setAverageRating(newAvg);
            existRating.setRating(ratingDto.getRating());
            tourServiceImpl.addRatingTour(tourId,newAvg);

        }
    }

    private void syncTourRatingFromCounter(UUID tourId) {
        Tour tour = tourRepository.getTourById(tourId);
        RatingCounter counter = ratingRepository.findRatingCounterByTourId(tourId);

        if (tour == null) return;

        if (counter != null) {
            tour.setRating(counter.getAverageRating());
        } else {
            tour.setRating(null);
        }
    }

}
