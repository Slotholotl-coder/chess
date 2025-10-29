package dataaccess;

import model.UserData;
import model.RegisterRequest;

public interface UserDAO {
    void registerUser(RegisterRequest registerRequest) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void removeUser(String username) throws DataAccessException;

    void clear() throws DataAccessException;

    boolean validUser(String username, String password) throws DataAccessException;
}
