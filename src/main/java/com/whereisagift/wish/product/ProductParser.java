package com.whereisagift.wish.product;

public interface ProductParser {
    boolean supports(String source);

    Product parse(String url);
}
