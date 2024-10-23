package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private MemoryUserDAO userDAO = MemoryUserDAO.getInstance();
    private MemoryGameDAO memoryGameDAO = MemoryGameDAO.getInstance();
    private MemoryAuthDAO memoryAuthDAO = MemoryAuthDAO.getInstance();
    private String currentAuthToken;

    public AuthData login(UserData user){

        UserData databaseUser = userDAO.getUser(user.getUsername());
        if (databaseUser != null && databaseUser.getPassword().equals(user.getPassword())){
            currentAuthToken = generateAuthToken();
            memoryAuthDAO.insertAuthToken(currentAuthToken, user.getUsername());
            return new AuthData(currentAuthToken, user.getUsername());
        }
        throw new RuntimeException("Unauthorized");
    }

    public void logout(String authToken) {
        if (authToken == null || authToken.isEmpty())
            throw new RuntimeException("unauthorized");
        String username = memoryAuthDAO.getAuthToken(authToken).getUsername();
        if (username == null || authToken.isEmpty())
            throw new RuntimeException("unauthorized");
        userDAO.removeUser(username);
    }

    public AuthData register(UserData user){
        if (!userDataIsValid(user))
            throw new RuntimeException("Error: bad request");

        if (userDAO.getUser(user.getUsername()) != null)
            throw new RuntimeException("Error: Already Taken");

        userDAO.insertUser(user);
        return new AuthData(generateAuthToken(), user.getUsername());
    }


    public String getAuthToken(){
        return currentAuthToken;
    }

    private String generateAuthToken(){
        return UUID.randomUUID().toString();
    }

    private boolean userDataIsValid(UserData user){
        if (user.getUsername() == null || user.getUsername().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty()) {
            return false;
        }
        return true;
    }


    public void clear(){
        userDAO.clear();
        memoryGameDAO.clear();
    }
}
