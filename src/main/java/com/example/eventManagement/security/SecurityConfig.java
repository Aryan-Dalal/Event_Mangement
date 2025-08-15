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

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
                "SELECT email, password, true as enabled FROM users WHERE email = ?"
        );
        manager.setAuthoritiesByUsernameQuery(
                "SELECT u.email, a.authority " +
                        "FROM authorities a " +
                        "INNER JOIN users u ON a.user_id = u.user_id " +
                        "WHERE u.email = ?"
        );
        return manager;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/images/**").permitAll() // public
                        .requestMatchers("/admin/**").hasRole("ADMIN") // admin only
                        .requestMatchers("/dashboard","/").authenticated() // must login to see dashboard
                        .anyRequest().authenticated() // everything else also needs login
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(roleBasedRedirect()) // role-based redirection
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }


    // Role-based redirection
    @Bean
    public AuthenticationSuccessHandler roleBasedRedirect() {
        return (request, response, authentication) -> {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            if (roles.contains("ROLE_ADMIN")) {
                response.sendRedirect("/admin"); // admin dashboard
            } else {
                response.sendRedirect("/dashboard"); // user homepage
            }
        };
    }
}
