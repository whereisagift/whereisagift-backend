package com.whereisagift.wish;

import com.whereisagift.wish.dto.CreateWishInput;
import com.whereisagift.wish.dto.UpdateWishInput;
import com.whereisagift.wish.price.PriceMapper;
import com.whereisagift.wish.product.ProductSourceMapper;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    uses = {PriceMapper.class, ProductSourceMapper.class})
public interface WishMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "creator", ignore = true)
  @Mapping(target = "wishlists", ignore = true)
  @Mapping(target = "type", source = "in.link")
  void toEntity(@MappingTarget Wish wish, CreateWishInput in);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "creator", ignore = true)
  @Mapping(target = "type", ignore = true)
  @Mapping(target = "wishlists", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntity(@MappingTarget Wish wish, UpdateWishInput in);
}
