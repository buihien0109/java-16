package com.example.blog.dto;

import lombok.*;
import com.example.blog.entity.Role;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private List<Role> roles;
}
