package com.whereisagift.wish;

import com.whereisagift.wishlist.Wishlist;
import com.whereisagift.wishlist.WishlistRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WishController {

    private final WishRepository wishRepository;
    private final WishlistRepository wishlistRepository;

    public WishController(WishRepository wishRepository, WishlistRepository wishlistRepository) {
        this.wishRepository = wishRepository;
        this.wishlistRepository = wishlistRepository;
    }

    @QueryMapping
    public Iterable<Wish> wishes() {
        return wishRepository.findAll();
    }

    @MutationMapping
    public Wish createWish(@Argument String name, @Argument Long wishlistId) {

        Wish wish = new Wish();
        wish.setName(name);
        Wishlist wishlist = wishlistRepository.findById(wishlistId).orElse(null);
        wish.setWishlist(wishlist);
        return wishRepository.save(wish);
    }

}
