package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
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
    void registerSuccess() throws Exception {
        var authData = userService.register(new UserData("player1", "password", "p1@email.com"));
        assertTrue(authData.getAuthToken().length() > 10);
    }

    @Test
    void registerFailure() throws Exception {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            var authData = userService.register(new UserData("player1", "password", null));
        });
        assertEquals("Error: bad request", exception.getMessage());
    }
}
