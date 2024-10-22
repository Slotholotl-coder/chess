package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class AuthData {
    String authToken;
    String username;

    public AuthData(String authToken, String username){
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
}
