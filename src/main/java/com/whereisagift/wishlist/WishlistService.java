package com.whereisagift.wishlist;

import com.whereisagift.user.User;
import com.whereisagift.user.UserService;
import com.whereisagift.wishlist.dto.CreateWishlistInput;
import com.whereisagift.wishlist.dto.UpdateWishlistInput;
import graphql.GraphQLException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;
    private final UserService userService;

    public Wishlist createWishlist(CreateWishlistInput input, Long userId) {
        User creator = userService.getById(userId);
        Wishlist wishlist = wishlistMapper.toEntity(input, creator);

        return wishlistRepository.save(wishlist);
    }

    public Wishlist getById(Long wishlistId, Long userId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new GraphQLException("Wishlist not found"));

        if (!Objects.equals(wishlist.getCreator().getId(), userId))
            throw new GraphQLException("Access denied");

        return wishlist;
    }

    public Wishlist updateWishlist(Long id, UpdateWishlistInput input, Long userId) {
        Wishlist wishlist = getById(id, userId);

        return wishlistMapper.updateEntity(wishlist, input);
    }
}
