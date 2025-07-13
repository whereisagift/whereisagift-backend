package com.whereisagift.wishlist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class UpdateWishlistInput {

    private Long id;

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 300, message = "Description must not exceed 300 characters")
    private String description;

    private List<String> wishIds = Collections.emptyList();

}
