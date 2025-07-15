package com.whereisagift.wish;

import com.whereisagift.wish.dto.CreateWishInput;
import com.whereisagift.wish.dto.UpdateWishInput;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@Validated
@RequiredArgsConstructor
public class WishController {
  private final WishService wishService;

  @QueryMapping
  @PreAuthorize("isAuthenticated()")
  public List<Wish> wishes(@AuthenticationPrincipal Long userId) {
    return wishService.getAllByUser(userId);
  }

  @QueryMapping
  @PreAuthorize("isAuthenticated()")
  public Wish wish(@Argument Long id, @AuthenticationPrincipal Long userId) {
    return wishService.getById(id, userId);
  }

  @MutationMapping
  @PreAuthorize("isAuthenticated()")
  public Wish createWish(
      @Valid @Argument CreateWishInput input, @AuthenticationPrincipal Long userId) {
    return wishService.createWish(input, userId);
  }

  @MutationMapping
  @PreAuthorize("isAuthenticated()")
  public Wish updateWish(
      @Argument Long id,
      @Valid @Argument UpdateWishInput input,
      @AuthenticationPrincipal Long userId) {
    return wishService.updateWish(id, input, userId);
  }

  @MutationMapping
  @PreAuthorize("isAuthenticated()")
  public Boolean deleteWish(@Argument Long id, @AuthenticationPrincipal Long userId) {
    return wishService.deleteWish(id, userId);
  }
}
