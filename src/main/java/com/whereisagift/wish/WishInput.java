package com.whereisagift.wish;

import lombok.Data;

@Data
public class WishInput {
    private String name;
    private Iterable<Long> wishlistIds;
}
