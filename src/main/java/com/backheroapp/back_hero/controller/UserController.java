package com.backheroapp.back_hero.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backheroapp.back_hero.model.User;
import com.backheroapp.back_hero.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername()) || userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User user) {
        Optional<User> existingUser = userService.findUserById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Optionally update only the fields that are provided in the request
        User updatedUser = existingUser.get();
        if (user.getUsername() != null)
            updatedUser.setUsername(user.getUsername());
        if (user.getEmail() != null)
            updatedUser.setEmail(user.getEmail());
        if (user.getPassword() != null)
            updatedUser.setPassword(user.getPassword());
        if (user.getRole() != null)
            updatedUser.setRole(user.getRole());
        if (user.getAccountStatus() != null)
            updatedUser.setAccountStatus(user.getAccountStatus());

        User savedUser = userService.saveUser(updatedUser);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
