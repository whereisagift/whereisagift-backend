package com.whereisagift.auth;

import com.whereisagift.user.User;
import lombok.Data;

@Data
public class AuthPayload {

    private String telegramId;
    private String firstName;
    private String lastName;
    private String username;
    private String photoUrl;
    private String authDate;
    private String hash;

}
