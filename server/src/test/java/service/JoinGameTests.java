package service;

import dataaccess.DataAccessException;
import model.CreateGameRequest;
import model.JoinGameRequest;
import model.LoginRequest;
import model.RegisterRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameTests extends ServiceTest {

    @Test
    void joinGameSuccess() throws DataAccessException {
        userService.register(new RegisterRequest("player1", "password", "email"));
        String authToken = userService.login(new LoginRequest("player1", "password")).authToken();
        gameService.createGame(authToken, new CreateGameRequest("alsdkjf"));
        gameService.joinGame(authToken, new JoinGameRequest("BLACK", 1));
        assertEquals(gameDAO.getGame(1).blackUsername(), "player1");
    }

    @Test
    void joinGameFailure() throws DataAccessException {

        userService.register(new RegisterRequest("player1", "password", "email"));
        String authToken = userService.login(new LoginRequest("player1", "password")).authToken();

        userService.register(new RegisterRequest("player2", "password", "email"));
        String authToken1 = userService.login(new LoginRequest("player1", "password")).authToken();

        gameService.createGame(authToken1, new CreateGameRequest("alsdkjf"));
        gameService.joinGame(authToken1, new JoinGameRequest("BLACK", 1));

        assertThrows(DataAccessException.class, () -> gameService.joinGame(authToken, new JoinGameRequest("BLACK", 1)));


    }

}
