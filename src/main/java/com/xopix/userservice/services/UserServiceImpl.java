package com.xopix.userservice.services;

import com.xopix.userservice.dto.*;
import com.xopix.userservice.exception.UserAlreadyExistsException;
import com.xopix.userservice.exception.UserNotFoundException;
import com.xopix.userservice.models.User;
import com.xopix.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Registers a new user.
     * In a real Auth0 integration, this method might also trigger Auth0 user creation
     * and then store Auth0's user ID in `auth0UserId`.
     *
     * @param request The user registration request.
     * @return The registered User entity.
     * @throws UserAlreadyExistsException if a user with the given email already exists.
     */
    @Override
    public UserResponseDTO registerUser(UserRegistrationRequestDTO request) throws UserAlreadyExistsException {
        if(userRepository.existsByEmail(request.getEmail())) {
            log.warn("Attempted registration with existing email: {}", request.getEmail());
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists.");
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .build();

        log.info("Registering new user with email: {}", request.getEmail());
        userRepository.save(newUser);
        return UserResponseDTO.fromEntity(newUser);
    }



    /**
     * Retrieves a user by their internal ID.
     *
     * @param id The internal UUID of the user.
     * @return The User entity.
     * @throws UserNotFoundException if the user is not found.
     */
    @Override
    public User getUserById(String id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));
    }


    /**
     * Retrieves a user by their Auth0 User ID.
     * This is useful when the API Gateway passes the Auth0 ID, and you need to find the internal user record.
     *
     * @param auth0UserId The Auth0 user ID.
     * @return The User entity.
     * @throws UserNotFoundException if the user is not found.
     */
    @Override
    public User getUserByAuth0UserId(String auth0UserId) throws UserNotFoundException{
        return userRepository.findByAuth0UserId(auth0UserId)
                .orElseThrow(() -> new UserNotFoundException("User with Auth0 ID " + auth0UserId + " not found."));
    }


    /**
     * Updates an existing user's profile.
     *
     * @param id The internal UUID of the user to update.
     * @param request The user update request.
     * @return The updated User entity.
     * @throws UserNotFoundException if the user is not found.
     */
    @Override
    public User updateUser(String id, UserUpdateRequestDTO request) throws UserNotFoundException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));

        Optional.ofNullable(request.getFirstName()).ifPresent(existingUser::setFirstName);
        Optional.ofNullable(request.getLastName()).ifPresent(existingUser::setLastName);
        Optional.ofNullable(request.getPhoneNumber()).ifPresent(existingUser::setPhoneNumber);

        existingUser.setUpdatedAt(LocalDateTime.now());

        log.info("Updating user with ID: {}", id);
        return userRepository.save(existingUser);
    }

    /**
     * Associates an Auth0 user ID with an internal user.
     * This might happen if a user registers via email/password through the UI,
     * and then later links their Auth0 account (e.g., social login).
     * Or, if Auth0 handles primary registration, this is how you link it to your internal User ID.
     *
     * @param email The user's email.
     * @param auth0UserId The Auth0 user ID to associate.
     * @return The updated User entity.
     * @throws UserNotFoundException if the user is not found.
     */
    @Override
    public User associateAuth0User(String email, String auth0UserId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found for Auth0 association."));

        if (userRepository.findByAuth0UserId(auth0UserId).isPresent()) {
            throw new UserAlreadyExistsException("Auth0 user ID " + auth0UserId + " is already associated with another user.");
        }

        user.setAuth0UserId(auth0UserId);
        log.info("Associated Auth0 user ID {} with user {}", auth0UserId, user.getId());
        return userRepository.save(user);
    }
}
