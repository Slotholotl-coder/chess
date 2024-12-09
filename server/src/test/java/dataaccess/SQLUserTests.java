package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testInsertUser_Positive() throws DataAccessException {
        UserData userData = new UserData(testUsername, testPassword, testEmail);
        userDAO.insertUser(userData);
        UserData retrievedUser = userDAO.getUser(testUsername);
        assertNotNull(retrievedUser);
        assertEquals(testUsername, retrievedUser.getUsername());
        assertEquals(testEmail, retrievedUser.getEmail());
    }

    @Test
    void testInsertUser_Negative() {
        UserData userData = new UserData(testUsername, testPassword, testEmail);
        assertDoesNotThrow(() -> userDAO.insertUser(userData));
        assertThrows(DataAccessException.class, () -> userDAO.insertUser(userData));
    }

    @Test
    void testGetUser_Positive() throws DataAccessException {
        UserData userData = new UserData(testUsername, testPassword, testEmail);
        userDAO.insertUser(userData);
        UserData retrievedUser = userDAO.getUser(testUsername);
        assertNotNull(retrievedUser);
        assertEquals(testUsername, retrievedUser.getUsername());
        assertEquals(testEmail, retrievedUser.getEmail());
    }

    @Test
    void testGetUser_Negative() throws DataAccessException {
        UserData retrievedUser = userDAO.getUser("nonexistentUser");
        assertNull(retrievedUser);
    }

    @Test
    void testRemoveUser_Positive() throws DataAccessException {
        UserData userData = new UserData(testUsername, testPassword, testEmail);
        userDAO.insertUser(userData);
        userDAO.removeUser(testUsername);
        UserData retrievedUser = userDAO.getUser(testUsername);
        assertNull(retrievedUser);
    }

    @Test
    void testRemoveUser_Negative() {
        assertDoesNotThrow(() -> userDAO.removeUser("nonexistentUser"));
    }

    @Test
    void testClear_Positive() throws DataAccessException {
        UserData userData1 = new UserData("user1", "password1", "user1@example.com");
        UserData userData2 = new UserData("user2", "password2", "user2@example.com");
        userDAO.insertUser(userData1);
        userDAO.insertUser(userData2);
        userDAO.clear();
        assertNull(userDAO.getUser("user1"));
        assertNull(userDAO.getUser("user2"));
    }

}