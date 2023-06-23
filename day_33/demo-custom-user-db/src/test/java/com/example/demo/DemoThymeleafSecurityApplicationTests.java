package com.example.demo;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
class DemoThymeleafSecurityApplicationTests {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Test
    void save_role() {
        List<Role> roles = List.of(
                new Role(null, "USER"),
                new Role(null, "ADMIN"),
                new Role(null, "AUTHOR")
        );
        roleRepository.saveAll(roles);
    }

    @Test
    void save_user() {
        Role useRole = roleRepository.findByName("USER").orElse(null);
        Role adminRole = roleRepository.findByName("ADMIN").orElse(null);
        Role authorRole = roleRepository.findByName("AUTHOR").orElse(null);

        List<User> users = List.of(
                new User(null, "Nguyen Van A", "a@gmail.com", encoder.encode("111"), List.of(useRole)),
                new User(null, "Tran Van B", "b@gmail.com", encoder.encode("111"), List.of(useRole, adminRole)),
                new User(null, "Ngo Thi C", "c@gmail.com", encoder.encode("111"), List.of(useRole, authorRole))
        );
        userRepository.saveAll(users);
    }
}
