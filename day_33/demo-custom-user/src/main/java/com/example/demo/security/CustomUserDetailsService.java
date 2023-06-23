package com.example.demo.security;

import com.example.demo.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

    // Tạo 1 ds user
    private List<User> userList = List.of(
            new User(1, "An", "a@gmail.com", encode.encode("111"), List.of("USER")),
            new User(1, "Binh", "b@gmail.com", encode.encode("111"), List.of("USER", "ADMIN")),
            new User(1, "Huy", "c@gmail.com", encode.encode("111"), List.of("USER", "AUTHOR"))
    );

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userList.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Not found user"));
    }
}
