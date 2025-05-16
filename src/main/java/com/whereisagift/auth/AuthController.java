// src/main/java/com/whereisagift/auth/AuthController.java
package com.whereisagift.auth;

import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
import graphql.GraphQLContext;
import graphql.GraphQLException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtEncoder jwtEncoder;

    @Value("${telegram.bot.token}")
    private String telegramBotToken;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer.uri}")
    private String issuerUri;

    @MutationMapping
    public User login(@Argument AuthPayload authPayload, GraphQLContext context) {
        if (!validateTelegramHash(authPayload)) {
            throw new GraphQLException("Invalid Telegram hash");
        }

        User user = userRepository.findByTelegramId(authPayload.getTelegramId().longValue())
                .orElseGet(() -> userRepository.save(toUser(authPayload)));

        String token = createJwtForUser(user);
        setCookie(context, token);

        return user;
    }

    private String createJwtForUser(User user) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(user.getId()))
                .issuer(issuerUri)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private void setCookie(GraphQLContext ctx, String token) {
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(3600)
                .sameSite("Strict")
                .build();
        HttpServletResponse res = ctx.get(HttpServletResponse.class);
        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private User toUser(AuthPayload payload) {
        User user = new User();
        user.setTelegramId(payload.getTelegramId().longValue());
        user.setFirstName(payload.getFirstName());
        user.setLastName(payload.getLastName());
        user.setUsername(payload.getUsername());
        user.setPhotoUrl(payload.getPhotoUrl());
        user.setAuthDate(payload.getAuthDate().longValue());
        return user;
    }

    private boolean validateTelegramHash(AuthPayload payload) {
        try {
            Map<String, String> hashMap = new LinkedHashMap<>();
            hashMap.put("auth_date", payload.getAuthDate().toString());
            hashMap.put("first_name", payload.getFirstName());
            hashMap.put("id", payload.getTelegramId().toString());
            hashMap.put("last_name", payload.getLastName());
            hashMap.put("photo_url", payload.getPhotoUrl());
            hashMap.put("username", payload.getUsername());

            String dataCheck = hashMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("\n"));

            byte[] key = MessageDigest.getInstance("SHA-256")
                    .digest(telegramBotToken.getBytes(StandardCharsets.UTF_8));

            String expected = new HmacUtils("HmacSHA256", key)
                    .hmacHex(dataCheck.getBytes(StandardCharsets.UTF_8));

            log.debug("dataCheck:\n{}\nexpectedHash: {}\nprovidedHash: {}",
                    dataCheck, expected, payload.getHash());

            return expected.equals(payload.getHash());
        } catch (Exception ex) {
            log.error("Error validating Telegram hash", ex);
            return false;
        }
    }
}
