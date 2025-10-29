package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;

import java.util.Objects;
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

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {

        if (userDAO.getUser(loginRequest.username()) != null){

            if (!Objects.equals(userDAO.getUser(loginRequest.username()).password(), loginRequest.password())){
                throw new DataAccessException("Incorrect password");
            }

            String authToken = generateToken();
            authDAO.insertAuthData(authToken, loginRequest.username());
            return new LoginResult(loginRequest.username(), authDAO.getAuthToken(authToken).authToken());
        }
        return null;
    }


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
