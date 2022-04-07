package com.service.auth.model.auth;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtResponse {
	private Long id;
	private String firstname;
	private String lastname;
	private String email;
	private Set<String> roles;
	private String tokenType;
	private String accessToken;
}
