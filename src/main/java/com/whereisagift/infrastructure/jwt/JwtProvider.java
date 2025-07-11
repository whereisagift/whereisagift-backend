package com.whereisagift.infrastructure.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtProvider {
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    private JwtEncoder jwtEncoder;

    public String createToken(Long userId) {
        Instant now = Instant.now();
        Instant exp = now.plus(jwtProperties.getTtl());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiresAt(exp)
                .build();

        JwsHeader headers = JwsHeader.with(() -> JwsAlgorithms.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(headers, claims))
                .getTokenValue();
    }

    public Long getAuthorizationPrincipal(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }

}
