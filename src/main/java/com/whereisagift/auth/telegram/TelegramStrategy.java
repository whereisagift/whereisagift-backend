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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (payload.getLastName() != null) user.setLastName(payload.getLastName());
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
            if (payload.getLastName() != null) hashMap.put("last_name", payload.getLastName());
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
