package com.whereisagift.wishlist;

import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
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
    private final UserRepository userRepository;

    public WishlistController(WishlistRepository wishlistRepository, UserRepository userRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public WishlistResponse<List<Wishlist>> wishlists(@AuthenticationPrincipal(expression = "subject") String userId) {

        long id = Long.parseLong(userId);

        List<Wishlist> wishlists = wishlistRepository.findByCreatorId(id);

        return wishlists.isEmpty()
                ? WishlistResponse.empty("You don't have any wishlist yet")
                : WishlistResponse.of(wishlists);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Wishlist createWishlist(@Argument String name, @AuthenticationPrincipal(expression = "subject") String userId) {
        Wishlist wishlist = new Wishlist();
        wishlist.setName(name);

        long id = Long.parseLong(userId);

        User user = userRepository.getReferenceById(id);
        wishlist.setCreator(user);

        return wishlistRepository.save(wishlist);
    }

}
