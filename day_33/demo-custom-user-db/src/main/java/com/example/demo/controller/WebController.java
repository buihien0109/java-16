package com.example.demo.controller;

import com.example.demo.security.IAuthenticationFacade;
import com.example.demo.security.IsAdmin;
import com.example.demo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
public class WebController {

    @Autowired
    private MailService mailService;

    // Ai cũng có thể vào được
    @GetMapping("/")
    public String getHome() {
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage(Authentication authentication) {
        if(authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/forgot-password")
    public String getForgotPasswordPage() {
        return "forgot-password";
    }

    // Có role admin hoặc author mới có thể vào
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AUTHOR')")
    @GetMapping("/profile")
    public String getProfile() {
        return "profile";
    }

    // Phải có role admin mới vào được
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @IsAdmin
    @GetMapping("/admin")
    public String getAdmin() {
        return "admin";
    }

    // Phải có role author mới vào được
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @GetMapping("/author")
    public String getAuthor() {
        return "author";
    }

    // Gửi -> Gửi email xác nhận quên mật khẩu
    // Trong email có 1 link để xác thực
    @GetMapping("/api/send-email")
    public ResponseEntity<?> testSendMail() {
        mailService.sendMail(
                "abc@gmail.com",
                "Mời cưới",
                "Trân trọng kính mời bạn đến dự đám cưới của chúng tôi"
        );
        return ResponseEntity.ok("Send mail success");
    }
}
