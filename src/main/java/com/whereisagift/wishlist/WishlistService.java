package com.whereisagift.wishlist;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class WishlistService {

    final private WishlistRepository wishlistRepository;

    public boolean isOwner(Long wishlistId, Long userId) {
        //проверяем существование вишлиста
        if (!wishlistRepository.existsById(wishlistId)) {
            throw new ResourceNotFoundException("Wishlist not found");
        }

        //проверяем владельца
        if (!wishlistRepository.existsByIdAndCreatorId(wishlistId, userId)) {
            throw new AccessDeniedException("You dont have permission to edit this wishlist");
        }

        return true;
    }
}
