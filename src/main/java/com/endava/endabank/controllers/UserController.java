package com.endava.endabank.controllers;

import com.endava.endabank.constants.Permissions;
import com.endava.endabank.constants.Routes;
import com.endava.endabank.dto.user.UpdatePasswordDto;
import com.endava.endabank.dto.user.UserRegisterDto;
import com.endava.endabank.exceptions.customExceptions.ActionNotAllowedException;
import com.endava.endabank.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

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
    public ResponseEntity<?> getUsersToApprove() {

        return ResponseEntity.status(HttpStatus.OK).body(userService.usersToApprove());
    }

    @PutMapping(Routes.APPROVE_ACCOUNT_ROUTE)
    @PreAuthorize(Permissions.AUTHORITY_ACCOUNT_VALIDATE)
    public ResponseEntity<?> updateUserIsApproved(@PathVariable Integer id, @RequestBody Map<String, Boolean> map) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateApprove(id, map.get("value")));
    }

    @GetMapping(Routes.RESET_PASSWORD_ROUTE+"/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable String email) {
        Map<String, Object> map = userService.generateResetPassword(email);
        return ResponseEntity.status((HttpStatus) map.get("statusCode")).body(map);
    }

    @PutMapping(Routes.RESET_PASSWORD_ROUTE)
    public ResponseEntity<?> updateForgotPassword(@RequestBody UpdatePasswordDto updatePasswordDto) throws ActionNotAllowedException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateForgotPassword(updatePasswordDto));
    }

}
