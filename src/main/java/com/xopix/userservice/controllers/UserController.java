package com.xopix.userservice.controllers;

import com.xopix.userservice.dto.*;
import com.xopix.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * Registers a new user.
     * This endpoint is typically public or secured by a very basic API key.
     * Auth0 would handle the actual authentication/login process for users.
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRegistrationRequestDTO userRegistrationRequestDTO) {
        return ResponseEntity.ok(userService.registerUser(userRegistrationRequestDTO));
    }

    /**
     * Get user profile by ID.
     * Authentication via JWT would happen at API Gateway (Kong).
     * The JWT would contain the user's ID (e.g., Auth0 user ID), which the API Gateway passes
     * as a header (e.g., X-User-ID) or the controller extracts from the security context.
     */
    // @PreAuthorize("isAuthenticated() and (#userId == authentication.principal.id or hasRole('ADMIN'))")
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String userId) {
        log.info("Fetching user with ID: {}", userId);
        return ResponseEntity.ok(UserResponseDTO.fromEntity(userService.getUserById(userId)));
    }

    /**
     * Update user profile by ID.
     * Similar to GET, authentication and authorization (e.g., user can only update their own profile
     * or an admin can update any profile) would be managed by Auth0/API Gateway/Spring Security.
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    // @PreAuthorize("isAuthenticated() and (#userId == authentication.principal.id or hasRole('ADMIN'))")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String userId,
                                                   @Valid @RequestBody UserUpdateRequestDTO request) {
        log.info("Updating user with ID: {}", userId);
        return ResponseEntity.ok(UserResponseDTO.fromEntity(userService.updateUser(userId, request)));
    }

    // --- Internal/Auth0-facing Endpoints ---
    /**
     * This endpoint would typically be called by Auth0's post-registration hook
     * or a management process to link an Auth0 user ID to an internally created user.
     * This should be highly secured (e.g., API key, IP whitelisting)
     */
    @RequestMapping(value = "/_internal/associate-auth0", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDTO> associateAuth0User(@RequestParam String email, @RequestParam String auth0UserId) {
        log.info("Associating Auth0 ID {} with user email {}", auth0UserId, email);
        return ResponseEntity.ok(UserResponseDTO.fromEntity(userService.associateAuth0User(email, auth0UserId)));
    }


}
