package service;

import dataaccess.DataAccessException;
import model.CreateGameRequest;
import model.CreateGameResult;
import model.RegisterRequest;
import model.RegisterResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameTests extends ServiceTest {

    @Test
    void createGameSuccess() throws DataAccessException {

        RegisterResult registerResult = userService.register(new RegisterRequest("playeraksjdfh", "ajls;djkf", "asdlfkj"));

        CreateGameResult createGameResult = gameService.createGame(registerResult.authToken(), new CreateGameRequest("asldfj", ";aslkdjf"));

        assertNotNull(gameDAO.getAllGames());
    }

    @Test
    void createGameFailure() {
        Exception exception = assertThrows(DataAccessException.class, () -> {
            RegisterResult registerResult = userService.register(new RegisterRequest("playeraksjdfh", "ajls;djkf", "asdlfkj"));

            CreateGameResult createGameResult = gameService.createGame("a;lwkejflkw", new CreateGameRequest("a;lwjefns", "alsdjkf"));
        });

        assertEquals("Error: unauthorized", exception.getMessage());
    }

}
