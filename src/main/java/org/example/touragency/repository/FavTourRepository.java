package org.example.touragency.repository;


import org.example.touragency.model.entity.FavouriteTour;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Repository
public class FavTourRepository extends AbstractHibernateRepository{


    protected FavTourRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public FavouriteTour save(FavouriteTour favouriteTour) {
        return executeInTransaction(session ->  {
            session.persist(favouriteTour);
            return favouriteTour;
        });
    }

    public void deleteFavouriteTourByUserId(UUID userId, UUID tourId) {
        executeInTransaction(session -> {
            session.createMutationQuery("DELETE FROM FavouriteTour WHERE user.id = :userId AND tour.id = :tourId")
                    .setParameter("userId", userId)
                    .setParameter("tourId", tourId)
                    .executeUpdate();
        });
    }

    public void deleteAllByUserId(UUID userId){
        executeInTransaction(session -> {
            session.createMutationQuery("DELETE FROM FavouriteTour WHERE user.id = :userId")
                    .setParameter("userId", userId)
                    .executeUpdate();
        });
    }

    public List<FavouriteTour> findAllByUserId(UUID userId){
        return executeInTransaction((Function<Session, List<FavouriteTour>>) session->
                session.createQuery("FROM FavouriteTour WHERE user.id = :userId", FavouriteTour.class)
                .setParameter("userId", userId)
                        .list()
                );
    }

    public void deleteAllIfTourDeleted(UUID tourId) {
        executeInTransaction(session -> {
            session.createMutationQuery("DELETE FROM FavouriteTour WHERE tour.id = :tourId")
                    .setParameter("tourId", tourId)
                    .executeUpdate();
        });
    }

}
