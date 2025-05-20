package com.whereisagift.wish;

import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
public class WishController {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;

    public WishController(WishRepository wishRepository, UserRepository userRepository) {
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Iterable<Wish> wishes(@AuthenticationPrincipal(expression = "subject") String userId) {
        long id = Long.parseLong(userId);

        User user = userRepository.getReferenceById(id);
        
        return wishRepository.findByCreator(user);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Wish createWish(@Argument String name,
                           @AuthenticationPrincipal(expression = "subject") String userId) {
        long id = Long.parseLong(userId);

        User user = userRepository.getReferenceById(id);

        Wish wish = new Wish();
        wish.setName(name);
        wish.setCreator(user);
        return wishRepository.save(wish);
    }

}
