package com.example.jparelationship.controller;

import com.example.jparelationship.service.FileServerService;
import com.example.jparelationship.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class WebController {
    private final UserService userService;
    private final FileServerService fileServerService;

    @GetMapping("/")
    public String getUserPage(Model model) {
        model.addAttribute("users", userService.getAllUser());
        return "index";
    }

    @GetMapping("/users/{id}/files")
    public String getFilesPage(Model model, @PathVariable Integer id) {
        model.addAttribute("files", fileServerService.getFilesOfUser(id));
        model.addAttribute("userId", id);
        return "file";
    }
}
