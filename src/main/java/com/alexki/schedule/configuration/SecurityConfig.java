package com.alexki.schedule.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain secureFilter(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/**").permitAll()
                                .requestMatchers("/schedule/delete/**").hasAuthority("ADMIN")
                                .anyRequest().permitAll()
                )
                .formLogin(
                        form ->
                                form
                                        .loginPage("/login")
                                        .defaultSuccessUrl("/schedule", true)
                                        .failureUrl("/login?error")
                                        .permitAll())
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout")
                                .permitAll()
                );

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
