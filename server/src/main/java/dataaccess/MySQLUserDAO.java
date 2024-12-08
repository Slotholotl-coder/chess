package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class MySQLUserDAO implements UserDAO {
    public static MySQLUserDAO instance;

    public MySQLUserDAO() {
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

    public static MySQLUserDAO getInstance(){
        if (instance == null) {
            instance = new MySQLUserDAO();
        }
        return instance;
    }

    @Override
    public void insertUser(UserData userData) throws DataAccessException {
        String INSERT_USER_SQL = "INSERT INTO users (username, password, email) VALUES (?, ?, ?);";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {

            preparedStatement.setString(1, userData.getUsername());
            preparedStatement.setString(2, hashPassword(userData.getUsername(),
                    userData.getPassword()));
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

    String hashPassword(String username, String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean validUser(String username, String password) throws DataAccessException {
        UserData userData = getUser(username);
        return Objects.equals(userData.getPassword(), readHashedPasswordFromDatabase(username));
    }

    void storeUserPassword(String username, String clearTextPassword) {
        String hashedPassword = hashPassword(username, clearTextPassword);

        // write the hashed password in database along with the user's other information
        writeHashedPasswordToDatabase(username, hashedPassword);
    }

    boolean passwordMatches(String username, String password){
        return BCrypt.checkpw(password, readHashedPasswordFromDatabase(username));
    }


    void writeHashedPasswordToDatabase(String username, String hashedPassword){
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE password_hash = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, hashedPassword);

            preparedStatement.executeUpdate();
            System.out.println("Password hash stored successfully for user: " + username);
        } catch (SQLException | DataAccessException e) {
            System.out.println("Error storing password hash: " + e.getMessage());
        }
    }

    public String readHashedPasswordFromDatabase(String username) {
        String sql = "SELECT password_hash FROM users WHERE username = ?";
        String hashedPassword = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    hashedPassword = rs.getString("password_hash");
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Error reading password hash: " + e.getMessage());
        }

        return hashedPassword;
    }
}