package com.binge.service;

import com.binge.data.User;
import com.binge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public List<User> getAllUsers() {
        return (List<User>)repository.findAll();
    }

    @Override
    public User getUserById(String userId) {
        return repository.findById(userId).get();
    }

    @Override
    public User addNewUser(User user) {
        return repository.save(user);
    }

}
