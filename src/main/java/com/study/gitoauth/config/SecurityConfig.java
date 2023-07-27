package com.study.gitoauth.config;

import com.study.gitoauth.oauth2.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록됨
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;
    //비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf 비활성화
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        //요청에 authorize 설정
        http
                .authorizeHttpRequests(
                        request -> request
                        // /user/ 요청은 인증이 필요
                        .requestMatchers("/user/**").authenticated()
                        // /manager/ 요청은 ADMIN, MANAGER 인가 필요
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // 그 외 요청은 허용
                        .anyRequest().permitAll())
                // 로그인 방식은 form 로그인 방식을 사용할 것이고,
                // loginPage는 /login을 사용하겠다.
                .formLogin(
                        login ->login
                                .loginPage("/loginForm")
                                //login 주소가 호출되면 시큐리티가 로그인 진행
                                .loginProcessingUrl("/login")
                                //성공시 리다이렉트
                                .defaultSuccessUrl("/"))
                // oauth2 방식으로 요청이 올 경우
                .oauth2Login(
                        oauth2 -> oauth2
                                // 받아 온 userInfo를 가지고 principalOauth2UserService로 가서 후처리
                                .userInfoEndpoint(
                                        userInfo -> userInfo
                                                .userService(principalOauth2UserService)
                                ));

        return http.build();
    }
}
