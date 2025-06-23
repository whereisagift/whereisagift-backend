package com.whereisagift.wishlist;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class UpdateWishlistInput {
    private Long id;
    private String name;
    private String description;
    private List<String> wishIds = Collections.emptyList();

}
