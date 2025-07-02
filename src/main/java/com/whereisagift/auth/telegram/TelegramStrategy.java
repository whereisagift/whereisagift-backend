package com.whereisagift.auth.telegram;

import com.whereisagift.auth.AuthPayload;
import com.whereisagift.auth.AuthStrategy;
import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
import graphql.GraphQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Profile("!local")
@Component
@RequiredArgsConstructor
public class TelegramStrategy implements AuthStrategy {

    private final UserRepository userRepository;

    @Value("${telegram.bot.token:}")
    private String telegramBotToken;


    @Override
    public User authenticate(AuthPayload payload) {
        if (!validateTelegramHash(payload)) {
            throw new GraphQLException("Invalid Telegram hash");
        }
        long tgId = payload.getTelegramId();
        return userRepository.findByTelegramId(tgId)
                .orElseGet(() -> userRepository.save(toUser(payload)));
    }

    private User toUser(AuthPayload payload) {
        User user = new User();
        user.setTelegramId(payload.getTelegramId().longValue());
        user.setFirstName(payload.getFirstName());
        Optional.ofNullable(payload.getLastName())
                .ifPresent(user::setLastName);
        user.setUsername(payload.getUsername());
        Optional.ofNullable(payload.getPhotoUrl())
                .ifPresent(user::setPhotoUrl);
        user.setUsername(payload.getUsername());
        user.setAuthDate(payload.getAuthDate().longValue());
        return user;
    }


    private boolean validateTelegramHash(AuthPayload payload) {
        try {
            String dataCheck = Stream.of(
                            Map.entry("auth_date", payload.getAuthDate().toString()),
                            Map.entry("first_name", payload.getFirstName()),
                            Map.entry("id", payload.getTelegramId().toString()),
                            Map.entry("last_name", payload.getLastName()),
                            Map.entry("photo_url", payload.getPhotoUrl()),
                            Map.entry("username", payload.getUsername()))
                    .filter(e -> e.getValue() != null)
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> e.getKey() + "=" + e.getValue())
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
