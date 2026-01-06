package org.example.touragency.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.touragency.dto.request.UserAddDto;
import org.example.touragency.dto.response.UserUpdateDto;
import org.example.touragency.model.Role;
import org.example.touragency.model.entity.User;
import org.example.touragency.repository.*;
import org.example.touragency.service.abstractions.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RatingService ratingService;
    private final FavouriteTourService favouriteTourService;
    private final BookingService bookingService;
    private final TourService tourService;
    private final RatingRepository ratingRepository;
    private final FavTourRepository favTourRepository;
    private final BookingRepository bookingRepository;
    private final TourRepository tourRepository;

    @Override
    public User addNewUser(UserAddDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("UserAddDto cannot be null");
        }
        if (userRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("The phone number already exists");
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("The email already exists");
        }

        User newUser = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .role(dto.getRole())
                .build();

        return userRepository.save(newUser);
    }

    @Override
    public void deleteUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }

        userRepository.findById(userId).ifPresent(user -> {
            if (user.getRole() == Role.AGENCY) {
                tourRepository.deleteAllByAgencyId(user.getId());
            }
            ratingRepository.deleteAllIfUserDeleted(userId);
            favTourRepository.deleteAllIfUserDeleted(userId);
            bookingRepository.deleteAllIfUserDeleted(userId);
            userRepository.deleteById(userId);
        });
    }

    @Override
    public User updateUser(UUID userId, UserUpdateDto dto) {
        if (userId == null || dto == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setFullName(dto.getFullName());
                    existingUser.setEmail(dto.getEmail());
                    existingUser.setPassword(dto.getPassword());
                    existingUser.setPhoneNumber(dto.getPhoneNumber());
                    return userRepository.update(existingUser);
                })
                .orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

}