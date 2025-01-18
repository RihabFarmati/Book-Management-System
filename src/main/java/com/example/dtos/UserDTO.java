package com.example.dtos;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserDTO {

    @Size(min = 3 , message = "your first name should have more than 3 characters ")
    private String firstName;

    private String username;

    private String password;

    private String roleName;

}
