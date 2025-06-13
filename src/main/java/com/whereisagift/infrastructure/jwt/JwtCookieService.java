package com.whereisagift.infrastructure.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtCookieService implements BearerTokenResolver {

    private final String cookieName = "jwt";
    private final DefaultBearerTokenResolver defaultResolver = new DefaultBearerTokenResolver();

    @Value("${cookie.domain:}")
    private String cookieDomain;

    @Override
    public String resolve(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(cookieName)) {
                    return c.getValue();
                }
            }
        }
        return defaultResolver.resolve(request);
    }

    public void writeToken(HttpServletResponse response, String token) {
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .path("/")
                .maxAge(3600);


        if (cookieDomain.isBlank()) {
            cookieBuilder.secure(true).sameSite("Strict");
        } else {
            cookieBuilder.domain(cookieDomain);
        }

        response.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder.build().toString());
    }

    public void clearToken(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
