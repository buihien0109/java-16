package com.example.jparelationship.repository;

import com.example.jparelationship.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}