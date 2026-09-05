package com.mander.interpreterpattern;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/publico").permitAll()
                        .requestMatchers("/usuario").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin").access(
                new WebExpressionAuthorizationManager("hasRole('ADMIN')"))
                .anyRequest().authenticated()
            ).httpBasic(org.springframework.security.config.Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()
        );
        return http.build();
    }

    @Bean
    InMemoryUserDetailsManager users() {
        UserDetails ana = User.withDefaultPasswordEncoder()
                .username("ana").password("1234").roles("USER").build();
        UserDetails root = User.withDefaultPasswordEncoder()
                .username("root").password("1234").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(ana, root);
    }

}
