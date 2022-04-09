package com.service.auth.service;

import com.service.auth.exception.TokenRefreshException;
import com.service.auth.model.auth.JwtResponse;
import com.service.auth.model.auth.RefreshTokenRequest;
import com.service.auth.model.auth.RefreshTokenResponse;
import com.service.auth.model.auth.SignInRequest;
import com.service.auth.model.auth.SignUpRequest;
import com.service.auth.security.TokenProvider;
import com.service.auth.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public void signUp(SignUpRequest signUpRequest) {
        userService.createUser(signUpRequest);
    }

    public JwtResponse signin(SignInRequest signInRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword());
        var authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var userPrincipal = (UserPrincipal) authentication.getPrincipal();
        var token = tokenProvider.createAccessToken(authentication);
        return prepareJwtResponse(token, userPrincipal);
    }

    private JwtResponse prepareJwtResponse(String token, UserPrincipal userPrincipal) {
        return JwtResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .id(userPrincipal.getId())
                .firstname(userPrincipal.getFirstname())
                .lastname(userPrincipal.getLastname())
                .email(userPrincipal.getEmail())
                .roles(userPrincipal.getRoles())
                .refreshToken(tokenProvider.createRefreshToken(userPrincipal.getId()))
                .build();
    }

    public RefreshTokenResponse refresh(RefreshTokenRequest refreshTokenRequest) {
        if (tokenProvider.validateRefreshToken(refreshTokenRequest.getRefreshToken())) {
            var id = tokenProvider.getUserIdFromRefreshToken(refreshTokenRequest.getRefreshToken());
            return RefreshTokenResponse.builder()
                    .accessToken(tokenProvider.createAccessToken(id))
                    .refreshToken(tokenProvider.createRefreshToken(id))
                    .tokenType("Bearer")
                    .build();
        }
        throw new TokenRefreshException(refreshTokenRequest.getRefreshToken(), "Invalid refresh token");
    }

}
