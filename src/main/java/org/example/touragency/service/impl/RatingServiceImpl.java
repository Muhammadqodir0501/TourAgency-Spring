package org.example.touragency.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.touragency.dto.request.RatingDto;
import org.example.touragency.model.entity.Rating;
import org.example.touragency.model.entity.RatingCounter;
import org.example.touragency.model.entity.Tour;
import org.example.touragency.model.entity.User;
import org.example.touragency.repository.RatingRepository;
import org.example.touragency.repository.TourRepository;
import org.example.touragency.repository.UserRepository;
import org.example.touragency.service.abstractions.RatingService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final TourRepository tourRepository;
    private final UserRepository userRepository;

    @Override
    public Rating addRating(RatingDto ratingDto) {

        UUID tourId = ratingDto.getTourId();
        UUID userId = ratingDto.getUserId();

        if (tourId == null || userId == null) {
            throw new RuntimeException("Tour or User ID is null");
        }

        Optional<Rating> existRating = ratingRepository.findRatingByUserAndTourIds(userId, tourId);
        Optional<Rating> rating;
        Optional<Tour> existTour = tourRepository.findById(tourId);
        Optional<User> existUser = userRepository.findById(userId);

        if(existRating.isPresent()){
            updateExistRating(ratingDto, existRating.orElse(null));
            rating = existRating;
        }else{
            rating = Optional.ofNullable(Rating.builder()
                    .tour(existTour.get())
                    .user(existUser.get())
                    .rating(ratingDto.getRating())
                    .build());
            ratingRepository.saveRating(rating);
            ratingCount(ratingDto);
        }
        syncTourRatingFromCounter(tourId);
        return rating.orElse(null);
    }

    @Override
    public void ratingCount(RatingDto ratingDto) {
        UUID tourId = ratingDto.getTourId();
        Optional<RatingCounter> counter = ratingRepository.findRatingCounterByTourId(tourId);

        if(counter.isPresent()){
            float newAvg = (counter.get().getAverageRating() * counter.get().getRatingAmount() + ratingDto.getRating())
                    / (counter.get().getRatingAmount() + 1);

            counter.get().setAverageRating(newAvg);
            counter.get().setRatingAmount(counter.get().getRatingAmount() + 1);
        }else{
            counter = Optional.ofNullable(RatingCounter.builder()
                    .tourId(tourId)
                    .averageRating(ratingDto.getRating())
                    .ratingAmount(1F)
                    .build());
            ratingRepository.saveCounter(counter);
        }
    }

    @Override
    public void updateExistRating(RatingDto ratingDto, Rating existRating) {
        Optional<RatingCounter> counter = ratingRepository.findRatingCounterByTourId(existRating.getTour().getId());

        if(counter.isPresent()){
            float newAvg = (counter.get().getAverageRating() * counter.get().getRatingAmount()
                    - existRating.getRating() + ratingDto.getRating())
                    / counter.get().getRatingAmount();

            counter.get().setAverageRating(newAvg);
            existRating.setRating(ratingDto.getRating());

        }
    }

    private void syncTourRatingFromCounter(UUID tourId) {
        Optional<Tour> tour = tourRepository.findById(tourId);
        Optional<RatingCounter> counter = ratingRepository.findRatingCounterByTourId(tourId);

        if (tour.isEmpty()) return;

        if (counter.isPresent()) {
            tour.get().setRating(counter.get().getAverageRating());
        } else {
            tour.get().setRating(null);
        }
    }

}
