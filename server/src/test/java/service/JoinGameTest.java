package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JoinGameTest {
    static MemoryAuthDAO memoryAuthDAO = MemoryAuthDAO.getInstance();
    static MemoryGameDAO memoryGameDAO = MemoryGameDAO.getInstance();
    static MemoryUserDAO memoryUserDAO = MemoryUserDAO.getInstance();
    private static UserService userService;
    private static GameService gameService;

    @BeforeAll
    static void setUp() {
        gameService = new GameService();
        userService = new UserService();

        memoryGameDAO.clear();
        memoryAuthDAO.clear();
        memoryUserDAO.clear();

        userService.register(new UserData("testUser", "testPass", "test@example.com"));

    }

    @Test
    void testJoinGameSuccess() {
        AuthData authData = userService.login(new UserData("testUser", "testPass", null));
        gameService.createGame(authData.getAuthToken(), "testGame");
        gameService.joinGame(authData.getAuthToken(), 1, "WHITE");
        assertEquals(memoryGameDAO.getGame(1).getWhiteUsername(), "testUser");
    }

    @Test
    void testJoinGameFailure() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            AuthData authData = userService.login(new UserData("testUser", "testPass", null));
            gameService.createGame("asdlkfj", "testGame");
        });

        assertEquals("unauthorized", exception.getMessage());
    }
}
