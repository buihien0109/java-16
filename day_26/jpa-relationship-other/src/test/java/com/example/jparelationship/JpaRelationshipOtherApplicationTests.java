package com.example.jparelationship;

import com.example.jparelationship.entity.Customer;
import com.example.jparelationship.entity.Order;
import com.example.jparelationship.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class JpaRelationshipOtherApplicationTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Transactional
    void demo_removal() {

    }
}
