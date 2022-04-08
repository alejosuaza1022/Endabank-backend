package com.endava.endabank.security.utils;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.endava.endabank.exceptions.customExceptions.BadDataException;
import com.endava.endabank.model.User;
import com.endava.endabank.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtManageTest {

    private final JwtManage jwtManage = new JwtManage("ZHVtbXkgdmFsdWUK");

    @Test
    void generateValidTokenTest() throws BadDataException {
        User user = TestUtils.getUserAdmin();
        String token = jwtManage.generateToken(user.getId(), user.getEmail());
        assertNotNull(token);
        assertNotEquals("", token);
    }

    @Test
    void generateInvalidTokenTest() throws BadDataException {
        assertThrows(BadDataException.class, () ->
                jwtManage.generateToken(null, "a@a.com"));
        assertThrows(BadDataException.class, () ->
                jwtManage.generateToken(1, null));
        assertThrows(BadDataException.class, () ->
                jwtManage.generateToken(1, ""));
    }

    @Test
    void verifyValidToken() throws BadDataException {
        User user = TestUtils.getUserAdmin();
        String token = jwtManage.generateToken(user.getId(), user.getEmail());
        int idUser = jwtManage.verifyToken("Bearer " + token);
        assertEquals(idUser, user.getId());
    }
    @Test
    void verifyInvalidToken() throws BadDataException {
        User user = TestUtils.getUserAdmin();
        String token = jwtManage.generateToken(user.getId(), user.getEmail());
       assertThrows(JWTVerificationException.class, ()->
               jwtManage.verifyToken("Bearer " + token + "asd")
               );
    }
}