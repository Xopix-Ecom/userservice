package com.xopix.userservice.utils;


import com.xopix.userservice.dto.LoginRequestDTO;
import com.xopix.userservice.models.LoginRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserLoginMapper {
    LoginRequestDTO toEntity(LoginRequest loginRequest);
    LoginRequest toDTO(LoginRequestDTO loginRequestDTO);
}
