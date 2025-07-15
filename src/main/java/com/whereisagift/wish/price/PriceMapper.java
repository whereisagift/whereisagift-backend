package com.whereisagift.wish.price;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {
  Price toPrice(PriceInput input);
}
