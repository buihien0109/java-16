package com.example.jparelationship.repository;

import com.example.jparelationship.entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByName(String name, Pageable pageable);

    @Query("select c from Customer c where upper(c.name) like upper(concat('%', ?1, '%'))")
    List<Customer> findByNameContainsIgnoreCase(String name);
}