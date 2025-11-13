package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO{

    List<AuthData> authDataList = new ArrayList<>();

    @Override
    public void insertAuthData(String authToken, String username) throws DataAccessException {
        authDataList.add(new AuthData(authToken, username));
    }

    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        for (AuthData authData : authDataList){
            if (Objects.equals(authData.authToken(), authToken)){
                return authData;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }

    @Override
    public void removeAuthToken(String authToken) throws DataAccessException {
        authDataList.removeIf(authData -> Objects.equals(authData.authToken(), authToken));
    }

    @Override
    public void clear() {
        authDataList.clear();
    }
}