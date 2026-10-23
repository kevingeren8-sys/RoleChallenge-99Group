package com.exercise.publicapi.service;

import com.exercise.publicapi.dto.CreateListingRequest;
import com.exercise.publicapi.dto.CreateUserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PublicApiService {

    private final RestTemplate restTemplate;
    private final String listingUrl;
    private final String userUrl;

    public PublicApiService(
            RestTemplate restTemplate,
            @Value("${services.listing-url}") String listingUrl,
            @Value("${services.user-url}") String userUrl
    ) {
        this.restTemplate = restTemplate;
        this.listingUrl = listingUrl;
        this.userUrl = userUrl;
    }

    public Map<String, Object> getListings(int pageNum, int pageSize, Integer userId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(listingUrl + "/listings")
                .queryParam("pageNum", pageNum)
                .queryParam("pageSize", pageSize);
        if (userId != null) {
            uriBuilder.queryParam("userId", userId);
        }

        Map<?, ?> listingResponse = restTemplate.getForObject(uriBuilder.build().toUri(), Map.class);
        List<?> rawListings = (List<?>) listingResponse.get("listings");
        List<Map<String, Object>> listingsWithUsers = new ArrayList<>();

        for (Object rawListing : rawListings) {
            Map<?, ?> listing = (Map<?, ?>) rawListing;
            Integer listingUserId = ((Number) listing.get("userId")).intValue();
            Map<?, ?> userResponse = restTemplate.getForObject(
                    userUrl + "/users/{id}", Map.class, listingUserId
            );

            Map<String, Object> listingWithUser = new LinkedHashMap<>();
            listingWithUser.put("id", listing.get("id"));
            listingWithUser.put("listingType", listing.get("listingType"));
            listingWithUser.put("price", listing.get("price"));
            listingWithUser.put("createdAt", listing.get("createdAt"));
            listingWithUser.put("updatedAt", listing.get("updatedAt"));
            listingWithUser.put("user", userResponse.get("user"));
            listingsWithUsers.add(listingWithUser);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("result", true);
        response.put("listings", listingsWithUsers);
        return response;
    }

    public Map<String, Object> createUser(CreateUserRequest request) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name", request.name());
        Map<?, ?> downstreamResponse = postForm(userUrl + "/users", form);
        return Map.of("user", downstreamResponse.get("user"));
    }

    public Map<String, Object> createListing(CreateListingRequest request) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("userId", request.userId().toString());
        form.add("listingType", request.listingType());
        form.add("price", request.price().toString());
        Map<?, ?> downstreamResponse = postForm(listingUrl + "/listings", form);
        return Map.of("listing", downstreamResponse.get("listing"));
    }

    private Map<?, ?> postForm(String url, MultiValueMap<String, String> form) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return restTemplate.postForObject(url, new HttpEntity<>(form, headers), Map.class);
    }
}
