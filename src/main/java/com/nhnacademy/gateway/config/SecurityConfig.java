package com.nhnacademy.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        // URL 별 접근 권한 설정
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers("/signup", "/login/**", "/css/**", "/js/**", "/assets/**").permitAll() // 모든 사용자 허용
                        .anyRequest().authenticated()
        );

        // CSRF 비활성화 (활성화 상태 -> form 전송 시 hidden input 필요) -> TODO 활성화시키기
        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(formLogin ->
                formLogin.loginPage("/login")
                        .usernameParameter("id")
                        .passwordParameter("pwd")
                        .loginProcessingUrl("/login/process")
                        .permitAll()
        );

//        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
//                httpSecurityExceptionHandlingConfigurer.accessDeniedPage("/error") // 권한 없음
//        );

        return http.build();
    }
}
