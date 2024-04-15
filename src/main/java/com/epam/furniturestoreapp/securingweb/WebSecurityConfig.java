package com.epam.furniturestoreapp.securingweb;

import com.epam.furniturestoreapp.service.UserTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final UserTableService userTableService;

    @Autowired
    public WebSecurityConfig(UserTableService userTableService) {
        this.userTableService = userTableService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/css/**").permitAll();
                    auth.requestMatchers("/js/**").permitAll();
                    auth.requestMatchers("/images/**").permitAll();
                    auth.requestMatchers("/fonts/**").permitAll();
                    auth.requestMatchers("/").permitAll();
                    auth.requestMatchers("/signup").permitAll();
                    auth.requestMatchers("/not-found").permitAll();
                    auth.requestMatchers("/products/**").permitAll();
                    auth.requestMatchers("/account").hasAnyRole("USER", "ADMIN");
                    auth.requestMatchers("/cart").hasAnyRole("USER", "ADMIN");
                    auth.requestMatchers("/checkout").hasAnyRole("USER", "ADMIN");
                    auth.requestMatchers("/orders").hasAnyRole("USER", "ADMIN");
                    auth.requestMatchers("/edit-user").hasAnyRole("USER", "ADMIN");
                    auth.requestMatchers("/edit-address").hasAnyRole("USER", "ADMIN");
                    auth.anyRequest().authenticated();
                })
                .formLogin(login -> {
                    login.loginPage("/login");
                    login.permitAll();
                })
                .logout(LogoutConfigurer::permitAll)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userTableService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userTableService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}