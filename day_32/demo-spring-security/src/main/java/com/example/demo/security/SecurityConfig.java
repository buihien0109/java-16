package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] PUBLIC_ROUTE = {"/", "/123", "456", "/css/**", "/js/**", "/image/**"};
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(PUBLIC_ROUTE).permitAll()
                        .requestMatchers("POST", "/cc").hasRole("ADMIN")
                        .requestMatchers("/profile").hasAnyRole("ADMIN", "AUTHOR")
                        .requestMatchers("/admin", "/abc", "/xyz").hasRole("ADMIN")
                        .requestMatchers("/author", "/bb/**").hasRole("AUTHOR")
                        .requestMatchers("/cc").hasAuthority("ROLE_SALE")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("111")
                .roles("USER", "ADMIN")
                .build();

        UserDetails author = User.withDefaultPasswordEncoder()
                .username("author")
                .password("111")
                .roles("USER", "AUTHOR")
                .build();
        return new InMemoryUserDetailsManager(admin, author);
    }

}
