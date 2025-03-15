package com.whereisagift.user;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @QueryMapping
    public User userById(@Argument Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public Iterable<User> users() {
        return userRepository.findAll();
    }

    @MutationMapping
    public User createUser(@Argument String name) {
        User user = new User(name);
        return userRepository.save(user);
    }
}
