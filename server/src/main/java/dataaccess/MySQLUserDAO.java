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
        String inserUserSql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?);";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(inserUserSql)) {

            preparedStatement.setString(1, userData.getUsername());
            preparedStatement.setString(2, hashPassword(userData.getPassword()));
            preparedStatement.setString(3, userData.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting user: " + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String selectUserSql = "SELECT * FROM users WHERE username = ?;";
        UserData userData = null;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectUserSql)) {

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
        String deleteUserSql = "DELETE FROM users WHERE username = ?;";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteUserSql)) {

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

    String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

        @Override
        public boolean validUser(String username, String providedClearTextPassword)
                throws DataAccessException {
            // read the previously hashed password from the database
            var hashedPassword = readHashedPasswordFromDatabase(username);

            if(hashedPassword != null && BCrypt.checkpw(providedClearTextPassword, hashedPassword)){
                return true;
            }
            else{
                throw new DataAccessException("Invalid Password");
            }
        }


    public String readHashedPasswordFromDatabase(String username) throws DataAccessException {
        String sql = "SELECT password FROM users WHERE username = ?";
        String hashedPassword = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    hashedPassword = rs.getString("password");
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error reading hashed passwords : " + e.getMessage());
        }

        return hashedPassword;
    }
}