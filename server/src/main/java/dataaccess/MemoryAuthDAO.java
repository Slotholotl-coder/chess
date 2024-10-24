package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    public static MemoryAuthDAO Instance;
    private final Map<String, AuthData> authTokens = new HashMap<>();

    public static MemoryAuthDAO getInstance() {
        // Lazy initialization
        if (Instance == null) {
            Instance = new MemoryAuthDAO();
        }
        return Instance;
    }

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

    public int getNumberOfAuthTokens() {
        return authTokens.size();
    }

    @Override
    public void clear() {
        authTokens.clear();
    }
}
