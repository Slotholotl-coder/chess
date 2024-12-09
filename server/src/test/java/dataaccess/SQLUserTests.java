package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserTests {

    UserData testUser;
    MySQLUserDAO userDAO;

    @BeforeEach
    void init() throws DataAccessException, SQLException {
        clearUserTable();
        DatabaseManager.createDatabase();
        userDAO = MySQLUserDAO.getInstance();
        clearUserTable();
        testUser = new UserData("testUser", "testPass", "test@example.com");
    }

    @AfterEach
    public void clearUserTable() throws SQLException {
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement("DELETE FROM user")) {
            stmt.executeUpdate();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createUserSuccessfully() throws DataAccessException, SQLException {
        clearUserTable();
        userDAO.insertUser(new UserData("pablo", "ajefio", "eaklfj"));

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            stmt.setString(1, testUser.getUsername());
            var rs = stmt.executeQuery();

            assertTrue(rs.next());
            assertEquals(testUser.getUsername(), rs.getString("username"));
            assertEquals(testUser.getEmail(), rs.getString("email"));
            assertTrue(passwordMatches("testUser", "testPass"));
        }
    }

    boolean passwordMatches(String username, String password) throws DataAccessException {
        return BCrypt.checkpw(password, userDAO.readHashedPasswordFromDatabase(username));
    }

    @Test
    void getUserExistingUser() throws DataAccessException {

        UserData retrievedUser = userDAO.getUser(testUser.getUsername());

        assertNotNull(retrievedUser);
        assertEquals(testUser.getUsername(), retrievedUser.getUsername());
        assertEquals(testUser.getEmail(), retrievedUser.getEmail());
    }

    @Test
    void getUserNonExistentUser() throws DataAccessException {
        assertNull(userDAO.getUser("nonexistentUser"));
    }

}