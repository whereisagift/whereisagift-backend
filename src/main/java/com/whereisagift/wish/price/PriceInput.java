package com.whereisagift.wish.price;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record PriceInput(
    @NotBlank(message = "Currency must not be blank")
        @Pattern(
            regexp = "^[A-Z]{3}$",
            message = "Currency must be a 3-letter ISO code (e.g., USD, EUR)")
        String currency,
    @NotNull(message = "Value must not be null")
        @PositiveOrZero(message = "Value must be zero or positive")
        BigDecimal value) {}
