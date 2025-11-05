package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO (){
        try { DatabaseManager.createDatabase(); } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
        try (var conn = DatabaseManager.getConnection()) {
            var createTestTable = """            
                    CREATE TABLE if NOT EXISTS auth (
                                    username VARCHAR(255) NOT NULL,
                                    authToken VARCHAR(255) NOT NULL,
                                    PRIMARY KEY (authToken)
                                    )""";
            try (var createTableStatement = conn.prepareStatement(createTestTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertAuthData(String authToken, String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var insertAuthStatement = conn.prepareStatement("INSERT INTO auth (username, authToken) VALUES(?, ?)")) {
                insertAuthStatement.setString(1, username);
                insertAuthStatement.setString(2, authToken);
                insertAuthStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("Authorization database error : " + e.getMessage());
        }
    }

    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        AuthData authData = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var getAuthStatement = conn.prepareStatement("SELECT * FROM auth WHERE authToken = ?")) {
                getAuthStatement.setString(1, authToken);
                try(ResultSet resultSet = getAuthStatement.executeQuery()){
                    if (resultSet.next()) {
                        String username = resultSet.getString("username");
                        authData = new AuthData(authToken, username);
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("Error : Not Authorized");
        }
        return authData;
    }

    @Override
    public void removeAuthToken(String authToken) throws DataAccessException {
        UserData userData = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var clearAuthDatabase = conn.prepareStatement("DELETE FROM auth WHERE authToken = ?")) {
                clearAuthDatabase.setString(1, authToken);
                clearAuthDatabase.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("Authorization database error : " + e.getMessage());
        }
    }

    @Override
    public void clear() {
        UserData userData = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var clearAuthDatabase = conn.prepareStatement("DELETE * FROM auth")) {
                clearAuthDatabase.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException();
        }
    }
}
