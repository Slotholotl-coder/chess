package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

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

        String hashedPassword = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
        RegisterRequest hashPWRegisterRequest = new RegisterRequest(registerRequest.username(), hashedPassword, registerRequest.email());

        userDAO.registerUser(hashPWRegisterRequest);

        String authToken = generateToken();

        authDAO.insertAuthData(authToken, hashPWRegisterRequest.username());

        RegisterResult registerResult = new RegisterResult(hashPWRegisterRequest.username(), authToken);

        return registerResult;
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {

        if (userDAO.getUser(loginRequest.username()) != null){

            String hashedPassword = userDAO.getUser(loginRequest.username()).password();

            if (!verifyUser(hashedPassword, loginRequest.password())){
                throw new DataAccessException("Incorrect password");
            }

            String authToken = generateToken();
            authDAO.insertAuthData(authToken, loginRequest.username());
            return new LoginResult(loginRequest.username(), authDAO.getAuthToken(authToken).authToken());
        }
        return null;
    }

    private boolean verifyUser(String hashedPassword, String providedClearTextPassword) {

        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        authDAO.getAuthToken(logoutRequest.authToken());
        authDAO.removeAuthToken(logoutRequest.authToken());
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
