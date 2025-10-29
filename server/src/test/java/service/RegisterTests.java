package service;

import dataaccess.DataAccessException;
import model.RegisterRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTests extends ServiceTest {

    @Test
    void registerSuccess() throws DataAccessException {
        var authData = userService.register(new RegisterRequest("player1", "password", "p1@email.com"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerFailure() throws DataAccessException {
        registerSuccess();
        Exception exception = assertThrows(DataAccessException.class,
                () -> userService.register(new RegisterRequest("player1", "password", null)));
        assertEquals("Error: Already Taken", exception.getMessage());

    }
}