package com.whereisagift.infrastructure.security;

import com.whereisagift.infrastructure.graphql.GraphQlAuthenticationEntryPoint;
import com.whereisagift.infrastructure.jwt.JwtConverter;
import com.whereisagift.infrastructure.jwt.JwtCookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtConverter jwtConverter;
  private final JwtCookieService jwtCookieService;
  private final JwtDecoder jwtDecoder;
  private final GraphQlAuthenticationEntryPoint graphQlAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .exceptionHandling(ex -> ex.authenticationEntryPoint(graphQlAuthenticationEntryPoint))
        .oauth2ResourceServer(
            oauth ->
                oauth
                    .authenticationEntryPoint(graphQlAuthenticationEntryPoint)
                    .bearerTokenResolver(jwtCookieService)
                    .jwt(jwt -> jwt.decoder(jwtDecoder).jwtAuthenticationConverter(jwtConverter)));

    return http.build();
  }
}
