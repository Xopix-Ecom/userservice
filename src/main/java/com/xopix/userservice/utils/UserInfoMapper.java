package com.xopix.userservice.utils;

import com.xopix.userservice.dto.UserRegistrationDTO;
import com.xopix.userservice.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {
    User toEntity(UserRegistrationDTO userRegistrationDTO);
    UserRegistrationDTO toDTO(User user);
}
