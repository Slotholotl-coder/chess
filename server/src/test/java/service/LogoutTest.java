package service;

import dataaccess.DataAccessException;
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
    void logoutSuccess() throws DataAccessException {
        AuthData authData = userService.register(new UserData("player1", "password", "p1@email" +
                ".com"));
        userService.logout(authData.getAuthToken());
        assertNull(memoryAuthDAO.getAuthToken(authData.getAuthToken()));
    }

    @Test
    void logoutFailure() {
        Exception exception = assertThrows(RuntimeException.class, () -> userService.logout(null));
        assertEquals("unauthorized", exception.getMessage());
    }

}
