package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class mySQLGameDAO implements GameDAO {
    public static mySQLGameDAO instance;

    public mySQLGameDAO() {
        try { DatabaseManager.createDatabase(); } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
        try (var conn = DatabaseManager.getConnection()) {
            var createTestTable = """            
                    CREATE TABLE if NOT EXISTS game (
                                    gameID INT NOT NULL,
                                    whiteUsername VARCHAR(255),
                                    blackUsername VARCHAR(255),
                                    gameName VARCHAR(255),
                                    chessGame TEXT,
                                    PRIMARY KEY (gameID)
                                    )""";
            try (var createTableStatement = conn.prepareStatement(createTestTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static mySQLGameDAO getInstance(){
        if (instance == null) {
            instance = new mySQLGameDAO();
        }
        return instance;
    }

    @Override
    public void insertGame(GameData game) {

    }

    @Override
    public GameData getGame(int gameNum) {
        return null;
    }

    @Override
    public Collection<GameData> getAllGames() {
        return List.of();
    }

    @Override
    public void clear() {

    }

    private String storeGame(ChessGame game) {
        return new Gson().toJson(game);
    }

    private ChessGame getGame(String serializedGame) {
        return new Gson().fromJson(serializedGame, ChessGame.class);
    }
}
