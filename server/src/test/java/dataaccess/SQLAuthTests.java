package dataaccess;

import model.AuthData;
import model.RegisterRequest;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SQLAuthTests {

    private SQLAuthDAO authDAO = new SQLAuthDAO();

    private String username = "username";
    private String password = "password";
    private String email = "email";
    private String authToken = "authToken";

    @AfterEach
    public void clear() throws DataAccessException {
        authDAO.clear();
    }

    @Test
    public void registerAuthPositive() throws DataAccessException {
        authDAO.insertAuthData(authToken, username);
        AuthData authData = authDAO.getAuthToken(authToken);
        assert authData != null;
    }

    @Test
    public void registerAuthNegative() throws DataAccessException {
        authDAO.insertAuthData(authToken, username);
        assertThrows(DataAccessException.class, () -> authDAO.insertAuthData(authToken, username));
    }

    @Test
    public void getAuthPositive() throws DataAccessException {
        authDAO.insertAuthData(authToken, username);
        AuthData authData = authDAO.getAuthToken(authToken);
        assert Objects.equals(authData.username(), username);
    }

    @Test
    public void getAuthNegative() throws DataAccessException {
        assertNull(authDAO.getAuthToken(authToken));
    }

    @Test
    public void clearDatabase() throws DataAccessException {
        authDAO.insertAuthData(authToken, username);
        authDAO.clear();
        assertNull(authDAO.getAuthToken(authToken));
    }

}