package service;

import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private MemoryUserDAO userDAO = new MemoryUserDAO();

    public AuthData login(UserData user){
        UserData verification = userDAO.getUser(user.getUsername());
        if (verification != null && verification.getPassword().equals(user.getPassword())){
            return new AuthData(generateAuthToken(), user.getUsername());
        }
        throw new RuntimeException("Unauthorized");
    }

    private String generateAuthToken(){
        return UUID.randomUUID().toString();
    }
}
