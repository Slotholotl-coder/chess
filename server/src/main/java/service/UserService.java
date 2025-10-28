package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;

public class UserService {

    private UserDAO userDAO;

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        userDAO.registerUser(registerRequest);

        RegisterResult registerResult = new RegisterResult(registerRequest.username(), "hello");

        return registerResult;
    }
}
