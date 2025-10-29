package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;

import java.util.UUID;

public class UserService {

    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        userDAO.registerUser(registerRequest);

        String authToken = generateToken();

        authDAO.insertAuthData(authToken, registerRequest.username());

        RegisterResult registerResult = new RegisterResult(registerRequest.username(), authToken);

        return registerResult;
    }


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
