package org.example.touragency.repository;

import org.example.touragency.model.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;


@Repository
public class UserRepository extends AbstractHibernateRepository {

    public UserRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public User save(User user) {
        return executeInTransaction(session -> {
            session.persist(user);
            //  session.merge(user); update user
            return user;
        });
    }

    public User update(User user) {
        return executeInTransaction(session -> {
            session.merge(user);
            return user;
        });
    }

    public Optional<User> findById(UUID id) {
        return executeInTransaction((Function<Session, Optional<User>>) session ->
                Optional.ofNullable(session.get(User.class, id))
        );
    }

    public Optional<User> findByEmail(String email) {
        return executeInTransaction((Function<Session, Optional<User>>) session ->
                session.createQuery("FROM User WHERE email = :email", User.class)
                        .setParameter("email", email)
                        .uniqueResultOptional()
        );
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return executeInTransaction((Function<Session, Optional<User>>) session ->
                session.createQuery("FROM User WHERE phoneNumber = :phone", User.class)
                        .setParameter("phone", phoneNumber)
                        .uniqueResultOptional()
        );
    }

    public List<User> findAll() {
        return executeInTransaction((Function<Session, List<User>>) session ->
                session.createQuery("FROM User", User.class).list()
        );
    }

    // Удалить по ID
    public void deleteById(UUID id) {
        executeInTransaction(session -> {
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
        });
    }

    // Удалить пользователя (по объекту)
    public void delete(User user) {
        executeInTransaction(session -> {
            session.remove(session.merge(user));  // на случай detached объекта
        });
    }
}