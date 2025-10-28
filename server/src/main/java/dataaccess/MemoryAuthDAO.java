package dataaccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO{

    @Override
    public void insertAuthToken(String authToken, String username) throws DataAccessException {
        System.out.println("Not implemented");
    }

    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        System.out.println("Not implemented");
        return null;
    }

    @Override
    public void removeAuthToken(String authToken) throws DataAccessException {
        System.out.println("Not implemented");
    }

    @Override
    public void clear() {
        System.out.println("Not implemented");
    }
}
