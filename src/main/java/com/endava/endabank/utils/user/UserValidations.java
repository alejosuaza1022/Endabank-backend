package com.endava.endabank.utils.user;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.exceptions.customExceptions.ActionNotAllowedException;
import com.endava.endabank.model.ForgotUserPasswordToken;
import com.endava.endabank.model.User;
import com.endava.endabank.security.utils.JwtManage;
import com.endava.endabank.service.ForgotUserPasswordTokenService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserValidations {

    public static int validateUserForgotPasswordToken(
            ForgotUserPasswordTokenService forgotUserPasswordTokenService, String token) {
        Integer userId = JwtManage.verifyToken("Bearer " + token, Strings.SECRET_JWT);
        ForgotUserPasswordToken tokenModel = forgotUserPasswordTokenService.findByUserId(userId);
        String tokenDb = tokenModel.getToken();
        if (!(tokenDb.equals(token))) {
            throw new AccessDeniedException(Strings.TOKEN_RESET_PASSWORD_INVALID);
        }
        tokenModel.setToken("");
        forgotUserPasswordTokenService.save(tokenModel);
        return userId;
    }

    public static void validateOldPassword(PasswordEncoder passwordEncoder,
                                           User user,
                                           String oldPassword
    ) throws ActionNotAllowedException {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ActionNotAllowedException(Strings.OLD_PASSWORD_NOT_MATCH);
        }
    }

    public static void comparePasswords(String password, String rePassword) throws ActionNotAllowedException {
        boolean passwordEquals = Objects.equals(password, rePassword);
        if (!passwordEquals) {
            throw new ActionNotAllowedException(Strings.PASSWORDS_DOESNOT_MATCH);
        }
    }
}
