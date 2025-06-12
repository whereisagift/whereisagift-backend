package com.whereisagift.wishlist;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class WishlistInput {
    private String name;
    private String description;
    private List<Long> wishIds = Collections.emptyList();
}
