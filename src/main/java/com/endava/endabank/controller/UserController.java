package com.endava.endabank.controller;

import com.endava.endabank.constants.Permissions;
import com.endava.endabank.constants.Routes;
import com.endava.endabank.dto.user.UpdatePasswordDto;
import com.endava.endabank.dto.user.UserPrincipalSecurity;
import com.endava.endabank.dto.user.UserRegisterDto;
import com.endava.endabank.exceptions.customExceptions.ActionNotAllowedException;
import com.endava.endabank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping(Routes.API_ROUTE + Routes.USERS_ROUTE)
@AllArgsConstructor
public class UserController {
    private final UserService userService;

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

    @GetMapping(Routes.RESET_PASSWORD_ROUTE + "/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable String email) {
        Map<String, Object> map = userService.generateResetPassword(email);
        return ResponseEntity.status((HttpStatus) map.get("statusCode")).body(map);
    }

    @PutMapping(Routes.RESET_PASSWORD_ROUTE)
    public ResponseEntity<?> updateForgotPassword(@RequestBody UpdatePasswordDto updatePasswordDto) throws ActionNotAllowedException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateForgotPassword(updatePasswordDto));
    }

    @CrossOrigin(origins = "http://localhost:3000/")
    @PutMapping(Routes.UPDATE_PASSWORD)
    public ResponseEntity<?> updatePassword(Principal principal, @RequestBody UpdatePasswordDto updatePasswordDto) throws ActionNotAllowedException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        UserPrincipalSecurity user = (UserPrincipalSecurity) usernamePasswordAuthenticationToken.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(userService.updatePassword(user, updatePasswordDto));
    }

}
