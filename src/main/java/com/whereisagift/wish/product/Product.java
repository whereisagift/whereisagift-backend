package com.whereisagift.wish.product;

import com.whereisagift.wish.price.Price;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@Builder
public class Product {
  private String name;
  private ProductSource type;
  @Nullable private String link;
  @Nullable private String img;
  @Nullable private String description;
  @Nullable private Price price;
}
