package com.ssm.server.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ssm.server.api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
