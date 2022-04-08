package com.service.auth.model.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenResponse {
	private String tokenType;
	private String accessToken;
	private String refreshToken;
}
