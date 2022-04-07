package com.service.auth.security;

import com.service.auth.security.config.AppAuthConfig;
import io.jsonwebtoken.ExpiredJwtException;
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

    public String createToken(Authentication authentication) {
        var userPrincipal = (UserPrincipal) authentication.getPrincipal();

        var now = new Date();
        var expiryDate = new Date(now.getTime() + appAuthConfig.getTokenExpiryInMs());
        var idString = Long.toString(userPrincipal.getId());
        var claims = Jwts.claims().setSubject(idString);
        // can set more claims here...

        return Jwts.builder()
                .setSubject(idString)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appAuthConfig.getTokenSecret())
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        var claims = Jwts.parser()
                .setSigningKey(appAuthConfig.getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appAuthConfig.getTokenSecret()).parseClaimsJws(authToken);
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

}
