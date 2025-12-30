package org.example.touragency.repository;

import org.example.touragency.model.entity.Rating;
import org.example.touragency.model.entity.RatingCounter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Repository
public class RatingRepository extends AbstractHibernateRepository{


    protected RatingRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void saveRating(Optional<Rating> rating){
        executeInTransaction(session -> {
            session.persist(rating);
            return rating;
        });
    }

    public void saveCounter(Optional<RatingCounter> ratingCounter) {
        executeInTransaction(session -> {
            session.persist(ratingCounter);
            return ratingCounter;
        });
    }

    public Rating updateRating(Rating rating) {
        return executeInTransaction(session -> {
            session.merge(rating);
            return rating;
        });
    }

    public RatingCounter updateRating(RatingCounter ratingCounter) {
        return executeInTransaction(session -> {
            session.merge(ratingCounter);
            return ratingCounter;
        });
    }

   public Optional<Rating> findRatingByUserAndTourIds(UUID userId, UUID tourId){
       return executeInTransaction((Function<Session, Optional<Rating>>)  session ->
           session.createQuery("FROM Rating WHERE user.id = :userId AND tour.id = :tourId", Rating.class)
                   .setParameter("userId", userId)
                   .setParameter("tourId", tourId)
                   .uniqueResultOptional()
       );
   }


    public Optional<RatingCounter> findRatingCounterByTourId(UUID tourId){
        return executeInTransaction((Function<Session, Optional<RatingCounter>>) session ->
                session.createQuery("FROM RatingCounter WHERE tourId =: tourId", RatingCounter.class)
                        .setParameter("tourId", tourId)
                        .uniqueResultOptional()
        );
    }



}
