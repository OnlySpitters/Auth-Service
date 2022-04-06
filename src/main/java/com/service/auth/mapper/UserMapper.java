package com.service.auth.mapper;

import com.service.auth.model.user.SignUpRequest;
import com.service.auth.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "password", expression = "java(getEncodedPassword(signUpRequest, passwordEncoder))")
    User map(SignUpRequest signUpRequest, PasswordEncoder passwordEncoder);

    default String getEncodedPassword(SignUpRequest signUpRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(signUpRequest.getPassword());
    }

}
