package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void insertAuthToken(String authToken, String username);

    AuthData getAuthToken(String authToken) throws DataAccessException;

    void removeAuthToken(String authToken);

    void clear();
}