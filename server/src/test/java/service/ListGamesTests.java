package service;

import dataaccess.DataAccessException;
import model.*;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ListGamesTests extends ServiceTest{

    @Test
    void testListGamesSuccess() throws DataAccessException {

        RegisterResult registerResult = userService.register(new RegisterRequest("player1", "password", "email"));
        gameService.createGame(registerResult.authToken(), new CreateGameRequest("asldkfj"));

        Collection<GameData> games = gameService.listGames(new ListGamesRequest(registerResult.authToken()));

        assertEquals(1, games.size());
    }

    @Test
    void testListGamesFailure() {
        Exception exception = assertThrows(DataAccessException.class, () -> {
            RegisterResult registerResult = userService.register(new RegisterRequest("player1", "password", "email"));
            gameService.createGame(registerResult.authToken(), new CreateGameRequest("asldkfj"));

            Collection<GameData> games = gameService.listGames(new ListGamesRequest("alsdkjf"));
        });
    }

}
