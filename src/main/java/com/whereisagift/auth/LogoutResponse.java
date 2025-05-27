package com.whereisagift.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogoutResponse {

    private boolean success;
    private String message;

}
