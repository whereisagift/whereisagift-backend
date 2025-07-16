package com.whereisagift.auth;

import com.whereisagift.user.User;

public interface AuthStrategy {
  User authenticate(AuthPayload payload);
}
