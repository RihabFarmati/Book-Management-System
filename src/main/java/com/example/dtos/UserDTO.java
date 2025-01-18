package com.example.dtos;

import com.example.models.User;
import lombok.Data;

@Data
public class UserDTO {

    private String firstName;

    private String username;

    private String password;

    private String roleName;

    public User getUser() {
        User user = new User();

        user.setFirstName(this.firstName);
        user.setPassword(this.password);
        user.setUserName(this.username);
        return user;
    }
}
