package dataaccess;

import model.AuthData;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() {
        try { DatabaseManager.createDatabase(); } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
        try (var conn = DatabaseManager.getConnection()) {
            var createTestTable = """            
                    CREATE TABLE if NOT EXISTS games (
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

    @Override
    public void insertGame(GameData game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var insertAuthStatement = conn.prepareStatement("INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES(?, ?, ?, ?, ?)")) {
                insertAuthStatement.setString(1, String.valueOf(game.gameID()));
                insertAuthStatement.setString(2, game.whiteUsername());
                insertAuthStatement.setString(3, game.blackUsername());
                insertAuthStatement.setString(4, game.gameName());
                insertAuthStatement.setString(5, game.game());
                insertAuthStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("Authorization database error : " + e.getMessage());
        }
    }

    @Override
    public GameData getGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var getAuthStatement = conn.prepareStatement("SELECT * FROM games WHERE gameID = ?")) {
                getAuthStatement.setString(1, String.valueOf(gameID));
                try(ResultSet resultSet = getAuthStatement.executeQuery()){
                    if (resultSet.next()) {
                        var whiteUsername = resultSet.getString("whiteUsername");
                        var blackUsername = resultSet.getString("blackUsername");
                        var gameName = resultSet.getString("gameName");
                        var chessGame = deserializeGame(resultSet.getString("chessGame"));
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("Error : Not Authorized");
        }
        throw new DataAccessException("Error : Game Not Found");
    }

    @Override
    public Collection<GameData> getAllGames() {
        return List.of();
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var clearStatement = conn.prepareStatement("DELETE FROM games")) {
                clearStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
