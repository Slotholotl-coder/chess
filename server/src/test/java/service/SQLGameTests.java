package service;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import dataaccess.MySQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameTests {
    private MySQLGameDAO gameDAO;
    private GameData sampleGame;

    @BeforeEach
    void initialize() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        gameDAO = new MySQLGameDAO();
        emptyGameTable();

        ChessGame chessGame = new ChessGame();
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        chessGame.setBoard(chessBoard);

        sampleGame = new GameData(9876, "playerWhite", "playerBlack", "TestGame", chessGame);
    }

    @AfterEach
    void cleanUp() throws SQLException, DataAccessException {
        emptyGameTable();
    }

    private void emptyGameTable() throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement("DELETE FROM game")) {
            stmt.executeUpdate();
        }
    }

    @Test
    void testAddGame() throws DataAccessException, SQLException {
        gameDAO.insertGame(sampleGame);

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement("SELECT * FROM game WHERE gameID = ?")) {
            stmt.setInt(1, sampleGame.getGameID());
            var rs = stmt.executeQuery();

            assertTrue(rs.next());
            assertEquals(sampleGame.getGameID(), rs.getInt("gameID"));
            assertEquals(sampleGame.getWhiteUsername(), rs.getString("whiteUsername"));
            assertEquals(sampleGame.getBlackUsername(), rs.getString("blackUsername"));
            assertEquals(sampleGame.getGameName(), rs.getString("gameName"));

            ChessGame retrievedGame = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);
            assertEquals(sampleGame.getGame().getBoard(),
                    retrievedGame.getBoard());
        }
    }

    @Test
    void testAddDuplicateGame() throws DataAccessException {
        gameDAO.insertGame(sampleGame);
        assertThrows(DataAccessException.class, () -> gameDAO.insertGame(sampleGame));
    }

    @Test
    void testRetrieveAllGames() throws DataAccessException {
        gameDAO.insertGame(sampleGame);
        gameDAO.insertGame(new GameData(5678, "player1", "player2", "AnotherGame", new ChessGame()));

        Collection<GameData> games = gameDAO.getAllGames();
        assertEquals(2, games.size());
    }

    @Test
    void testRetrieveEmptyGameList() {
        Collection<GameData> games = gameDAO.getAllGames();
        assertTrue(games.isEmpty());
    }

    @Test
    void testFetchExistingGame() throws DataAccessException {
        gameDAO.insertGame(sampleGame);
        GameData fetchedGame = gameDAO.getGame(sampleGame.getGameID());
        assertEquals(sampleGame.getGameID(), fetchedGame.getGameID());
    }

    @Test
    void testFetchNonexistentGame() {
        assertThrows(DataAccessException.class, () -> gameDAO.getGame(9999));
    }

    @Test
    void testGamePresence() throws DataAccessException {
        gameDAO.insertGame(sampleGame);
        assertNotNull(gameDAO.getGame(sampleGame.getGameID()));
    }

    @Test
    void testModifyGame() throws DataAccessException {
        gameDAO.insertGame(sampleGame);
        GameData modifiedGame = new GameData(sampleGame.getGameID(), "newWhitePlayer",
                sampleGame.getBlackUsername(), sampleGame.getGameName(), sampleGame.getGame());
        gameDAO.updateGame(modifiedGame);

        GameData retrievedGame = gameDAO.getGame(sampleGame.getGameID());
        assertEquals(modifiedGame.getGameName(), retrievedGame.getGameName());
    }

    @Test
    void testClearGames() throws DataAccessException, SQLException {
        gameDAO.insertGame(sampleGame);
        gameDAO.clear();

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement("SELECT COUNT(*) FROM game")) {
            var rs = stmt.executeQuery();
            rs.next();
            assertEquals(0, rs.getInt(1));
        }
    }
}