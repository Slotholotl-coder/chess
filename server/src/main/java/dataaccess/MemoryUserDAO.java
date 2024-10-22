package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements  UserDAO{
    private final Map<String, UserData> users = new HashMap<>();

    @Override
    public void insertUser(UserData user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }
}
