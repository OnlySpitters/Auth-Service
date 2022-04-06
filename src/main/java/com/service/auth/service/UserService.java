package com.service.auth.service;

import com.service.auth.mapper.UserMapper;
import com.service.auth.model.user.SignUpRequest;
import com.service.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest signUpRequest) {
        var user = userMapper.map(signUpRequest, passwordEncoder);
        userRepository.save(user);
    }

}
