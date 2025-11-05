package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.RegisterRequest;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SQLGameTests {

    private SQLGameDAO gameDAO = new SQLGameDAO();

    private String username = "username";
    private String password = "password";
    private String email = "email";

    @AfterEach
    public void clear() throws DataAccessException {
        gameDAO.clear();
    }

    @Test
    public void registerGamePositive() throws DataAccessException {
        gameDAO.insertGame(new GameData(1, username, username, username, new ChessGame()));
        GameData gameData = gameDAO.getGame(1);
        assert gameData != null;
    }

    @Test
    public void registerGamePositive1() throws DataAccessException {
        gameDAO.insertGame(new GameData(1, username, username, username, new ChessGame()));
        GameData gameData = gameDAO.getGame(1);
        assert Objects.equals(gameData.gameName(), username);
    }

    @Test
    public void registerGameNegative() throws DataAccessException {
        gameDAO.insertGame(new GameData(1, username, username, username, new ChessGame()));
        assertThrows(DataAccessException.class, () ->
                gameDAO.insertGame(new GameData(1, username, username, username, new ChessGame())));
    }

    @Test
    public void registerGameNegative1() throws DataAccessException {
        gameDAO.insertGame(new GameData(4, username, username, username, new ChessGame()));
        assertThrows(DataAccessException.class, () ->
                gameDAO.insertGame(new GameData(4, username, username, username, new ChessGame())));
    }

    @Test
    public void getGamePositive() throws DataAccessException {
        gameDAO.insertGame(new GameData(1, username, username, username, new ChessGame()));
        GameData gameData = gameDAO.getGame(1);
        assert Objects.equals(gameData.whiteUsername(), username);
    }

    @Test
    public void getGameNegative() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> gameDAO.getGame(1));
    }

    @Test
    public void clearDatabase() throws DataAccessException {
        gameDAO.insertGame(new GameData(1, username, username, username, new ChessGame()));
        gameDAO.clear();
        assertThrows(DataAccessException.class, () -> gameDAO.getGame(1));
    }

}
