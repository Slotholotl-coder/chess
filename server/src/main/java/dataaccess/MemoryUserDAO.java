package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    public static MemoryUserDAO instance;

    private final Map<String, UserData> users = new HashMap<>();

    public static MemoryUserDAO getInstance() {
        // Lazy initialization
        if (instance == null) {
            instance = new MemoryUserDAO();
        }
        return instance;
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

    public int getNumberOfUsers() {
        return users.size();
    }

    @Override
    public void clear() {
        users.clear();
    }
}
