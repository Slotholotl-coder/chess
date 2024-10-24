package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void insertAuthToken(String authToken, String username);

    AuthData getAuthToken(String authToken);

    void removeAuthToken(String authToken);

    void clear();
}