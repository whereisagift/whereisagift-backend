package com.whereisagift.wishlist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class CreateWishlistInput {
  @NotBlank(message = "Name is required")
  @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
  private String name;

  @Nullable
  @Size(max = 300, message = "Description must not exceed 300 characters")
  private String description;

  private List<String> wishIds = Collections.emptyList();
}
