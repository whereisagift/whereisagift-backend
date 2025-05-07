package com.whereisagift.auth;

import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
import graphql.GraphQLContext;
import graphql.GraphQLException;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Slf4j
@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // чтобы получить доступ к .env
    @Autowired
    private Dotenv dotenv;

    @Autowired
    private JwtEncoder jwtEncoder;

    @MutationMapping
    public AuthPayload login(
            @Argument String telegramId,
            @Argument String firstName,
            @Argument String lastName,
            @Argument String username,
            @Argument String photoUrl,
            @Argument String authDate,
            @Argument String hash,
            GraphQLContext context) {

        if (!validateTelegramHash(telegramId, firstName, lastName, username,
                photoUrl, authDate, hash)) throw new GraphQLException("Invalid Telegram hash");

//        if (System.currentTimeMillis()/1000 - Long.parseLong(authDate) > 86400) throw new GraphQLException("Auth data expired");

        User user = userRepository.findByTelegramId(Long.parseLong(telegramId)).orElseGet(() -> {
            User newUser = new User();
            newUser.setTelegramId(Long.parseLong(telegramId));
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setUsername(username);
            newUser.setPhotoUrl(photoUrl);
            return userRepository.save(newUser);
        });

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(String.valueOf(user.getId()))
                .issuer(dotenv.get("SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI"))
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

        return new AuthPayload(token, user);
    }

    private boolean validateTelegramHash(String telegramId, String firstName, String lastName,
                                         String username, String photoUrl, String authDate, String hash) {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("telegramId", telegramId);
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("username", username);
        params.put("photoUrl", photoUrl);
        params.put("authDate", authDate);

        String dataCheckString = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("\n"));

        String calculatedHash = new HmacUtils("HmacSHA256", dotenv.get("TELEGRAM_BOT_TOKEN").getBytes())
                .hmacHex(dataCheckString.getBytes());

        log.debug("hash in check hash: {}\ndataCheckString: {}", calculatedHash, dataCheckString);

        return calculatedHash.equals(hash);
    }
}
