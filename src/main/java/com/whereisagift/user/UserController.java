package com.whereisagift.user;

import graphql.GraphQLException;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @QueryMapping
    public User user(@Argument Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public User me(@AuthenticationPrincipal(expression = "subject") String userId) {
        Long id = Long.valueOf(userId);
        return userRepository.findById(id)
                .orElseThrow(() -> new GraphQLException("User not found"));
    }

    @QueryMapping
    public Iterable<User> users() {
        return userRepository.findAll();
    }
}
