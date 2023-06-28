package com.example.demo.controller;

import com.example.demo.model.TokenConfirm;
import com.example.demo.repository.TokenConfirmRepository;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private MailService mailService;
    @Autowired
    private TokenConfirmRepository tokenConfirmRepository;

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

    @GetMapping("/doi-mat-khau/{token}")
    public String getUpdatePasswordPage(@PathVariable String token, Model model) {
        // Kiểm tra token có hợp lệ hay không
        Optional<TokenConfirm> optionalTokenConfirm = tokenConfirmRepository.findByToken(token);
        if(optionalTokenConfirm.isEmpty()) {
            model.addAttribute("isValid", false);
            model.addAttribute("message", "Token không hợp lệ");
            return "update-password";
        }
        // Kiểm tra token đã được kích hoạt hay chưa
        TokenConfirm tokenConfirm = optionalTokenConfirm.get();
        if(tokenConfirm.getConfirmedAt() != null) {
            model.addAttribute("isValid", false);
            model.addAttribute("message", "Token đã được kích hoạt");
            return "update-password";
        }

        // Kiểm tra token đã hết hạn hay chưa
        if(tokenConfirm.getExpiredAt().isBefore(LocalDateTime.now())) {
            model.addAttribute("isValid", false);
            model.addAttribute("message", "Token đã hết hạn");
            return "update-password";
        }

        tokenConfirm.setConfirmedAt(LocalDateTime.now());
        tokenConfirmRepository.save(tokenConfirm);
        model.addAttribute("isValid", true);
        return "update-password";
    }
}
