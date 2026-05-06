/*
 * Proyecto: GreenHouse Manager
 * Archivo: SecurityConfig.java
 * Descripcion: Configuracion de seguridad con OAuth2 y JWT.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration for JWT-based stateless sessions.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    /**
     * Creates a new SecurityConfig.
     *
     * @param jwtAuthenticationFilter jwt filter
     * @param restAuthenticationEntryPoint authentication entry point
     * @param customOAuth2UserService oauth2 user service
     * @param oAuth2AuthenticationSuccessHandler oauth2 success handler
     */
    public SecurityConfig(
        JwtAuthenticationFilter jwtAuthenticationFilter,
        RestAuthenticationEntryPoint restAuthenticationEntryPoint,
        CustomOAuth2UserService customOAuth2UserService,
        OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
    }

    /**
     * Defines the security filter chain.
     *
     * @param http http security builder
     * @return security filter chain
     * @throws Exception on configuration errors
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(Customizer.withDefaults());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(ex -> ex.authenticationEntryPoint(restAuthenticationEntryPoint));
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api/auth/**",
                "/oauth2/**",
                "/login/oauth2/**",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/error"
            ).permitAll()
            .anyRequest().authenticated()
        );
        http.oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
            .successHandler(oAuth2AuthenticationSuccessHandler)
        );
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
