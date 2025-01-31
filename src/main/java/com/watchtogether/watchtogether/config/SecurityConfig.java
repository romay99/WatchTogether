package com.watchtogether.watchtogether.config;

import com.watchtogether.watchtogether.jwt.JwtAuthFilter;
import com.watchtogether.watchtogether.jwt.JwtProvider;
import com.watchtogether.watchtogether.member.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtProvider jwtProvider;
  private final CustomUserDetailsService customUserDetailsService;

  /*
   * 패스워드 인코더 Bean 생성
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /*
   * 시큐리티 설정
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf((config) -> config.disable())
        .sessionManagement(
            //JWT 사용을 위한 세션정책 STATELESS 설정 , formLogin off
            (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin((form) -> form.disable());

    // 인증 작업 하지 않을 경로 설정
    http
        .authorizeHttpRequests((request) ->
            request.requestMatchers(
                    "/","/swagger-ui/**", "/v3/api-docs/**","/v3/api-docs").permitAll()
                .requestMatchers(
                    "/member/join", "/movie/list/screen", "/movie/detail/**",
                    "/member/join", "/member/login", "/cinema/info","/movie/list/title").permitAll()

                .anyRequest().authenticated());

    // JWT 필터 추가
    http
        .addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtProvider),
            UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}