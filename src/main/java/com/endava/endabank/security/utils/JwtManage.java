package com.endava.endabank.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtManage {
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        List<String> authorities =
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority).toList();
        claims.put("permissions", authorities);
        return doGenerateToken(claims, user.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        Algorithm algorithm = Algorithm.HMAC512(secret.getBytes(StandardCharsets.UTF_8));
        return JWT.create().
                withSubject(username).withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 20 * 60 * 1000))
                .withPayload(claims)
                .sign(algorithm);
    }

    public Object[] verifyToken(String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC512(secret.getBytes(StandardCharsets.UTF_8));
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        Claim _permissions = decodedJWT.getClaim("permissions");
        String[] permissions = {};
        if (!_permissions.isNull()) {
            permissions = _permissions.asArray(String.class);
        }
        return new Object[]{username, permissions};
    }
}
