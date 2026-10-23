package com.exercise.userservice.service;

import com.exercise.userservice.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUsers(int pageIndex, int pageSize);

    User getUser(Integer id);

    User createUser(User user);
}
