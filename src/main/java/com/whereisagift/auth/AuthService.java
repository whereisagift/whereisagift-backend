package com.whereisagift.auth;

import com.whereisagift.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthStrategy strategy;    // единственный бин в контексте

    public User login(AuthPayload payload) {
        return strategy.authenticate(payload);
    }
}