package io.groom.scubadive.shoppingmall.global.securirty;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF 비활성화 (테스트용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/signup",
                                "/ping",
                                "/h2-console/**"
                        ).permitAll() // 인증 없이 허용할 경로들
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .headers().frameOptions().disable() // H2 콘솔 보이도록
                .and()
                .formLogin().disable(); // 기본 로그인 페이지 비활성화

        return http.build();
    }
}
