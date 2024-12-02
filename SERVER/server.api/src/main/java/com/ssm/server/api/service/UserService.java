package com.ssm.server.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.server.api.model.User;
import com.ssm.server.api.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Long authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user.getId();
        }
        return null;
    }

    public boolean authenticateUserById(Long userId, String password) {
        java.util.Optional<User> user = userRepository.findById(userId);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    public User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? user.getId() : null;
    }

    private boolean verifyUserPassword(Long id, String password) {
        java.util.Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(user -> user.getPassword().equals(password)).orElse(false);
    }

    public java.util.Optional<User> deleteUser(Long id, String password) {
        if (verifyUserPassword(id, password)) {
            userRepository.deleteById(id);
            return java.util.Optional.empty();
        }
        return java.util.Optional.empty();
    }

    public java.util.Optional<User> editUserPassword(Long id, String password, String newPassword) {
        if (verifyUserPassword(id, password)) {
            java.util.Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setPassword(newPassword);
                userRepository.save(user);
                return java.util.Optional.of(user);
            }
        }
        return java.util.Optional.empty();
    }

    public java.util.Optional<User> editUsername(Long id, String password, String newUsername) {
        if (verifyUserPassword(id, password)) {
            java.util.Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setUsername(newUsername);
                return java.util.Optional.of(userRepository.save(user));
            }
        }
        return java.util.Optional.empty();
    }
}
