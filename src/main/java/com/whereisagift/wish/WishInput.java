package com.whereisagift.wish;

import com.whereisagift.wish.price.PriceInput;
import com.whereisagift.wish.product.ProductSource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishInput {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 300, message = "Description must not exceed 300 characters")
    private String description;

    @Size(max = 300, message = "Link must not exceed 300 characters")
    @Pattern(
            regexp = "^(https?://)?[\\w.-]+\\.[a-zA-Z]{2,}.*$",
            message = "Link must be a valid website URL"
    )
    private String link;


    @Size(max = 300, message = "Image URL must not exceed 300 characters")
    @Pattern(
            regexp = "^(https?://)?[\\w.-]+\\.[a-zA-Z]{2,}.*$",
            message = "Image URL must be a valid website URL"
    )
    private String img;

    @NotNull(message = "Type is required")
    private ProductSource type;

    @Valid
    private PriceInput price;

    @Size(min = 1, message = "At least one wishlistId is required")
    private List<Long> wishlistIds = new ArrayList<>();

    @Min(value = 0, message = "Rate must be at least 0")
    @Max(value = 5, message = "Rate must be at most 5")
    private Integer rate;
}

