package com.whereisagift.wish;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class WishInput {
    private String name;
    private List<Long> wishlistIds = Collections.emptyList();
    private String description;
}
