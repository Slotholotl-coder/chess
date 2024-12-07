package service;

import dataaccess.DataAccessException;
import dataaccess.MySQLAuthDAO;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthTests {

    MySQLAuthDAO mySQLAuthDAO;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        DatabaseManager.getConnection();
        mySQLAuthDAO = MySQLAuthDAO.getInstance();
        // Clear the auth table before each test
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE auth")) {
                statement.executeUpdate();
            }
        }}

    //Pos
    @Test
    void testInsertAuthToken() throws DataAccessException {
        AuthData test = new AuthData("test", "token1");

        mySQLAuthDAO.insertAuthToken(test.getAuthToken(), "test");

        assertNotNull(mySQLAuthDAO.getAuthToken("test"));
    }

    //Neg
    @Test
    void testDeleteNonExistentAuth() throws DataAccessException {
        AuthData test = new AuthData("test", "testToken");
        mySQLAuthDAO.insertAuthToken(test.getAuthToken(), test.getUsername());

        assertThrows(RuntimeException.class, () -> mySQLAuthDAO.getAuthToken("invalidAuthToken"));
    }

    @Test
    void testGetAuth() throws DataAccessException {
        AuthData test = new AuthData("test", "token1");

        mySQLAuthDAO.insertAuthToken(test.getAuthToken(), "test");

        assertEquals(test, mySQLAuthDAO.getAuthToken("token1"));
    }

    @Test
    void testRemoveAuth() throws DataAccessException {
        AuthData test = new AuthData("test", "token1");

        mySQLAuthDAO.insertAuthToken(test.getAuthToken(), "test");

        mySQLAuthDAO.removeAuthToken(test.getAuthToken());

        assertThrows(RuntimeException.class, () -> mySQLAuthDAO.getAuthToken("token1"));

    }

    @Test
    void testClear() throws DataAccessException {
        AuthData test = new AuthData("test", "token1");

        mySQLAuthDAO.insertAuthToken(test.getAuthToken(), "test");

        mySQLAuthDAO.clear();

        assertThrows(RuntimeException.class, () -> mySQLAuthDAO.getAuthToken("token1"));

    }

}