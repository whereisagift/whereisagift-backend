package com.whereisagift.wishlist;

import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
public class WishlistController {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;

    public WishlistController(WishlistRepository wishlistRepository, UserRepository userRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
    }

    @QueryMapping
    public Wishlist wishlist(@Argument Long id) {
        return wishlistRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public Iterable<Wishlist> wishlists() {
        return wishlistRepository.findAll();
    }

    @MutationMapping
    public Wishlist createWishlist(@Argument String name) {
        Wishlist wishlist = new Wishlist();
        wishlist.setName(name);
        User user = userRepository.findById(1L).orElse(null);
        wishlist.setCreator(user);

        return wishlistRepository.save(wishlist);
    }

}
