package com.endava.endabank.controllers;

import com.endava.endabank.dto.user.AuthenticationDto;
import com.endava.endabank.security.utils.JwtManage;
import com.endava.endabank.services.impl.UserAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtManage jwtManage;
    private final UserAuthentication userAuthenticationService;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtManage jwtManage, UserAuthentication userAuthenticationService) {
        this.authenticationManager = authenticationManager;
        this.jwtManage = jwtManage;
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> CreateAuthenticationToken(@RequestBody AuthenticationDto authenticationDto) {
        authenticate(authenticationDto.getEmail(), authenticationDto.getPassword());
        final UserDetails userDetails = userAuthenticationService.loadUserByUsername(authenticationDto.getEmail());
        final String token = jwtManage.generateToken(userDetails);
        Map<String,String> dataResponse = new HashMap<>();
        dataResponse.put("access_token",token);
        return ResponseEntity.ok(dataResponse);
    }

    private void authenticate(String email, String password) {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        System.out.println(authentication.getPrincipal());


    }
}
