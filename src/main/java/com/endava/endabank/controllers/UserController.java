package com.endava.endabank.controllers;

import com.endava.endabank.constants.Permissions;
import com.endava.endabank.constants.Routes;
import com.endava.endabank.dto.user.UserRegisterDto;
import com.endava.endabank.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(Routes.API_ROUTE + Routes.USERS_ROUTE)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserRegisterDto user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.save(user));
    }

    @GetMapping()
    @PreAuthorize(Permissions.AUTHORITY_ACCOUNT_VALIDATE)
    public ResponseEntity<?> get(Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body("hello world");
    }
}
