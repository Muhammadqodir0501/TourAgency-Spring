package org.example.touragency.repository;

import lombok.RequiredArgsConstructor;
import org.example.touragency.model.Role;
import org.example.touragency.model.enity.Tour;
import org.example.touragency.model.enity.User;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {

    public UserRepository() {
        addDefaultUsers();
    }

    private final Map<UUID, User> users = new ConcurrentHashMap<>();

    private  void addDefaultUsers() {
        User user1 = new User();
        user1.setFullName("John Doe");
        user1.setEmail("john@example.com");
        user1.setPassword("password123");
        user1.setPhoneNumber("+998946780090");
        user1.setRole(Role.USER);

        User user2 = new User();
        user2.setFullName("Admin Boss");
        user2.setEmail("admin@example.com");
        user2.setPassword("admin123");
        user2.setPhoneNumber("+998965432100");
        user2.setRole(Role.AGENCY);

        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);
    }

    public User addUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public void deleteUser(User user) {
        users.remove(user.getId());
    }

    public User findByPhoneNumber(String phoneNumber) {
        return users.values().stream()
                .filter(user -> phoneNumber != null &&
                        phoneNumber.equals(user.getPhoneNumber()))
                .findFirst()
                .orElse(null);
    }

    public User getUserById(UUID id) {
        return users.get(id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

}
