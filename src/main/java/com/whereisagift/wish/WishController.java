package com.whereisagift.wish;

import com.whereisagift.user.UserRepository;
import com.whereisagift.wishlist.Wishlist;
import com.whereisagift.wishlist.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WishController {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;

    public WishController(WishRepository wishRepository, UserRepository userRepository,
                          WishlistRepository wishlistRepository) {
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
        this.wishlistRepository = wishlistRepository;
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Iterable<Wish> wishes(@AuthenticationPrincipal Long userId) {
        return wishRepository.findByCreator(userRepository.getReferenceById(userId));
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Wish createWish(@Argument WishInput wishInput,
                           @AuthenticationPrincipal Long userId) {
        List<Long> wishlistIds = wishInput.getWishlistIds();

        Wish wish = new Wish();
        wish.setName(wishInput.getName());
        wish.setDescription(wishInput.getDescription());
        wish.setCreator(userRepository.getReferenceById(userId));

        if (!wishlistIds.isEmpty()) {
            List<Wishlist> wishlists = wishlistRepository.findAllById(wishlistIds);
            wish.setWishlists(wishlists);
        }
        return wishRepository.save(wish);
    }
}
