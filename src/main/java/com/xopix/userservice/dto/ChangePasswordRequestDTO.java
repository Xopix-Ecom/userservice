package com.xopix.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDTO {

    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;

}
