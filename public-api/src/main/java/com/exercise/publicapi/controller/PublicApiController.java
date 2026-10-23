package com.exercise.publicapi.controller;

import com.exercise.publicapi.dto.CreateListingRequest;
import com.exercise.publicapi.dto.CreateUserRequest;
import com.exercise.publicapi.service.PublicApiService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Validated
@RestController
public class PublicApiController {

    private final PublicApiService publicApiService;

    public PublicApiController(PublicApiService publicApiService) {
        this.publicApiService = publicApiService;
    }

    @GetMapping("/public-api/listings")
    public Map<String, Object> getListings(
            @RequestParam(defaultValue = "1") @Min(1) int pageNum,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize,
            @RequestParam(required = false) @Min(1) Integer userId
    ) {
        return publicApiService.getListings(pageNum, pageSize, userId);
    }

    @PostMapping("/public-api/users")
    public Map<String, Object> createUser(@Valid @RequestBody CreateUserRequest request) {
        return publicApiService.createUser(request);
    }

    @PostMapping("/public-api/listings")
    public Map<String, Object> createListing(@Valid @RequestBody CreateListingRequest request) {
        return publicApiService.createListing(request);
    }
}
