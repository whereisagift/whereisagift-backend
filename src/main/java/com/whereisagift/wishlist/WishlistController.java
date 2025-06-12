package com.whereisagift.wishlist;

import com.whereisagift.user.User;
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

    public WishlistController(WishlistRepository wishlistRepository, WishRepository wishRepository) {
        this.wishlistRepository = wishlistRepository;
        this.wishRepository = wishRepository;
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Iterable<Wishlist> wishlists(@AuthenticationPrincipal User user) {
        return wishlistRepository.findByCreatorId(user.getId());
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Wishlist createWishlist(@Argument WishlistInput wishlistInput, @AuthenticationPrincipal User user) {
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
