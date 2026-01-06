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

    public void saveRating(Rating rating) {
        executeInTransaction(session -> {
            session.persist(rating);
            return rating;
        });
    }

    public void saveCounter(RatingCounter ratingCounter) {
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

    public void updateCounter(RatingCounter counter) {
        executeInTransaction(session -> {
            session.merge(counter);
            return counter;
        });
    }


    public Optional<Rating> findRatingByUserAndTourIds(UUID userId, UUID tourId){
        return executeInTransaction((Function<Session, Optional<Rating>>)  session ->
                session.createQuery("FROM Rating WHERE userId = :userId AND tourId = :tourId", Rating.class)
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

    public void deleteAllCountersIfTourDeleted(UUID tourId) {
        executeInTransaction(session -> {
            session.createMutationQuery("DELETE FROM RatingCounter WHERE tourId = :tourId")
                    .setParameter("tourId", tourId)
                    .executeUpdate();
        });
    }

    public void deleteAllRatingsIfTourDeleted(UUID tourId) {
        executeInTransaction(session -> {
            session.createMutationQuery("DELETE FROM Rating WHERE tourId = :tourId")
                    .setParameter("tourId", tourId)
                    .executeUpdate();
        });
    }

    public void deleteAllIfUserDeleted(UUID userId){
        executeInTransaction(session -> {
            session.createMutationQuery("DELETE FROM Rating WHERE userId = :userId")
                    .setParameter("userId", userId)
                    .executeUpdate();
        });
    }


}