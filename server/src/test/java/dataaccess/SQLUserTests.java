package dataaccess;

import model.RegisterRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SQLUserTests {

    private SQLUserDAO userDAO = new SQLUserDAO();

    private String username = "username";
    private String password = "password";
    private String email = "email";

    @AfterEach
    public void clear() throws DataAccessException {
        userDAO.clear();
    }

    @Test
    public void registerUserPositive() throws DataAccessException {
        userDAO.registerUser(new RegisterRequest(username, password, email));
        UserData userData = userDAO.getUser(username);
        assert userData != null;
    }

    @Test
    public void registerUserPositive1() throws DataAccessException {
        userDAO.registerUser(new RegisterRequest(username, password, email));
        UserData userData = userDAO.getUser(username);
        assert Objects.equals(userData.username(), username);
    }

    @Test
    public void registerUserNegative() throws DataAccessException {
        userDAO.registerUser(new RegisterRequest(username, password, email));
        assertThrows(DataAccessException.class, () -> userDAO.registerUser(new RegisterRequest(username, password, email)));
    }

    @Test
    public void registerUserNegative1() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> userDAO.registerUser(new RegisterRequest(username, null, email)));
    }

    @Test
    public void getUserPositive() throws DataAccessException {
        userDAO.registerUser(new RegisterRequest(username, password, email));
        UserData userData = userDAO.getUser(username);
        assert Objects.equals(userData.username(), username);
    }

    @Test
    public void getUserNegative() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> userDAO.getUser(username));
    }

    @Test
    public void clearDatabase() throws DataAccessException {
        userDAO.registerUser(new RegisterRequest(username, password, email));
        userDAO.clear();
        assertThrows(DataAccessException.class, () -> userDAO.getUser(username));
    }

}
