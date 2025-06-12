package com.whereisagift.wishlist;

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

import java.util.List;

@Slf4j
@Controller
public class WishlistController {

    private final WishlistRepository wishlistRepository;
    private final WishRepository wishRepository;
    private final UserRepository userRepository;

    public WishlistController(WishlistRepository wishlistRepository, WishRepository wishRepository, UserRepository userRepository) {
        this.wishlistRepository = wishlistRepository;
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Iterable<Wishlist> wishlists(@AuthenticationPrincipal Long userId) {
        return wishlistRepository.findByCreatorId(userId);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Wishlist createWishlist(@Argument WishlistInput wishlistInput, @AuthenticationPrincipal Long userId) {
        List<Long> wishIds = wishlistInput.getWishIds();

        Wishlist wishlist = new Wishlist();
        wishlist.setName(wishlistInput.getName());
        wishlist.setDescription(wishlistInput.getDescription());
        wishlist.setCreator(userRepository.getReferenceById(userId));

        if (!wishIds.isEmpty()) {
            List<Wish> wishes = wishRepository.findAllById(wishIds);
            wishlist.setWishes(wishes);
        }

        return wishlistRepository.save(wishlist);
    }
}
