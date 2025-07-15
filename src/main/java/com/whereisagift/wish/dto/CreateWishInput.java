package com.whereisagift.wish.dto;

import com.whereisagift.wish.price.PriceInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import org.springframework.lang.Nullable;

public record CreateWishInput(
    @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,
    @Nullable @Size(max = 300, message = "Description must not exceed 300 characters")
        String description,
    @Nullable
        @Size(max = 300, message = "Link must not exceed 300 characters")
        @Pattern(
            regexp = "^(https?://)?[\\w.-]+\\.[a-zA-Z]{2,}.*$",
            message = "Link must be a valid URL")
        String link,
    @Nullable
        @Size(max = 300)
        @Pattern(
            regexp = "^(https?://)?[\\w.-]+\\.[a-zA-Z]{2,}.*$",
            message = "Image URL must be a valid URL")
        String img,
    @Nullable @Valid PriceInput price,
    @Size(min = 1, message = "At least one wishlistId is required") List<@NotNull Long> wishlistIds,
    @Min(value = 0, message = "Rate must be at least 0")
        @Max(value = 5, message = "Rate must be at most 5")
        Integer rate) {}
