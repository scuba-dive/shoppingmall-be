package io.groom.scubadive.shoppingmall.global.securirty;

import io.groom.scubadive.shoppingmall.member.domain.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화 (JWT 인증에 불필요)
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 제거
                .httpBasic(AbstractHttpConfigurer::disable) // Basic 인증 제거
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 저장 안 함
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/signup",
                                "/api/users/login",
                                "/api/products/**",
                                "/api/categories",
                                "/ping",
                                "/h2-console/**"
                        ).permitAll() // 인증 없이 허용할 경로들

                        .requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name()) // 어드민 롤만 접근 가능.
                        .requestMatchers("/api/users/me").hasAuthority("USER")
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .headers().frameOptions().disable();



        return http.build();
    }
}
