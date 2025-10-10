// /backend/src/main/java/com/service_booking_system/service/config/SecurityConfig.java

package com.service_booking_system.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/register","/login","/verify-otp","/resend-otp","/change-password/**","/services/**","/subservices/**","/orders/**","/bills/**","/api/payments/**","/feedback/**","/cancel/**","/customer/profile/**")
                                .permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint(authenticationEntryPoint)
                )

                .formLogin(AbstractHttpConfigurer::disable)   // disable default login form
                .httpBasic(AbstractHttpConfigurer::disable);
//        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

