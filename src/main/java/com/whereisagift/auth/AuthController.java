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
import org.springframework.security.oauth2.jwt.Jwt;
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
    public User login(
            @Argument AuthPayload authPayload,
            GraphQLContext context) {

        if (!validateTelegramHash(authPayload)) throw new GraphQLException("Invalid Telegram hash");

//        if (System.currentTimeMillis()/1000 - Long.parseLong(authDate) > 86400) throw new GraphQLException("Auth data expired");

        User user = userRepository.findByTelegramId(authPayload.getTelegramId().longValue()).orElseGet(() -> {
            User newUser = new User();
            newUser.setTelegramId(authPayload.getTelegramId().longValue());
            newUser.setFirstName(authPayload.getFirstName());
            newUser.setLastName(authPayload.getLastName());
            newUser.setUsername(authPayload.getUsername());
            newUser.setPhotoUrl(authPayload.getPhotoUrl());
            return userRepository.save(newUser);
        });

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(String.valueOf(user.getId()))
                .issuer(issuerUri)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .build();

        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet));

        String token = jwt.getTokenValue();

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(3600)
                .sameSite("Strict")
                .build();

        HttpServletResponse response = context.get(HttpServletResponse.class);

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return user;
    }

    private boolean validateTelegramHash(AuthPayload authPayload) {
        try {
            Map<String, String> params = new LinkedHashMap<>();
            params.put("auth_date", authPayload.getAuthDate().toString());
            params.put("first_name", authPayload.getFirstName());
            params.put("id", authPayload.getTelegramId().toString());
            params.put("last_name", authPayload.getLastName());
            params.put("photo_url", authPayload.getPhotoUrl());
            params.put("username", authPayload.getUsername());

            String dataCheckString = params.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("\n"));

            byte[] key = MessageDigest.getInstance("SHA-256")
                    .digest(telegramBotToken.getBytes(StandardCharsets.UTF_8));

            String calculatedHash = new HmacUtils("HmacSHA256", key)
                    .hmacHex(dataCheckString.getBytes(StandardCharsets.UTF_8));

            log.debug("dataCheckString:\n{}", dataCheckString);
            log.debug("expectedHash: {}", calculatedHash);
            log.debug("providedHash: {}", authPayload.getHash());

            return calculatedHash.equals(authPayload.getHash());
        } catch (Exception e) {
            log.error("Error validating Telegram hash", e);
            return false;
        }
    }


}
