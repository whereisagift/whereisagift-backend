package com.whereisagift.auth.local;

import com.whereisagift.auth.AuthPayload;
import com.whereisagift.auth.AuthStrategy;
import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
import graphql.GraphQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
@RequiredArgsConstructor
public class LocalStrategy implements AuthStrategy {
  private final UserRepository userRepository;

  @Value("${local.user.id:}")
  private String localUserId;

  @Override
  public User authenticate(AuthPayload payload) {
    long uid;
    try {
      uid = Long.parseLong(localUserId);
    } catch (NumberFormatException ex) {
      throw new GraphQLException("Invalid local.user.id: " + localUserId);
    }

    return userRepository
        .findById(uid)
        .orElseThrow(() -> new GraphQLException("User not found: " + uid));
  }
}
