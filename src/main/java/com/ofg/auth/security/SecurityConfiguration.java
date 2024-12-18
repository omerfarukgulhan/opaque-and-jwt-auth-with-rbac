package com.ofg.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final TokenFilter tokenFilter;
    private final AuthEntryPoint authEntryPoint;

    @Value("${app.client.host}")
    private String clientHost;

    @Autowired
    public SecurityConfiguration(TokenFilter tokenFilter,
                                 AuthEntryPoint authEntryPoint) {
        this.tokenFilter = tokenFilter;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authentication) ->
                        authentication
                                .requestMatchers(HttpMethod.POST, "/user-roles/assign").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/user-roles/revoke").hasAuthority("ROLE_ADMIN")

                                .requestMatchers(HttpMethod.GET, "/roles").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/roles/{roleId}").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/roles").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/roles/{roleId}").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/roles/{roleId}").hasAuthority("ROLE_ADMIN")

                                .requestMatchers(HttpMethod.GET, "/users").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/users/{userId}").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/users").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/users/{userId}").hasAuthority("ROLE_USER")
                                .requestMatchers(HttpMethod.PUT, "/users/activate/{token}").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/users/update-password/{userId}").hasAuthority("ROLE_USER")
                                .requestMatchers(HttpMethod.PUT, "/users/reset-password").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/users/reset-password/verify/{token}").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/users/{userId}").hasAuthority("ROLE_USER")

                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/logout").hasAuthority("ROLE_USER")

                                .anyRequest().permitAll()
                )
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(authEntryPoint))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(corsConfig -> corsConfig.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList(clientHost));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setMaxAge(3600L);
                    return config;
                }));
        return http.build();
    }
}