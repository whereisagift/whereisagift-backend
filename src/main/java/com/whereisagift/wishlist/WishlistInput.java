package com.whereisagift.wishlist;

import lombok.Data;

import java.util.List;

@Data
public class WishlistInput {
    String name;
    String description;
    Iterable<Long> wishIds;
}
