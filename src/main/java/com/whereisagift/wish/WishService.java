package com.whereisagift.wish;

import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
import com.whereisagift.wish.dto.CreateWishInput;
import com.whereisagift.wish.dto.UpdateWishInput;
import com.whereisagift.wishlist.Wishlist;
import com.whereisagift.wishlist.WishlistRepository;
import graphql.GraphQLException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class WishService {
  private final WishRepository wishRepository;
  private final WishlistRepository wishlistRepository;
  private final UserRepository userRepository;
  private final WishMapper wishMapper;

  public Wish createWish(CreateWishInput in, Long userId) {
    User creator = userRepository.getReferenceById(userId);
    Wish wish = new Wish();
    wish.setCreator(creator);
    wish.setWishlists(loadWishlists(creator, in.wishlistIds()));
    wishMapper.toEntity(wish, in);
    return wishRepository.save(wish);
  }

  public Wish updateWish(Long id, UpdateWishInput in, Long userId) {
    User creator = userRepository.getReferenceById(userId);
    Wish wish =
        wishRepository.findById(id).orElseThrow(() -> new GraphQLException("Wish not found"));
    if (!wish.getCreator().getId().equals(userId)) {
      throw new GraphQLException("Access denied");
    }
    if (in.wishlistIds() != null) {
      wish.setWishlists(loadWishlists(creator, in.wishlistIds()));
    }
    wishMapper.updateEntity(wish, in);
    return wish;
  }

  public List<Wish> getAllByUser(Long userId) {
    return wishRepository.findByCreator(userRepository.getReferenceById(userId));
  }

  public Wish getById(Long id, Long userId) {
    Wish wish =
        wishRepository.findById(id).orElseThrow(() -> new GraphQLException("Wish not found"));

    if (!Objects.equals(wish.getCreator().getId(), userId)) {
      throw new GraphQLException("Access denied");
    }

    return wish;
  }

  public Boolean deleteWish(Long id, Long userId) {
    Wish wish =
        wishRepository.findById(id).orElseThrow(() -> new GraphQLException("Wish not found"));

    if (!Objects.equals(wish.getCreator().getId(), userId)) {
      throw new GraphQLException("Access denied");
    }

    wishRepository.delete(wish);
    return true;
  }

  private List<Wishlist> loadWishlists(User creator, List<@NotNull Long> wishlistIds) {
    Set<Long> idsSet = new HashSet<>(wishlistIds);

    List<Wishlist> lists = wishlistRepository.findAllById(idsSet);

    if (lists.isEmpty()) {
      throw new IllegalArgumentException("At least one wishlistId is required");
    }

    if (lists.size() != idsSet.size()) {
      throw new EntityNotFoundException("One or more wishlists not found");
    }

    for (Wishlist wl : lists) {
      if (!wl.getCreator().equals(creator)) {
        throw new AccessDeniedException("Not owner of wishlist " + wl.getId());
      }
    }

    return lists;
  }
}
