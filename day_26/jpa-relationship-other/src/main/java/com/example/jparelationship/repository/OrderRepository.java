package com.example.jparelationship.repository;

import com.example.jparelationship.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}