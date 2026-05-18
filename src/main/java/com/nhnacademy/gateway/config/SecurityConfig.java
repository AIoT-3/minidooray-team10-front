package com.nhnacademy.gateway.config;

import com.nhnacademy.gateway.auth.handler.CustomLogoutHandler;
import com.nhnacademy.gateway.auth.handler.LoginFailureHandler;
import com.nhnacademy.gateway.auth.handler.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginSuccessHandler successHandler;
    private final LoginFailureHandler failureHandler;
    private final CustomLogoutHandler logoutHandler;

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
                        .loginProcessingUrl("/login/process")
                        .usernameParameter("email")
                        .passwordParameter("pwd")
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll()
        );

        http.logout(logout ->
                logout.logoutUrl("/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessUrl("/login")
        );

//        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
//                httpSecurityExceptionHandlingConfigurer.accessDeniedPage("/error") // 권한 없음
//        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
