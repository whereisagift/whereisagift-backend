package com.whereisagift.wishlist;

import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
import com.whereisagift.wish.Wish;
import com.whereisagift.wish.WishRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class WishlistController {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final WishRepository wishRepository;

    public WishlistController(WishlistRepository wishlistRepository, UserRepository userRepository, WishRepository wishRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.wishRepository = wishRepository;
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public Iterable<Wishlist> wishlists(@AuthenticationPrincipal(expression = "subject") String userId) {
        long id = Long.parseLong(userId);

        return wishlistRepository.findByCreatorId(id);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Wishlist createWishlist(@Argument WishlistInput wishlistInput, @AuthenticationPrincipal(expression = "subject") String userId) {
        long id = Long.parseLong(userId);
        User user = userRepository.getReferenceById(id);

        String name = wishlistInput.getName();
        String description = wishlistInput.getDescription();
        Iterable<Long> wishIds = wishlistInput.getWishIds();

        Wishlist wishlist = new Wishlist();
        wishlist.setName(name);
        wishlist.setDescription(description);
        wishlist.setCreator(user);

        if (wishIds.iterator().hasNext()) {
            List<Wish> wishes = wishRepository.findAllById(wishIds);
            wishlist.setWishes(wishes);
        }
        return wishlistRepository.save(wishlist);
    }
}
