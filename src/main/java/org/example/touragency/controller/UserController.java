package org.example.touragency.controller;

import lombok.RequiredArgsConstructor;
import org.example.touragency.dto.request.UserAddDto;
import org.example.touragency.dto.response.UserUpdateDto;
import org.example.touragency.model.enity.User;
import org.example.touragency.service.abstractions.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addNewUser(@RequestBody UserAddDto userAddDto) {
        try{
            userService.addNewUser(userAddDto);
            return ResponseEntity.ok("User has successfully been added");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserById(@RequestParam UUID id) {
        try{
            userService.deleteUser(id);
            return ResponseEntity.ok("User has successfully been deleted");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

   @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDto userUpdateDto) {
        try{
            userService.updateUser(userUpdateDto);
            return ResponseEntity.ok("User has successfully been Updated");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
