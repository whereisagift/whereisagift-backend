package com.whereisagift.user;

import graphql.GraphQLException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new GraphQLException("User not found"));

    }

}
