package com.whereisagift.wish;

import com.whereisagift.user.User;
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

import java.util.ArrayList;
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
    public Iterable<Wish> wishes(@AuthenticationPrincipal(expression = "subject") String userId) {
        long id = Long.parseLong(userId);

        User user = userRepository.getReferenceById(id);
        
        return wishRepository.findByCreator(user);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Wish createWish(@Argument WishInput wishInput,
                           @AuthenticationPrincipal(expression = "subject") String userId) {
        List<Wishlist> wishlists = new ArrayList<>();
        long id = Long.parseLong(userId);
        User user = userRepository.getReferenceById(id);

        String name = wishInput.getName();
        Iterable<Long> wishlistIds = wishInput.getWishlistIds();

        Wish wish = new Wish();
        wish.setName(name);
        wish.setCreator(user);

        if (wishlistIds.iterator().hasNext()) {
            wishlistIds.forEach(
                    wishlistId -> {
                        Wishlist wishlist = wishlistRepository.findById(wishlistId).orElse(null);
                        wishlists.add(wishlist);
                    }
            );
            wish.setWishlists(wishlists);
        }
        return wishRepository.save(wish);
    }



}
