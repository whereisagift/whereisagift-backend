package com.whereisagift.wishlist;

import com.whereisagift.wish.Wish;
import com.whereisagift.wishlist.dto.CreateWishlistInput;
import com.whereisagift.wishlist.dto.UpdateWishlistInput;
import graphql.GraphQLException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;

    public Wishlist createWishlist(CreateWishlistInput input, Long userId) {

        Wishlist wishlist = wishlistMapper.toEntity(input, userId);

        return wishlistRepository.save(wishlist);

    }

    public Wishlist getById(Long wishlistId, Long userId) {

        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new GraphQLException("Wishlist not found"));

        if (!Objects.equals(wishlist.getCreator().getId(), userId))
            throw new GraphQLException("Access denied");

        return wishlist;
    }

    public Wishlist updateWishlist(UpdateWishlistInput input, Long userId) {

        Wishlist wishlist = getById(input.getId(), userId);

        return wishlistMapper.updateEntity(wishlist, input);

    }

}
