package com.whereisagift.wishlist;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@wishlistSecurityService.isOwner(#input.id, " +
        "authentication.principal)")
public @interface IsWishlistOwner {
}
