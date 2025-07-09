package com.whereisagift.wish;

import com.whereisagift.user.User;
import com.whereisagift.wish.dto.CreateWishInput;
import com.whereisagift.wish.dto.UpdateWishInput;
import com.whereisagift.wish.price.Price;
import com.whereisagift.wish.price.PriceInput;
import com.whereisagift.wish.product.ProductService;
import com.whereisagift.wishlist.Wishlist;
import com.whereisagift.wishlist.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WishMapper {

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;

    public Wish toEntity(CreateWishInput in, User creator) {
        Wish w = new Wish();
        w.setCreator(creator);
        w.setName(in.getName());
        w.setDescription(in.getDescription());
        w.setLink(in.getLink());
        w.setType(productService.detectSource(in.getLink()));
        w.setImg(in.getImg());
        w.setRate(in.getRate());
        w.setPrice(mapPrice(in.getPrice()));
        w.setWishlists(loadWishlists(in.getWishlistIds()));
        return w;
    }

    public Wish updateEntity(Wish wish, UpdateWishInput in) {
        if (in.getName() != null) wish.setName(in.getName());
        if (in.getDescription() != null) wish.setDescription(in.getDescription());
        if (in.getImg() != null) wish.setImg(in.getImg());
        if (in.getRate() != null) wish.setRate(in.getRate());
        if (in.getPrice() != null) wish.setPrice(mapPrice(in.getPrice()));
        if (in.getWishlistIds() != null) wish.setWishlists(loadWishlists(in.getWishlistIds()));
        return wish;
    }

    private Price mapPrice(PriceInput in) {
        return Optional.ofNullable(in)
                .map(p -> new Price(p.getCurrency(), p.getValue()))
                .orElse(null);
    }

    private HashSet<Wishlist> loadWishlists(
            List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(wishlistRepository.findAllById(ids));
    }
}