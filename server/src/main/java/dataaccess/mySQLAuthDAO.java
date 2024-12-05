package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class mySQLAuthDAO implements AuthDAO{
    public static mySQLAuthDAO instance;

    public mySQLAuthDAO() {
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

    public static mySQLAuthDAO getInstance(){
        if (instance == null){
            instance = new mySQLAuthDAO();
        }
        return instance;
    }

    @Override
    public void insertAuthToken(String authToken, String username) {

    }

    @Override
    public AuthData getAuthToken(String authToken) {
        return null;
    }

    @Override
    public void removeAuthToken(String authToken) {

    }

    @Override
    public void clear() {

    }
}
