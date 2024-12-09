package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserTests {

    private MySQLUserDAO userDAO;
    private final String testUsername = "testUser";
    private final String testPassword = "testPassword";
    private final String testEmail = "test@example.com";

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = MySQLUserDAO.getInstance();
        userDAO.clear();
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        userDAO.clear();
    }

    @Test
    void testInsertUserPositive() throws DataAccessException {
        UserData userData = new UserData(testUsername, testPassword, testEmail);
        userDAO.insertUser(userData);
        UserData retrievedUser = userDAO.getUser(testUsername);
        assertNotNull(retrievedUser);
        assertEquals(testUsername, retrievedUser.getUsername());
        assertEquals(testEmail, retrievedUser.getEmail());
    }

    @Test
    void testInsertUserNegative() {
        UserData userData = new UserData(testUsername, testPassword, testEmail);
        assertDoesNotThrow(() -> userDAO.insertUser(userData));
        assertThrows(DataAccessException.class, () -> userDAO.insertUser(userData));
    }

    @Test
    void testGetUserPositive() throws DataAccessException {
        UserData userData1 = new UserData(testUsername, testPassword, testEmail);

        userDAO.insertUser(userData1);

        UserData retrievedUser1 = userDAO.getUser(testUsername);
        assertNotNull(retrievedUser1);
        assertEquals(testUsername, retrievedUser1.getUsername());
        assertEquals(testEmail, retrievedUser1.getEmail());
        assertTrue(BCrypt.checkpw(testPassword, retrievedUser1.getPassword()));
    }

    @Test
    void testGetUserNegative() throws DataAccessException {
        UserData retrievedUser = userDAO.getUser("nonexistentUser");
        assertNull(retrievedUser);
    }

    @Test
    void testRemoveUserPositive() throws DataAccessException {
        UserData userData = new UserData(testUsername, testPassword, testEmail);
        userDAO.insertUser(userData);
        userDAO.removeUser(testUsername);
        UserData retrievedUser = userDAO.getUser(testUsername);
        assertNull(retrievedUser);
    }

    @Test
    void testRemoveUserNegative() {
        assertDoesNotThrow(() -> userDAO.removeUser("nonexistentUser"));
    }

    @Test
    void testClearPositive() throws DataAccessException {
        UserData userData1 = new UserData("user1", "password1", "user1@example.com");
        UserData userData2 = new UserData("user2", "password2", "user2@example.com");
        userDAO.insertUser(userData1);
        userDAO.insertUser(userData2);
        userDAO.clear();
        assertNull(userDAO.getUser("user1"));
        assertNull(userDAO.getUser("user2"));
    }

}