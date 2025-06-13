package com.whereisagift.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @QueryMapping
    public User user(@Argument Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public User me(@AuthenticationPrincipal Long userId) {
        return userRepository.getReferenceById(userId);
    }
}
