package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ListGamesTest {
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
    void testListGamesSuccess() {

        AuthData authData = userService.register(new UserData("player1", "password", "player1@example.com"));
        int gameID = gameService.createGame(authData.getAuthToken(), "testGame");

        Collection<GameData> games = gameService.listGames(authData.getAuthToken());

        assertEquals(1, games.size());
        assertNull(games.iterator().next().getWhiteUsername());
        assertEquals(gameID, games.iterator().next().getGameID());
    }

    @Test
    void testListGamesFailure() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            AuthData authData = userService.register(new UserData("player1", "password", "player1@example.com"));
            gameService.createGame(authData.getAuthToken(), "testGame");

            gameService.listGames(authData.getAuthToken());
        });

        assertEquals("Error: Already Taken", exception.getMessage());
    }

}
