package com.exercise.publicapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateListingRequest(
        @NotNull @Min(1) Integer userId,
        @NotNull @Pattern(regexp = "rent|sale") String listingType,
        @NotNull @Min(1) Integer price
) {
}
