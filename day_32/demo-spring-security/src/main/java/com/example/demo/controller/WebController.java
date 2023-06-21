package com.example.demo.controller;

import com.example.demo.security.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class WebController {
    @Autowired
    private IAuthenticationFacade authenticationFacade;

    // Ai cũng có thể vào được
    @GetMapping("/")
    public String getHome() {
        return "Home page";
    }

    // Có role admin hoặc author mới có thể vào
    @GetMapping("/profile")
    public String getProfile() {
        return "Profile page";
    }

    // Phải có role admin mới vào được
    @GetMapping("/admin")
    public String getAdmin() {
        return "Admin page";
    }

    // Phải có role author mới vào được
    @GetMapping("/author")
    public String getAuthor() {
        return "Author page";
    }

    @GetMapping("/get-info")
    public Object getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails;
        }
        return null;
    }

    @GetMapping("/get-info-1")
    public Object getInfo1(Principal principal) {
        return principal;
    }

    @GetMapping("/get-info-2")
    public Object getInfo2(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails;
    }

    @GetMapping("/get-info-3")
    public Object getInfo3() {
        UserDetails userDetails = (UserDetails) authenticationFacade.getAuthentication().getPrincipal();
        return userDetails;
    }
}
