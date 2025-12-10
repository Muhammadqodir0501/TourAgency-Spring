package org.example.touragency.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.touragency.dto.request.UserAddDto;
import org.example.touragency.dto.response.UserUpdateDto;
import org.example.touragency.model.Role;
import org.example.touragency.model.enity.User;
import org.example.touragency.repository.FavTourRepository;
import org.example.touragency.repository.TourRepository;
import org.example.touragency.repository.UserRepository;
import org.example.touragency.service.abstractions.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final FavTourRepository favTourRepository;

    @Override
    public User addNewUser(UserAddDto userAddDto) {
        User existUser = userRepository.findByPhoneNumber(userAddDto.getPhoneNumber());

        if(userAddDto!=null && existUser==null){
            User newUser = User.builder()
                    .fullName(userAddDto.getFullName())
                    .email(userAddDto.getEmail())
                    .password(userAddDto.getPassword())
                    .role(userAddDto.getRole())
                    .phoneNumber(userAddDto.getPhoneNumber())
                    .build();
            userRepository.addUser(newUser);
            return newUser;
        }
        return null;
    }

    @Override
    public void deleteUser(UUID userId) {
        if(userId!=null){
            User deletingUser = userRepository.getUserById(userId);

            if(deletingUser!=null){

                if(deletingUser.getRole() == Role.AGENCY){
                    tourRepository.deleteAgencyAllTours(deletingUser.getId());
                }
                favTourRepository.deleteAllFavouriteToursByUserId(deletingUser.getId());
                userRepository.deleteUser(deletingUser);

            }
        }
    }

    @Override
    public User updateUser(UserUpdateDto userUpdateDto) {
        User existingUser = userRepository.getUserById(userUpdateDto.getUserId());

        if(existingUser!=null){
            existingUser.setFullName(userUpdateDto.getFullName());
            existingUser.setEmail(userUpdateDto.getEmail());
            existingUser.setPassword(userUpdateDto.getPassword());
            existingUser.setPhoneNumber((userUpdateDto.getPhoneNumber()));
            return existingUser;
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }



}
