package com.example.demo.controller;

import com.example.demo.model.TokenConfirm;
import com.example.demo.model.User;
import com.example.demo.repository.TokenConfirmRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.LoginRequest;
import com.example.demo.service.MailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenConfirmRepository tokenConfirmRepository;

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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        // Kiểm tra email gửi lên có tồn tại trong db hay không
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new RuntimeException("Not found user");
                });

        // Tạo ra token -> lưu vào cơ sở dữ liệu
        // Token là chuỗi UUID
        TokenConfirm tokenConfirm = TokenConfirm.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(30))
                .user(user)
                .build();
        tokenConfirmRepository.save(tokenConfirm);
        // Send email chưa token
        // Link : http://localhost:8080/doi-mat-khau/hakdiwowjkdkdkdjfffki
        mailService.sendMail(
                user.getEmail(),
                "Quên mật khẩu (Bùi Hiên)",
                "Link :" + "http://localhost:8080/doi-mat-khau/" + tokenConfirm.getToken()
        );
        return ResponseEntity.ok("Send mail success");
    }
}
// Client -> Server -> Tạo session -> Sesson_id lưu vào trong cookie -> Client
