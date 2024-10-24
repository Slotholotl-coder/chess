package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ClearTest {
    private static UserService userService;
    private static GameService gameService;
    MemoryAuthDAO memoryAuthDAO = MemoryAuthDAO.getInstance();
    MemoryGameDAO memoryGameDAO = MemoryGameDAO.getInstance();
    MemoryUserDAO memoryUserDAO = MemoryUserDAO.getInstance();

    @BeforeAll
    static void setUp() {
        userService = new UserService();
        AuthData authData = userService.register(new UserData("testUser", "testPass", "test@example.com"));

        gameService = new GameService();
        gameService.createGame(authData.getAuthToken(), "testGame");
    }

    @Test
    void clearTest() {
        userService.clear();
        assert (memoryAuthDAO.getNumberOfAuthTokens() == 0 && memoryUserDAO.getNumberOfUsers() == 0
                && memoryGameDAO.getNumberOfGames() == 0);
    }

}
