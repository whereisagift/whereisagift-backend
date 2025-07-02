package com.whereisagift.wishlist;

import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
import com.whereisagift.wish.Wish;
import com.whereisagift.wish.WishRepository;
import com.whereisagift.wishlist.dto.CreateWishlistInput;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class WishlistMapper {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;

    public Wishlist toDomain(CreateWishlistInput input, Long userId) {

        User creator = userRepository.getReferenceById(userId);
        Wishlist wishlist = new Wishlist();
        wishlist.setName(input.getName());
        wishlist.setCreator(creator);

        Optional.ofNullable(input.getDescription())
                .ifPresent(wishlist::setDescription);

        if (!input.getWishIds().isEmpty()) {

            List<Long> wishIds = input.getWishIds().stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            List<Wish> wishes = wishRepository.findAllById(wishIds);
            wishlist.setWishes(wishes);
        }

        return wishlist;
    }

}
