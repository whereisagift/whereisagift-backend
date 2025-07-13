package com.whereisagift.wishlist;

import com.whereisagift.user.UserRepository;
import com.whereisagift.wish.Wish;
import com.whereisagift.wish.WishRepository;
import com.whereisagift.wishlist.dto.CreateWishlistInput;
import com.whereisagift.wishlist.dto.UpdateWishlistInput;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Slf4j
@Controller
@Validated
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistRepository wishlistRepository;
    private final WishlistService wishlistService;

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public Iterable<Wishlist> wishlists(@AuthenticationPrincipal Long userId) {
        return wishlistRepository.findByCreatorId(userId);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public Wishlist wishlist(
            @Argument Long id,
            @AuthenticationPrincipal Long userId
    ) {

        return wishlistService.getById(id, userId);

    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Wishlist createWishlist(
            @Valid @Argument CreateWishlistInput input,
            @AuthenticationPrincipal Long userId
    ) {

        return wishlistService.createWishlist(input, userId);

    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Wishlist updateWishlist(
            @Valid @Argument UpdateWishlistInput input,
            @AuthenticationPrincipal Long userId
    ) {

        return wishlistService.updateWishlist(input, userId);

    }
}
