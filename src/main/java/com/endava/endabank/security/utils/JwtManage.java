package com.endava.endabank.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.endava.endabank.exceptions.customExceptions.BadDataException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtManage {

    public JwtManage(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    private final String secret;

    public String generateToken(Integer id, String username) throws BadDataException {
        Map<String, Object> claims = new HashMap<>();
        if(id == null || username == null || username.equals("")){
            throw new BadDataException("The id and the username are required for the token creation");
        }
        claims.put("userId", id);
        return doGenerateToken(claims, username);
    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        Algorithm algorithm = Algorithm.HMAC512(secret);
        return JWT.create().
                withSubject(username).withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 20 * 60 * 1000))
                .withPayload(claims)
                .sign(algorithm);
    }

    public Integer verifyToken(String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC512(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        Claim id = decodedJWT.getClaim("userId");
        return id.asInt();
    }
}
