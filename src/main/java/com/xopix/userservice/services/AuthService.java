package com.xopix.userservice.services;

import com.xopix.userservice.dto.ChangePasswordRequestDTO;
import com.xopix.userservice.dto.LoginRequestDTO;
import com.xopix.userservice.dto.LoginResponseDTO;
import com.xopix.userservice.dto.UserRegistrationDTO;


public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    String changePassword(Long userId, ChangePasswordRequestDTO changePasswordRequestDTO);
    String registerUser(UserRegistrationDTO userRegistrationDTO);
}
