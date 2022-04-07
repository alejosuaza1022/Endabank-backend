package com.endava.endabank.dao;

import com.endava.endabank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByIdentifier(String identifier);
}
