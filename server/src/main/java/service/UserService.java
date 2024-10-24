package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final MemoryUserDAO userDAO = MemoryUserDAO.getInstance();
    private final MemoryGameDAO memoryGameDAO = MemoryGameDAO.getInstance();
    private final MemoryAuthDAO memoryAuthDAO = MemoryAuthDAO.getInstance();

    public AuthData login(UserData user) {

        UserData databaseUser = userDAO.getUser(user.getUsername());
        if (databaseUser != null && databaseUser.getPassword().equals(user.getPassword())) {
            String newAuthToken = generateAuthToken();
            memoryAuthDAO.insertAuthToken(newAuthToken, user.getUsername());
            return new AuthData(newAuthToken, user.getUsername());
        }
        throw new RuntimeException("Unauthorized");
    }

    public void logout(String authToken) {
        if (memoryAuthDAO.getAuthToken(authToken) == null) {
            throw new RuntimeException("unauthorized");
        }
        String username = memoryAuthDAO.getAuthToken(authToken).getUsername();
        memoryAuthDAO.removeAuthToken(authToken);
        userDAO.removeUser(username);
    }

    public AuthData register(UserData user) {
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


    public void clear() {
        userDAO.clear();
        memoryGameDAO.clear();
        memoryAuthDAO.clear();
    }
}
