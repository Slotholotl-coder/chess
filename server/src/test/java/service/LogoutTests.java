package service;

import dataaccess.DataAccessException;
import model.LogoutRequest;
import model.RegisterRequest;
import model.RegisterResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutTests extends ServiceTest {

    @Test
    void logoutSuccess() throws DataAccessException {
        RegisterResult registerResult = userService.register(new RegisterRequest("player1", "password", "email"));
        userService.logout(new LogoutRequest(registerResult.authToken()));
        assertThrows(DataAccessException.class, () -> authDAO.getAuthToken(registerResult.authToken()));
    }

    @Test
    void logoutFailure() {
        Exception exception = assertThrows(DataAccessException.class, () -> userService.logout(new LogoutRequest("alsdkjfalsk")));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

}
