package com.whereisagift.user;

import com.whereisagift.wishlist.Wishlist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

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
    public Iterable<User> users() {
        return userRepository.findAll();
    }


    @MutationMapping
    public User createUser(@Argument Long telegram_id, @Argument String first_name,
                           @Argument String last_name, @Argument String telegram_username,
                           @Argument String photo_url) {
        User user = new User();
        user.setTelegram_id(telegram_id);
        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        user.setTelegram_username(telegram_username);
        user.setPhoto_url(photo_url);
        return userRepository.save(user);
    }
}
