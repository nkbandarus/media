package com.binge.service;

import com.binge.data.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(String userId);

    User addNewUser(User user);
}
