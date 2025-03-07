package com.whereisagift.user;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserQueryResolver implements GraphQLQueryResolver {

    @Autowired
    UserRepository userRepository;

    public User userById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> users() {
        return userRepository.findAll();
    }

}
