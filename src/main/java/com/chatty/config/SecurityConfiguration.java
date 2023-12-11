package com.chatty.config;

import com.chatty.security.CustomAccessDeniedHandler;
import com.chatty.security.CustomAuthenticationEntryPoint;
import com.chatty.security.JwtAuthenticationFilter;
import com.chatty.security.JwtTokenProvider;
import com.chatty.security.MobileNumberAuthenticationFilter;
import com.chatty.security.MobileNumberProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;
    private final MobileNumberProvider mobileNumberProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(
                                "/sign-api/sign-in",
                                "/sign-api/sign-up",
                                "/sign-api/exception"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                                .requestMatchers("**exception**").permitAll()
                                .anyRequest().hasRole("ADMIN")
                )
                .exceptionHandling(authenticationManager -> authenticationManager
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), new MobileNumberAuthenticationFilter("",mobileNumberProvider).getClass());

        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(WebSecurity webSecurity) throws Exception {
        return (WebSecurityCustomizer) webSecurity.ignoring().requestMatchers("/v2/api-docs", "/swagger-resource/**","/swagger-ui.html","/webjars/**","/swagger/**","/sign-api/exception");
    }
}
