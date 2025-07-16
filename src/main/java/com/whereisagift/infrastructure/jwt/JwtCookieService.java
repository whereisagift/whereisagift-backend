package com.whereisagift.infrastructure.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtCookieService implements BearerTokenResolver {
  private static final String COOKIE_NAME = "jwt";
  @Autowired JwtProperties jwtProperties;

  @Value("${cookie.domain:}")
  private String cookieDomain;

  @Override
  public String resolve(HttpServletRequest request) {
    return Optional.ofNullable(request.getCookies())
        .flatMap(
            cookies ->
                Arrays.stream(cookies)
                    .filter(c -> COOKIE_NAME.equals(c.getName()))
                    .map(Cookie::getValue)
                    .map(String::strip)
                    .filter(t -> !t.isEmpty())
                    .findFirst())
        .orElse(null);
  }

  public void writeToken(HttpServletResponse response, String token) {
    response.addHeader(
        HttpHeaders.SET_COOKIE, buildCookie(token, jwtProperties.getTtl()).toString());
  }

  public void clearToken(HttpServletResponse response) {
    response.addHeader(HttpHeaders.SET_COOKIE, buildCookie("", Duration.ZERO).toString());
  }

  private ResponseCookie buildCookie(String value, Duration maxAge) {
    ResponseCookie.ResponseCookieBuilder builder =
        ResponseCookie.from(COOKIE_NAME, value)
            .httpOnly(true)
            .path("/")
            .maxAge(maxAge)
            .secure(cookieDomain.isBlank())
            .sameSite(cookieDomain.isBlank() ? "Strict" : "Lax");

    if (!cookieDomain.isBlank()) {
      builder.domain(cookieDomain);
    }

    return builder.build();
  }
}
