package com.example.eventManagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;
import java.util.Set;

@Configuration
public class SecurityConfig {

    // Configure user details service to read from the updated authorities table
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        // Query to get users
        manager.setUsersByUsernameQuery(
                "SELECT email, password, true as enabled FROM users WHERE email = ?"
        );

        // Query to get authorities/roles
        manager.setAuthoritiesByUsernameQuery(
                "SELECT u.email, a.authority " +
                        "FROM authorities a " +
                        "INNER JOIN users u ON a.user_id = u.user_id " +
                        "WHERE u.email = ?"
        );

        return manager;
    }

    // Password encoder bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security filter chain configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public routes
                        .requestMatchers("/login", "/register", "/css/**", "/images/**").permitAll()
                        // Admin-only routes
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Authenticated users
                        .requestMatchers("/dashboard", "/").authenticated()
                        // All others require authentication
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(roleBasedRedirect()) // Role-based redirect
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }

    // Role-based redirection after login
    @Bean
    public AuthenticationSuccessHandler roleBasedRedirect() {
        return (request, response, authentication) -> {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            if (roles.contains("ROLE_ADMIN")) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/dashboard");
            }
        };
    }
}
