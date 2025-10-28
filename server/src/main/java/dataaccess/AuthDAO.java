package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void insertAuthToken(String authToken, String username) throws DataAccessException;

    AuthData getAuthToken(String authToken) throws DataAccessException;

    void removeAuthToken(String authToken) throws DataAccessException;

    void clear();
}
