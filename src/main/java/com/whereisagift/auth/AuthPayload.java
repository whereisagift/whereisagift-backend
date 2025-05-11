package com.whereisagift.auth;

import com.whereisagift.user.User;
import lombok.Data;

@Data
public class AuthPayload {
    private User user;

    public AuthPayload(User user) {
        this.user = user;
    }
}
