package service;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutTest {
    static MemoryAuthDAO memoryAuthDAO = MemoryAuthDAO.getInstance();
    static MemoryGameDAO memoryGameDAO = MemoryGameDAO.getInstance();
    static MemoryUserDAO memoryUserDAO = MemoryUserDAO.getInstance();
    private static UserService userService;

    @BeforeAll
    static void setUp() {
        userService = new UserService();

        memoryGameDAO.clear();
        memoryAuthDAO.clear();
        memoryUserDAO.clear();
    }

    @Test
    void logoutSuccess() throws Exception {
        AuthData authData = userService.register(new UserData("player1", "password", "p1@email.com"));
        userService.logout(authData.getAuthToken());
        assertNull(memoryAuthDAO.getAuthToken(authData.getAuthToken()));
    }

    @Test
    void logoutFailure() throws Exception {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            AuthData authData = userService.register(new UserData("player1", "password", null));
            userService.logout(null);
        });
        assertEquals("Error: bad request", exception.getMessage());
    }

}
