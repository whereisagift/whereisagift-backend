package com.whereisagift.wish.product;

import com.whereisagift.wish.price.Price;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {

    private String name;

    private ProductSource type;

    private String link;

    private String img;

    private String description;

    private Price price;

}
