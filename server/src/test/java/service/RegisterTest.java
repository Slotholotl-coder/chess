package java.service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import service.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest extends ServiceTest {


    @Test
    void registerSuccess() throws DataAccessException {
        var authData = userService.register(new RegisterRequest("player1", "password", "email"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerFailure() {
        Exception exception = assertThrows(RuntimeException.class,
                () -> userService.register(new RegisterRequest("player1", "password", null)));
        assertEquals("Error: bad request", exception.getMessage());

    }

}