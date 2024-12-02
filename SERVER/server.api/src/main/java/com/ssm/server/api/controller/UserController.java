package com.ssm.server.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssm.server.api.model.User;
import com.ssm.server.api.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody User loginUser) {
        if(com.ssm.server.api.ServerApiApplication.debug()){System.out.println("Hit Login");}
        Long userId = userService.authenticateUser(loginUser.getUsername(), loginUser.getPassword());
        return userId != null ? ResponseEntity.ok(userId) : ResponseEntity.status(401).build();
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        if (com.ssm.server.api.ServerApiApplication.debug()) { System.out.println("Hit CreateUser"); }
        User createdUser = userService.createUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/lookup")
    public ResponseEntity<Long> userLookup(@RequestBody UserLookupRequest request) {
        if (com.ssm.server.api.ServerApiApplication.debug()) { System.out.println("Hit UserLookup"); }
        Long userId = userService.getUserIdByUsername(request.getUsername());
        return userId != null ? ResponseEntity.ok(userId) : ResponseEntity.status(404).build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody DeleteUserRequest request) {
        if(com.ssm.server.api.ServerApiApplication.debug()){System.out.println("Hit Delete");}
        Optional<User> deletedUser = userService.deleteUser(request.getId(), request.getPassword());
        return deletedUser.isPresent() ? ResponseEntity.noContent().build() : ResponseEntity.status(403).build();
    }

    @PostMapping("/editPassword")
    public ResponseEntity<User> editUserPassword(@RequestBody PasswordUpdateRequest request) {
        if(com.ssm.server.api.ServerApiApplication.debug()){System.out.println("Hit EditPassword");}
        Optional<User> updatedUser = userService.editUserPassword(request.getId(), request.getPassword(), request.getNewPassword());
        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(403).build());
    }

    @PostMapping("/editUsername")
    public ResponseEntity<User> editUsername(@RequestBody UsernameUpdateRequest request) {
        if(com.ssm.server.api.ServerApiApplication.debug()){System.out.println("Hit EditUser");}
        Optional<User> updatedUser = userService.editUsername(request.getId(), request.getPassword(), request.getNewUsername());
        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(403).build());
    }

    public static class CreateUserRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class UserLookupRequest {
        private String username;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }

    public static class DeleteUserRequest {
        private Long id;
        private String password;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class PasswordUpdateRequest {
        private Long id;
        private String password;
        private String newPassword;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    public static class UsernameUpdateRequest {
        private Long id;
        private String password;
        private String newUsername;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getNewUsername() { return newUsername; }
        public void setNewUsername(String newUsername) { this.newUsername = newUsername; }
    }
}
