package com.spotter.backend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(exception ->
                exception
                    .authenticationEntryPoint(
                        (request, response, authException) -> {
                            response.setStatus(
                                ErrorCode.UNAUTHORIZED.getStatus().value()
                            );
                            response.setContentType(
                                MediaType.APPLICATION_JSON_VALUE
                            );
                            response.setCharacterEncoding("UTF-8");
                            objectMapper.writeValue(
                                response.getWriter(),
                                ApiResponse.onFailure(ErrorCode.UNAUTHORIZED)
                            );
                        }
                    )
                    .accessDeniedHandler(
                        (request, response, accessDeniedException) -> {
                            response.setStatus(
                                ErrorCode.FORBIDDEN.getStatus().value()
                            );
                            response.setContentType(
                                MediaType.APPLICATION_JSON_VALUE
                            );
                            response.setCharacterEncoding("UTF-8");
                            objectMapper.writeValue(
                                response.getWriter(),
                                ApiResponse.onFailure(ErrorCode.FORBIDDEN)
                            );
                        }
                    )
            )
            .authorizeHttpRequests(auth ->
                auth
                    .requestMatchers(
                        // 이 3가지 엔드포인트는 auth 없이도 허용(permitAll)
                        "/api/health",
                        "/api/users/signup",
                        "/api/users/login"
                    )
                    .permitAll()
                    .anyRequest() // 그 외 모든 엔드포인트는 auth 필요
                    .authenticated()
            )
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            )
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
