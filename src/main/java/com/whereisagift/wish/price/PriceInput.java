package com.whereisagift.wish.price;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceInput {

    @NotBlank(message = "Currency must not be blank")
    @Pattern(
            regexp = "^[A-Z]{3}$",
            message = "Currency must be a 3-letter ISO code (e.g., USD, EUR)"
    )
    private String currency;

    @NotNull(message = "Value must not be null")
    @PositiveOrZero(message = "Value must be zero or positive")
    private BigDecimal value;
}
