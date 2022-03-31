package com.endava.endabank.controllers;

import com.endava.endabank.constants.Routes;
import com.endava.endabank.dto.user.AuthenticationDto;
import com.endava.endabank.security.UserAuthentication;
import com.endava.endabank.security.utils.JwtManage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(Routes.API_ROUTE)
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtManage jwtManage;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtManage jwtManage) {
        this.authenticationManager = authenticationManager;
        this.jwtManage = jwtManage;
    }

    @PostMapping(value = Routes.LOGIN_ROUTE)
    public ResponseEntity<?> CreateAuthenticationToken(@Valid @RequestBody AuthenticationDto authenticationDto) {
        Authentication authentication = authenticate(authenticationDto.getEmail(), authenticationDto.getPassword());
        final String token = jwtManage.generateToken((UserAuthentication) authentication.getPrincipal());
        Map<String, String> dataResponse = new HashMap<>();
        dataResponse.put("access_token", token);
        return ResponseEntity.ok(dataResponse);
    }

    private Authentication authenticate(String email, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));


    }
}
