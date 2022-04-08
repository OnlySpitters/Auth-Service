package com.service.auth.security;

import com.service.auth.security.config.AppAuthConfig;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    private final AppAuthConfig appAuthConfig;

    public String createAccessToken(Authentication authentication) {
        var userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return createAccessToken(userPrincipal.getId());
    }

    public String createRefreshToken(long id) {
        return createBasicToken(Long.toString(id), appAuthConfig.getRefreshTokenExpiryInMs())
                .signWith(SignatureAlgorithm.HS512, appAuthConfig.getRefreshTokenSecret())
                .compact();
    }

    public String createAccessToken(long id) {
        var idString = Long.toString(id);
        var claims = Jwts.claims().setSubject(idString);
        // can set more claims here...

        return createBasicToken(idString, appAuthConfig.getTokenExpiryInMs())
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, appAuthConfig.getTokenSecret())
                .compact();
    }

    public Long getUserIdFromToken(String token, String secret) {
        var claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public Long getUserIdFromAccessToken(String token) {
        return getUserIdFromToken(token, appAuthConfig.getTokenSecret());
    }

    public Long getUserIdFromRefreshToken(String token) {
        return getUserIdFromToken(token, appAuthConfig.getRefreshTokenSecret());
    }

    private boolean validateToken(String secret, String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature", ex);
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token", ex);
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token", ex);
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.", ex);
        }
        return false;
    }

    private JwtBuilder createBasicToken(String id, long expiry) {
        var now = new Date();
        var expiryDate = new Date(now.getTime() + expiry);

        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(now)
                .setExpiration(expiryDate);
    }

    public boolean validateAccessToken(String authToken) {
        return validateToken(appAuthConfig.getTokenSecret(), authToken);
    }

    public boolean validateRefreshToken(String refreshAuthToken) {
        return validateToken(appAuthConfig.getRefreshTokenSecret(), refreshAuthToken);
    }

}
