package com.service.auth.service;

import com.service.auth.mapper.UserMapper;
import com.service.auth.model.auth.SignUpRequest;
import com.service.auth.model.user.UserResponse;
import com.service.auth.repository.UserRepository;
import com.service.auth.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void createUser(SignUpRequest signUpRequest) {
        var user = userMapper.map(signUpRequest, passwordEncoder);
        userRepository.save(user);
    }

    public UserResponse getUser(UserPrincipal userPrincipal) {
        return userMapper.map(userPrincipal);
    }

}
