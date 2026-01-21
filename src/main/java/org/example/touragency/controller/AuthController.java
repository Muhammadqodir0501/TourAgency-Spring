package org.example.touragency.controller;

import org.example.touragency.dto.request.LoginRequest;
import org.example.touragency.exception.ConflictException;
import org.example.touragency.exception.NotFoundException;
import org.example.touragency.security.jwt.JwtUtil;
import org.example.touragency.model.entity.User;
import org.example.touragency.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new ConflictException("Wrong password");
        }

        String token = JwtUtil.generateToken(user.getId(), user.getRole().name());

        return Map.of("token", token);
    }
}
