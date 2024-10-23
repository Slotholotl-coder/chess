package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements  UserDAO{
    public static MemoryUserDAO Instance;

    private final Map<String, UserData> users = new HashMap<>();

    public static MemoryUserDAO getInstance() {
        // Lazy initialization
        if (Instance == null) {
            Instance = new MemoryUserDAO();
        }
        return Instance;
    }

    @Override
    public void insertUser(UserData user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void removeUser(String username) {
        users.remove(username);
    }

    @Override
    public void clear() {
        users.clear();
    }
}
