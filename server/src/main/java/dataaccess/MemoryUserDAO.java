package dataaccess;

import model.UserData;
import service.RegisterRequest;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements  UserDAO{

    private List<UserData> users = new ArrayList<>();

    @Override
    public void registerUser(RegisterRequest registerRequest) throws DataAccessException {

        for (UserData userData : users){
            if (userData.username().equals(registerRequest.username())){
                throw new DataAccessException("Error: Already Taken");
            }
        }

        users.add(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void removeUser(String username) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }

    @Override
    public boolean validUser(String username, String password) throws DataAccessException {
        return false;
    }
}
