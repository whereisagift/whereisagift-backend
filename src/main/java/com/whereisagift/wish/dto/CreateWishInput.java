package com.whereisagift.wish.dto;

import com.whereisagift.wish.price.PriceInput;
import com.whereisagift.wish.product.ProductSource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.util.List;

@Value
@Builder
public class CreateWishInput {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name;

    @Nullable
    @Size(max = 300, message = "Description must not exceed 300 characters")
    String description;

    @Nullable
    @Size(max = 300, message = "Link must not exceed 300 characters")
    @Pattern(
            regexp = "^(https?://)?[\\w.-]+\\.[a-zA-Z]{2,}.*$",
            message = "Link must be a valid URL"
    )
    String link;

    @Nullable
    @Size(max = 300)
    @Pattern(
            regexp = "^(https?://)?[\\w.-]+\\.[a-zA-Z]{2,}.*$",
            message = "Image URL must be a valid URL"
    )
    String img;

    @NotNull(message = "Type is required")
    ProductSource type;

    @Nullable
    @Valid
    PriceInput price;

    @Size(min = 1, message = "At least one wishlistId is required")
    List<@NotNull Long> wishlistIds;

    @Min(value = 0, message = "Rate must be at least 0")
    @Max(value = 5, message = "Rate must be at most 5")
    Integer rate;
}
