package com.whereisagift.wishlist;

import lombok.Data;

import java.util.List;

@Data
public class WishlistInput {
    private String name;
    private String description;
    private Iterable<Long> wishIds;
}
