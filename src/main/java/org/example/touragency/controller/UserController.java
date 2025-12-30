package org.example.touragency.controller;

import lombok.RequiredArgsConstructor;
import org.example.touragency.dto.request.UserAddDto;
import org.example.touragency.dto.response.UserUpdateDto;
import org.example.touragency.model.entity.User;
import org.example.touragency.service.abstractions.UserService;
import org.example.touragency.exception.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> addNewUser(@RequestBody UserAddDto userAddDto) {
        User newUser = userService.addNewUser(userAddDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(newUser));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(users));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

   @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable UUID userId,
                                                        @RequestBody UserUpdateDto userUpdateDto) {
       User updatedUser = userService.updateUser(userId, userUpdateDto);
       return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(updatedUser));
   }

}
