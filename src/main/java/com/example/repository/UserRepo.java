package com.example.repository;

import com.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    User findUserByUserName(String userName);
}
