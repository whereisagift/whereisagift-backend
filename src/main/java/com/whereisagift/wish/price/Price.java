package com.whereisagift.wish.price;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Price {

    @Column(name = "price_currency", length = 3)
    private String currency;

    @Column(name = "price_value", precision = 10, scale = 2)
    private BigDecimal value;
}
