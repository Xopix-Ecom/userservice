package com.xopix.userservice.controllers;

import com.xopix.userservice.dto.ChangePasswordRequestDTO;
import com.xopix.userservice.dto.LoginRequestDTO;
import com.xopix.userservice.dto.LoginResponseDTO;
import com.xopix.userservice.dto.UserRegistrationDTO;
import com.xopix.userservice.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        authService.registerUser(userRegistrationDTO);
        return ResponseEntity.ok("User Registered successfully");
    }

    @RequestMapping(value = "/change-password/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<String> changeUserPassword(@PathVariable Long userId,
                                                     @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        authService.changePassword(userId, changePasswordRequestDTO);
        return ResponseEntity.ok("Password updated successfully");
    }


}
