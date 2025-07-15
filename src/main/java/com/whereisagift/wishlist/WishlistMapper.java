package com.whereisagift.wishlist;

import com.whereisagift.user.User;
import com.whereisagift.user.UserService;
import com.whereisagift.wish.Wish;
import com.whereisagift.wish.WishRepository;
import com.whereisagift.wishlist.dto.CreateWishlistInput;
import com.whereisagift.wishlist.dto.UpdateWishlistInput;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WishlistMapper {
  private final WishRepository wishRepository;
  private final UserService userService;

  public Wishlist toEntity(CreateWishlistInput input, User creator) {
    Wishlist wishlist = new Wishlist();
    wishlist.setName(input.getName());
    wishlist.setCreator(creator);

    Optional.ofNullable(input.getDescription()).ifPresent(wishlist::setDescription);

    if (!input.getWishIds().isEmpty()) {
      List<Long> wishIds =
          input.getWishIds().stream().map(Long::valueOf).collect(Collectors.toList());

      List<Wish> wishes = wishRepository.findAllById(wishIds);
      wishlist.setWishes(wishes);
    }

    return wishlist;
  }

  public Wishlist updateEntity(Wishlist wishlist, UpdateWishlistInput input) {
    Optional.ofNullable(input.getName()).ifPresent(wishlist::setName);
    Optional.ofNullable(input.getDescription()).ifPresent(wishlist::setDescription);

    if (!input.getWishIds().isEmpty()) {
      List<Long> wishIds =
          input.getWishIds().stream().map(Long::valueOf).collect(Collectors.toList());

      List<Wish> wishes = wishRepository.findAllById(wishIds);
      wishlist.setWishes(wishes);
    }

    return wishlist;
  }
}
