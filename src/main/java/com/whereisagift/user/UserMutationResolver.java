package com.whereisagift.user;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMutationResolver implements GraphQLMutationResolver {

    @Autowired
    private UserRepository userRepository;

    public User addUser(String username, String password) {
        User user = new User(username, password);
        return userRepository.save(user);
    }

}
