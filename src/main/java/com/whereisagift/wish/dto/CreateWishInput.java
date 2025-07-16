package com.whereisagift.wish.dto;

import com.whereisagift.wish.price.PriceInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import org.springframework.lang.Nullable;

public record CreateWishInput(
    @NotBlank(message = "{wish.validation.name.NotBlank}")
        @Size(min = 2, max = 100, message = "{wish.validation.name.Size}")
        String name,
    @Nullable @Size(max = 300, message = "{wish.validation.description.Size}") String description,
    @Nullable
        @Size(max = 300, message = "{wish.validation.link.Size}")
        @Pattern(
            regexp = "^(https?://)?[\\w.-]+\\.[a-zA-Z]{2,}.*$",
            message = "{wish.validation.link.Pattern}")
        String link,
    @Nullable
        @Size(max = 300, message = "{wish.validation.img.Size}")
        @Pattern(
            regexp = "^(https?://)?[\\w.-]+\\.[a-zA-Z]{2,}.*$",
            message = "{wish.validation.img.Pattern}")
        String img,
    @Nullable @Valid PriceInput price,
    @Size(min = 1, message = "{wish.validation.rate.Size}") List<@NotNull Long> wishlistIds,
    @Min(value = 0, message = "{wish.validation.rate.Min}")
        @Max(value = 5, message = "{wish.validation.rate.Max}")
        Integer rate) {}
