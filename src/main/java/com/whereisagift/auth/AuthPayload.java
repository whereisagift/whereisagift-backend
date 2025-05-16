package com.whereisagift.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthPayload {

    @JsonProperty("id")
    private Integer telegramId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("photo_url")
    private String photoUrl;

    @JsonProperty("auth_date")
    private Integer authDate;

    @JsonProperty("hash")
    private String hash;
}
