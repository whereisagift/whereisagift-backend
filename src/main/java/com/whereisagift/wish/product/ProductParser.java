package com.whereisagift.wish.product;

public interface ProductParser {
    boolean supports(String source);

    ProductSource getSource();

    Product parse(String url);
}
