package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class mySQLUserDAO implements UserDAO {
    public static mySQLUserDAO instance;

    public mySQLUserDAO() {
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
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static mySQLUserDAO getInstance(){
        if (instance == null) {
            instance = new mySQLUserDAO();
        }
        return instance;
    }

    @Override
    public void insertUser(UserData userData) throws DataAccessException {
        String INSERT_USER_SQL = "INSERT INTO users (username, password, email) VALUES (?, ?, ?);";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {

            preparedStatement.setString(1, userData.getUsername());
            preparedStatement.setString(2, userData.getPassword()); // Consider hashing the password
            preparedStatement.setString(3, userData.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting user: " + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String SELECT_USER_SQL = "SELECT * FROM users WHERE username = ?;";
        UserData userData = null;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_SQL)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                userData = new UserData(username, password, email);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving user: " + e.getMessage());
        }
        return userData;
    }

    @Override
    public void removeUser(String username) throws DataAccessException {
        String DELETE_USER_SQL = "DELETE FROM users WHERE username = ?;";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL)) {

            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error removing user: " + e.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        String CLEAR_USERS_SQL = "DELETE FROM users;";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CLEAR_USERS_SQL)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing users: " + e.getMessage());
        }
    }

    void storeUserPassword(String username, String clearTextPassword) {
        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());

        // write the hashed password in database along with the user's other information
        writeHashedPasswordToDatabase(username, hashedPassword);
    }

    boolean verifyUser(String username, String providedClearTextPassword) {
        // read the previously hashed password from the database
        var hashedPassword = readHashedPasswordFromDatabase(username);

        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }

}