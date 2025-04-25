package com.whereisagift.auth;

import com.whereisagift.user.User;
import lombok.Data;

@Data
public class AuthPayload {
    private String token;
    private User user;

    public AuthPayload(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
