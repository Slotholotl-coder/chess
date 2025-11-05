package dataaccess;

import model.RegisterRequest;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() {
        try { DatabaseManager.createDatabase(); }
        catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
        try (var conn = DatabaseManager.getConnection()) {
            var createTestTable = """            
                    CREATE TABLE if NOT EXISTS users (
                                    username VARCHAR(255) NOT NULL,
                                    password VARCHAR(255) NOT NULL,
                                    email VARCHAR(255),
                                    PRIMARY KEY (username)
                                    )""";
            try (var createTableStatement = conn.prepareStatement(createTestTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerUser(RegisterRequest registerRequest) throws DataAccessException {
        UserData userData = null;
        try {
            userData = getUser(registerRequest.username());
        } catch (DataAccessException e) {
            userData =null;
        }

        try (var conn = DatabaseManager.getConnection()) {
            try (var insertUserStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES(?, ?, ?)")) {
                insertUserStatement.setString(1, registerRequest.username());
                insertUserStatement.setString(2, registerRequest.password());
                insertUserStatement.setString(3, registerRequest.email());
                insertUserStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("User database error : " + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData userData = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var insertAuthStatement = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
                insertAuthStatement.setString(1, username);
                try(ResultSet resultSet = insertAuthStatement.executeQuery()){
                    if (resultSet.next()) {
                        String password = resultSet.getString("password");
                        String email = resultSet.getString("email");
                        userData = new UserData(username, password, email);
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("User Database Error : " + e.getMessage());
        }
        return userData;
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var clearStatement = conn.prepareStatement("DELETE * FROM users")) {
                clearStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("Error : deleting user database");
        }
    }
}
