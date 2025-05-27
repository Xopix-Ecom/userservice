package com.xopix.userservice.services;

import com.xopix.userservice.dto.ChangePasswordRequestDTO;
import com.xopix.userservice.dto.LoginRequestDTO;
import com.xopix.userservice.dto.LoginResponseDTO;
import com.xopix.userservice.dto.UserRegistrationDTO;
import com.xopix.userservice.models.User;
import com.xopix.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println(passwordEncoder.matches(loginRequestDTO.getPassword(),user.getPassword()));
        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid Email or Password");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponseDTO(user.getUserId(), user.getEmail(), token);
    }

    @Override
    public String changePassword(Long userId, ChangePasswordRequestDTO changePasswordRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordRequestDTO.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
        userRepository.save(user);

        return "Password changed successfully!!";
    }

    @Override
    public String registerUser(UserRegistrationDTO userRegistrationDTO) {
        if(userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new UsernameNotFoundException("User Already exist");
        }

        User user = new User();
        user.setFirstName(userRegistrationDTO.getFirstName());
        user.setLastName(userRegistrationDTO.getLastName());
        user.setPhone(userRegistrationDTO.getPhone());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));

        userRepository.save(user);
        return "User created successfully";
    }
}
