package dataaccess;

import model.RegisterRequest;
import model.UserData;

public class SQLUserDAO implements UserDAO {
    @Override
    public void registerUser(RegisterRequest registerRequest) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
