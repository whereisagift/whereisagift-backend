package com.whereisagift.wishlist;

import com.whereisagift.user.UserRepository;
import com.whereisagift.wish.Wish;
import com.whereisagift.wish.WishRepository;
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

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WishlistController {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Iterable<Wishlist> wishlists(@AuthenticationPrincipal Long userId) {
        return wishlistRepository.findByCreatorId(userId);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Wishlist createWishlist(@Argument WishlistInput wishlistInput, @AuthenticationPrincipal Long userId) {
        List<Long> wishIds = wishlistInput.getWishIds().stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        Wishlist wishlist = new Wishlist();
        wishlist.setName(wishlistInput.getName());
        wishlist.setDescription(wishlistInput.getDescription());
        wishlist.setCreator(userRepository.getReferenceById(userId));

        if (!wishIds.isEmpty()) {

            List<Wish> wishes = wishRepository.findAllById(wishIds);
            wishlist.setWishes(wishes);
        }

        return wishlistRepository.save(wishlist);
    }

    @MutationMapping
    @IsWishlistOwner
    @PreAuthorize("isAuthenticated")
    @Transactional
    public Wishlist updateWishlist(
            @Valid @Argument UpdateWishlistInput input,
            @AuthenticationPrincipal Long userId
    ) {
        // получаем вишлист, ранее в аннотации проверяли существование
        Wishlist wishlist = wishlistRepository.getReferenceById(input.getId());

        // обновляем поля
        Optional.ofNullable(input.getName()).ifPresent(wishlist::setName);
        Optional.ofNullable(input.getDescription()).ifPresent(wishlist::setDescription);

        // обновляем список желаний
        if (input.getWishIds() != null) {
            List<Wish> wishes = wishRepository.findAllById(
                    input.getWishIds().stream()
                            .map(Long::valueOf)
                            .collect(Collectors.toList())
            );
            // какие-то желания могу быть не найдены, вернется только часть (без ошибки и исключения)
            wishlist.setWishes(wishes);
        }

        return wishlistRepository.save(wishlist);
    }

}
