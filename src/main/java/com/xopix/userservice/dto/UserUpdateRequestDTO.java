package com.xopix.userservice.dto;

import lombok.Data;

@Data
public class UserUpdateRequestDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
