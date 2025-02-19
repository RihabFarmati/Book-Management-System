package com.example.service;

import com.example.models.User;
import com.example.models.Role;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user, String roleName);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    User getUser(String username);

    List<User> getUsers();

    Optional<User> findUserById(Long id);

    User getUserByUserName(String username);
}
