package service;

import dataaccess.DataAccessException;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTests extends ServiceTest {

    @Test
    void loginSuccess() throws DataAccessException {
        userService.register(new RegisterRequest("player1", "password", "email"));
        String authToken = userService.login(new LoginRequest("player1", "password")).authToken();
        assertEquals("player1", authDAO.getAuthToken(authToken).username());
    }

    @Test
    void loginFailure() throws DataAccessException {
        userService.register(new RegisterRequest("player1", "password", "email"));
        java.lang.Exception exception = assertThrows(DataAccessException.class, ()-> userService.login(new LoginRequest("player1", "asldfjk;")));
    }

}