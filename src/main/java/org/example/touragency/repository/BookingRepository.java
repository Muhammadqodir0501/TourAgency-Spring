package org.example.touragency.repository;

import org.example.touragency.model.entity.Booking;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Repository
public class BookingRepository extends AbstractHibernateRepository {


    protected BookingRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Booking save(Booking booking) {
        return executeInTransaction(session ->  {
            session.persist(booking);
            return booking;
        });
    }

    public Optional<Booking> findBookingByUserAndTourId(UUID userId, UUID tourId) {
        return executeInTransaction((Function<Session, Optional<Booking>>) session ->
            session.createQuery("FROM Booking WHERE user.id = :userId AND tour.id = :tourId", Booking.class)
                    .setParameter("userId", userId)
                    .setParameter("tourId", tourId)
                    .uniqueResultOptional()
        );
    }

    public List<Booking> findAllBookingsByUserId(UUID userId) {
        return executeInTransaction((Function<Session, List<Booking>>) session ->
                session.createQuery("FROM Booking b WHERE b.user.id = :userId", Booking.class)
                        .setParameter("userId", userId)
                        .list()
        );
    }

    public void deleteByUserIdAndTourId(UUID userId, UUID tourId) {
        executeInTransaction(session -> {
           session.createMutationQuery("DELETE FROM Booking WHERE user.id = :userId AND tour.id = :tourId")
                   .setParameter("userId", userId)
                   .setParameter("tourId", tourId)
                   .executeUpdate();
        });
    }

}
