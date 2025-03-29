package com.whereisagift.wish;

import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
import com.whereisagift.wishlist.Wishlist;
import com.whereisagift.wishlist.WishlistRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WishController {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;

    public WishController(WishRepository wishRepository, UserRepository userRepository) {
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
    }

    @QueryMapping
    public Iterable<Wish> wishes() {
        return wishRepository.findAll();
    }

    @MutationMapping
    public Wish createWish(@Argument String name) {

        Wish wish = new Wish();
        wish.setName(name);
        User user = userRepository.findById(1L).orElse(null);
        wish.setCreator(user);
        return wishRepository.save(wish);
    }

}
