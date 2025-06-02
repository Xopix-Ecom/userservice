package com.xopix.userservice.services;

import com.xopix.userservice.dto.*;
import com.xopix.userservice.models.User;


public interface UserService {
    UserResponseDTO registerUser(UserRegistrationRequestDTO userRegistrationRequestDTO);
    User getUserById(String id);
    User getUserByAuth0UserId(String auth0UserId);
    User updateUser(String id, UserUpdateRequestDTO userUpdateRequestDTO);
    User associateAuth0User(String email, String auth0UserId);
}
