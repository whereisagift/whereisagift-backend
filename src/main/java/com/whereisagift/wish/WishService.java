package com.whereisagift.wish;

import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
import com.whereisagift.wish.dto.CreateWishInput;
import com.whereisagift.wish.dto.UpdateWishInput;
import graphql.GraphQLException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class WishService {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final WishMapper wishMapper;

    public Wish createWish(CreateWishInput in, Long userId) {
        User creator = userRepository.getReferenceById(userId);
        Wish wish = wishMapper.toEntity(in, creator);
        return wishRepository.save(wish);
    }

    public Wish updateWish(Long id, UpdateWishInput in, Long userId) {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new GraphQLException("Wish not found"));
        if (!wish.getCreator().getId().equals(userId)) {
            throw new GraphQLException("Access denied");
        }
        wishMapper.updateEntity(wish, in);
        return wishRepository.save(wish);
    }

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

    public Boolean deleteWish(Long id, Long userId) {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new GraphQLException("Wish not found"));

        if (!Objects.equals(wish.getCreator().getId(), userId)) {
            throw new GraphQLException("Access denied");
        }

        wishRepository.delete(wish);
        return true;
    }
}
