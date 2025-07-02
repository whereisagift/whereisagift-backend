package com.whereisagift.wishlist;

import com.whereisagift.user.UserRepository;
import com.whereisagift.wish.Wish;
import com.whereisagift.wish.WishRepository;
import com.whereisagift.wishlist.dto.CreateWishlistInput;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class WishlistController {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;

    private final WishlistService wishlistService;

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Iterable<Wishlist> wishlists(@AuthenticationPrincipal Long userId) {
        return wishlistRepository.findByCreatorId(userId);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Wishlist createWishlist(
            @Argument CreateWishlistInput input,
            @AuthenticationPrincipal Long userId
    ) {

        return wishlistService.createWishlist(input, userId);

    }
}
