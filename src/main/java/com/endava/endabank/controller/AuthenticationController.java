package com.endava.endabank.controller;

import com.endava.endabank.constants.Routes;
import com.endava.endabank.dto.user.AuthenticationDto;
import com.endava.endabank.exceptions.customExceptions.BadDataException;
import com.endava.endabank.service.impl.UserAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(Routes.API_ROUTE)
@AllArgsConstructor
public class AuthenticationController {
    private final UserAuthenticationService userAuthenticationService;
    private final AuthenticationManager authenticationManager;

    @PostMapping(value = Routes.LOGIN_ROUTE)
    public ResponseEntity<?> CreateAuthenticationToken(@Valid @RequestBody AuthenticationDto authenticationDto) throws BadDataException {
        Authentication authentication = authenticate(authenticationDto.getEmail(), authenticationDto.getPassword());
        return ResponseEntity.ok(userAuthenticationService.logInUser(authentication));
    }

    private Authentication authenticate(String email, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}
