package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    public static MemoryAuthDAO Instance;

    public static MemoryAuthDAO getInstance() {
        // Lazy initialization
        if (Instance == null) {
            Instance = new MemoryAuthDAO();
        }
        return Instance;
    }


    private Map<String, AuthData> authTokens = new HashMap<>();

    @Override
    public void insertAuthToken(String authToken, String username) {
        authTokens.put(authToken, new AuthData(authToken, username));
    }

    @Override
    public AuthData getAuthToken(String authToken) {
        System.out.println(authTokens.keySet());
        return authTokens.get(authToken);
    }

    @Override
    public void removeAuthToken(String authToken) {
        authTokens.remove(authToken);
    }

    @Override
    public void clear() {
        authTokens.clear();
    }
}
