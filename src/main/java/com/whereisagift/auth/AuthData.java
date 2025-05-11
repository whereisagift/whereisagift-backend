package com.whereisagift.auth;

import lombok.Data;

@Data
public class AuthData {
    private String telegramId;
    private String firstName;
    private String lastName;
    private String username;
    private String photoUrl;
    private String authDate;
    private String hash;
}
