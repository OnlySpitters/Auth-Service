package com.service.auth.model.user;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Set<String> roles;
}
