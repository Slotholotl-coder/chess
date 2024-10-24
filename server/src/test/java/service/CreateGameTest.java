package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameTest {
    static MemoryAuthDAO memoryAuthDAO = MemoryAuthDAO.getInstance();
    static MemoryGameDAO memoryGameDAO = MemoryGameDAO.getInstance();
    static MemoryUserDAO memoryUserDAO = MemoryUserDAO.getInstance();
    private static UserService userService;
    private static GameService gameService;

    @BeforeAll
    static void setUp() {
        userService = new UserService();
        gameService = new GameService();

        memoryGameDAO.clear();
        memoryAuthDAO.clear();
        memoryUserDAO.clear();
    }

    @Test
    void testCreateGameSuccess() {

        AuthData authData = userService.register(new UserData("player1", "password", "player1" +
                "@example.com"));


        int response = gameService.createGame(authData.getAuthToken(), "testGame");

        assertNotNull(memoryGameDAO.getGame(response));
    }

    @Test
    void testCreateGameFailure() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.register(new UserData("player2", "password2", "player2@example.com"));
            gameService.createGame("jaldksf", "testGame");
        });

        assertEquals("unauthorized", exception.getMessage());
    }
}
