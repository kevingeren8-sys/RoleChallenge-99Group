package com.exercise.userservice.controller;

import com.exercise.userservice.dto.UserDto;
import com.exercise.userservice.entity.User;
import com.exercise.userservice.service.UserService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Validated
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Map<String, Object> getUsers(
            @RequestParam(defaultValue = "1") @Min(1) int pageNum,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize
    ) {
        List<UserDto> users = userService.getUsers(pageNum - 1, pageSize)
                .stream()
                .map(this::toDto)
                .toList();

        return Map.of("result", true, "users", users);
    }

    @GetMapping("/users/{id}")
    public Map<String, Object> getUser(@PathVariable @Min(1) Integer id) {
        return Map.of("result", true, "user", toDto(userService.getUser(id)));
    }

    @PostMapping("/users")
    public Map<String, Object> createUser(
            @RequestParam @NotBlank String name
    ) {
        User user = new User();
        user.setName(name);
        return Map.of("result", true, "user", toDto(userService.createUser(user)));
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
