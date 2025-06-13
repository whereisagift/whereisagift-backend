package com.whereisagift.infrastructure.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtProvider {
    @Autowired
    private JwtEncoder jwtEncoder;


    public String createToken(Long userId) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(userId.toString())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .build();

        JwsHeader headers = JwsHeader.with(() -> JwsAlgorithms.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(headers, claims))
                .getTokenValue();
    }

    public Long getAuthorizationPrincipal(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }

}
