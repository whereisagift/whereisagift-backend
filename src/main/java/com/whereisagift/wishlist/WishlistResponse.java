package com.whereisagift.wishlist;

import lombok.Data;

@Data
public class WishlistResponse<T> {
    private boolean isEmpty;
    private String message;
    private T data;

    private WishlistResponse(boolean isEmpty, String message, T data) {
        this.isEmpty = isEmpty;
        this.message = message;
        this.data = data;
    }

    public static <T> WishlistResponse<T> of(T data) {
        return new WishlistResponse<>(false, null, data);
    }

    public static <T> WishlistResponse<T> empty(String message) {
        return new WishlistResponse<>(true, message, null);
    }
}
