package com.watchtogether.watchtogether.config;

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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

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
                    "/member/join", "/movie/list").permitAll()
                .anyRequest().authenticated());

    return http.build();
  }
}