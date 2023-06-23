package com.example.demo.controller;

import com.example.demo.request.LoginRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("login-handle")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        // Tạo đối tượng xác thực
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        try {
            // Tiến hành xác thực
            Authentication authentication = authenticationManager.authenticate(token);

            // Lưu vào Context Holder
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Lưu vào trong session
            session.setAttribute("MY_SESSION", authentication.getName()); // Lưu email -> session

            return ResponseEntity.ok("Login success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login fail");
        }
    }
}
// Client -> Server -> Tạo session -> Sesson_id lưu vào trong cookie -> Client
