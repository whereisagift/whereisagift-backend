package com.whereisagift.auth;

import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
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
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    @MutationMapping
    public User login(@Argument AuthPayload authPayload) {
        if (!validateTelegramHash(authPayload)) {
            throw new GraphQLException("Invalid Telegram hash");
        }

        User user = userRepository.findByTelegramId(authPayload.getTelegramId().longValue())
                .orElseGet(() -> userRepository.save(toUser(authPayload)));

        String token = createJwtForUser(user);

        writeJwtCookie(token);

        return user;
    }

    private String createJwtForUser(User user) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getId().toString())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(jwsHeader, claims))
                .getTokenValue();
    }

    private void writeJwtCookie(String token) {
        // Grab the current servlet response
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("No current request attributes");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("No current HTTP response");
        }

        // Build and set the HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)      // set false if you're testing over HTTP locally
                .path("/")
                .maxAge(3600)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
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

            return expected.equals(payload.getHash());
        } catch (Exception ex) {
            log.error("Error validating Telegram hash", ex);
            return false;
        }
    }
}
