package com.whereisagift.wish.product;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

@Mapper(componentModel = "spring")
public abstract class ProductSourceMapper {
  @Autowired protected ProductService productService;

  public ProductSource toProductSource(@Nullable String url) {
    return productService.detectSource(url);
  }
}
