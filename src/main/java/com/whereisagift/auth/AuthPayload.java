package com.whereisagift.auth;

import lombok.Getter;


public class AuthPayload {
    private Integer id;
    private String first_name;
    private String last_name;
    private String photo_url;
    private Integer auth_date;

    @Getter
    private String hash;
    @Getter
    private String username;

    public Integer getTelegramId() {
        return id;
    }

    public Integer getAuthDate() {
        return auth_date;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getPhotoUrl() {
        return photo_url;
    }
}
