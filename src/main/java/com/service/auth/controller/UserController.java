package com.service.auth.controller;

import com.service.auth.model.user.UserResponse;
import com.service.auth.security.CurrentUser;
import com.service.auth.security.UserPrincipal;
import com.service.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@CurrentUser UserPrincipal userPrincipal) {
        return ResponseEntity.ok(userService.getUser(userPrincipal));
    }

}
