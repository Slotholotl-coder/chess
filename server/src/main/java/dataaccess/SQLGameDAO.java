package dataaccess;

import model.AuthData;

public class SQLGameDAO implements AuthDAO {
    @Override
    public void insertAuthData(String authToken, String username) throws DataAccessException {

    }

    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void removeAuthToken(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() {

    }
}
