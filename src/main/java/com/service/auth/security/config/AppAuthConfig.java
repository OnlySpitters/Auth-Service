package com.service.auth.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.auth")
@Component
@Getter
@Setter
public class AppAuthConfig {

    private String tokenSecret;
    private long tokenExpiryInMs;

}
