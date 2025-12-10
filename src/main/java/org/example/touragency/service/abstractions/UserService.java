package org.example.touragency.service.abstractions;

import org.example.touragency.dto.request.UserAddDto;
import org.example.touragency.dto.response.UserUpdateDto;
import org.example.touragency.model.enity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User addNewUser(UserAddDto userAddDto);

    void deleteUser(UUID userId);

    User updateUser(UserUpdateDto userUpdateDto);

    List<User> getAllUsers();
}
