package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO = mySQLUserDAO.getInstance();
    private final GameDAO memoryGameDAO = mySQLGameDAO.getInstance();
    private final AuthDAO memoryAuthDAO = mySQLAuthDAO.getInstance();


    public AuthData login(UserData user) throws DataAccessException {

        UserData databaseUser = userDAO.getUser(user.getUsername());
        if (databaseUser != null && databaseUser.getPassword().equals(user.getPassword())) {
            String newAuthToken = generateAuthToken();
            memoryAuthDAO.insertAuthToken(newAuthToken, user.getUsername());
            return new AuthData(newAuthToken, user.getUsername());
        }
        throw new RuntimeException("Unauthorized");
    }

    public void logout(String authToken) throws DataAccessException {
        if (memoryAuthDAO.getAuthToken(authToken) == null) {
            throw new RuntimeException("unauthorized");
        }
        String username = memoryAuthDAO.getAuthToken(authToken).getUsername();
        memoryAuthDAO.removeAuthToken(authToken);
        userDAO.removeUser(username);
    }

    public AuthData register(UserData user) throws DataAccessException {
        if (!userDataIsValid(user)) {
            throw new RuntimeException("Error: bad request");
        }

        if (userDAO.getUser(user.getUsername()) != null) {
            throw new RuntimeException("Error: Already Taken");
        }

        userDAO.insertUser(user);
        AuthData authData = new AuthData(generateAuthToken(), user.getUsername());
        memoryAuthDAO.insertAuthToken(authData.getAuthToken(), authData.getUsername());
        return authData;
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    private boolean userDataIsValid(UserData user) {
        return user.getUsername() != null && !user.getUsername().isEmpty() &&
                user.getPassword() != null && !user.getPassword().isEmpty() &&
                user.getEmail() != null && !user.getEmail().isEmpty();
    }


    public void clear() throws DataAccessException {
        userDAO.clear();
        memoryGameDAO.clear();
        memoryAuthDAO.clear();
    }
}
