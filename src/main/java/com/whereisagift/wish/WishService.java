package com.whereisagift.wish;

import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
import com.whereisagift.wish.price.Price;
import com.whereisagift.wishlist.WishlistRepository;
import graphql.GraphQLException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WishService {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;

    public List<Wish> getAllByUser(Long userId) {
        return wishRepository.findByCreator(userRepository.getReferenceById(userId));
    }

    public Wish getById(Long id, Long userId) {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new GraphQLException("Wish not found"));

        if (!Objects.equals(wish.getCreator().getId(), userId)) {
            throw new GraphQLException("Access denied");
        }

        return wish;
    }

    public Wish createWish(WishInput input, Long userId) {
        User creator = userRepository.getReferenceById(userId);
        Wish wish = mapToWish(new Wish(), input);
        wish.setCreator(creator);
        return wishRepository.save(wish);
    }

    public Wish updateWish(Long id, WishInput input, Long userId) {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new GraphQLException("Wish not found"));

        if (!Objects.equals(wish.getCreator().getId(), userId)) {
            throw new GraphQLException("Access denied");
        }

        return wishRepository.save(mapToWish(wish, input));
    }

    public Boolean deleteWish(Long id, Long userId) {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new GraphQLException("Wish not found"));

        if (!Objects.equals(wish.getCreator().getId(), userId)) {
            throw new GraphQLException("Access denied");
        }

        wishRepository.delete(wish);
        return true;
    }

    private Wish mapToWish(Wish wish, WishInput input) {
        wish.setName(input.getName());
        wish.setDescription(input.getDescription());
        wish.setLink(input.getLink());
        wish.setImg(input.getImg());
        wish.setType(input.getType());
        wish.setRate(input.getRate());

        wish.setPrice(
                Optional.ofNullable(input.getPrice())
                        .map(p -> new Price(p.getCurrency(), p.getValue()))
                        .orElse(null)
        );

        wish.setWishlists(new HashSet<>(wishlistRepository.findAllById(input.getWishlistIds())));

        return wish;
    }
}
