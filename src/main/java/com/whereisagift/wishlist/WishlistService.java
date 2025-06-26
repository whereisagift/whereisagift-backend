package com.whereisagift.wishlist;

import com.whereisagift.user.UserRepository;
import com.whereisagift.wish.Wish;
import com.whereisagift.wish.WishRepository;
import graphql.GraphQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishlistService {

    final private WishlistRepository wishlistRepository;
    final private UserRepository userRepository;
    final private WishRepository wishRepository;

    public boolean isOwner(Long wishlistId, Long userId) {

        //проверяем существование вишлиста
        if (!wishlistRepository.existsById(wishlistId)) {
            throw new GraphQLException("Wishlist not found");
        }

        //проверяем владельца
        if (!wishlistRepository.existsByIdAndCreatorId(wishlistId, userId)) {
            throw new GraphQLException("You dont have permission to edit this wishlist");
        }

        return true;
    }

    public Wishlist createWishlist(WishlistInput wishlistInput, Long userId) {

        List<Long> wishIds = wishlistInput.getWishIds().stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        Wishlist wishlist = new Wishlist();
        wishlist.setName(wishlistInput.getName());
        wishlist.setDescription(wishlistInput.getDescription());
        wishlist.setCreator(userRepository.getReferenceById(userId));
//        wishlist.setCreatedAt(OffsetDateTime.now());
//        wishlist.setUpdatedAt(OffsetDateTime.now());

        if (!wishIds.isEmpty()) {

            List<Wish> wishes = wishRepository.findAllById(wishIds);
            wishlist.setWishes(wishes);
        }

        return wishlistRepository.save(wishlist);
    }

    public Wishlist updateWishlist(Long id, WishlistInput wishlistInput, Long userId) {

        Wishlist wishlist = wishlistRepository.findById(id).orElseThrow(() -> new GraphQLException("Wishlist not found"));

        if (!Objects.equals(wishlist.getCreator().getId(), userId))
            throw new GraphQLException("Access denied");

        // обновляем поля
        Optional.ofNullable(wishlistInput.getName()).ifPresent(wishlist::setName);
        Optional.ofNullable(wishlistInput.getDescription()).ifPresent(wishlist::setDescription);

        // обновляем список желаний
        if (wishlistInput.getWishIds() != null) {
            List<Wish> wishes = wishRepository.findAllById(
                    wishlistInput.getWishIds().stream()
                            .map(Long::valueOf)
                            .collect(Collectors.toList())
            );
            // какие-то желания могу быть не найдены, вернется только часть (без ошибки и исключения)
            wishlist.setWishes(wishes);
        }

        return wishlistRepository.save(wishlist);
    }
}
