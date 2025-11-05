package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import model.*;
import service.UserService;

public class UserHandler {

    private Gson serializer = new Gson();
    private UserService userService;

    public UserHandler(UserDAO userDAO, AuthDAO authDAO){
        userService = new UserService(userDAO, authDAO);
    }

    public void register(Context context) {
        RegisterRequest registerRequest = serializer.fromJson(context.body(), RegisterRequest.class);

        if(registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null){
            context.status(400);
            context.json("{\"message\": \"Error: bad request" + "\"}");
            return;
        }

        RegisterResult registerResult = null;

        try {
            registerResult = userService.register(registerRequest);
            context.result(serializer.toJson(registerResult));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Taken")){
                context.status(403);
            }
            else {
                context.status(500);
            }
            context.json("{\"message\": \"" + e.getMessage() + "\"}");
        }

    }

    public void login(Context context){
        LoginRequest loginRequest = serializer.fromJson(context.body(), LoginRequest.class);

        if(loginRequest.username() == null || loginRequest.password() == null){
            context.status(400);
            context.json("{\"message\": \"Error: bad request" + "\"}");
            return;
        }

        LoginResult loginResult = null;

        try {
            loginResult = userService.login(loginRequest);
            context.result(serializer.toJson(loginResult));
        } catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized")) {
                context.status(401);
                context.json("{\"message\": \"Error: unauthorized" + e.getMessage() + "\"}");
            }
            else {
                context.status(500);
                context.json("{\"message\": \"" + e.getMessage() + "\"}");
            }
        }

    }

    public void logout(Context context){
        String authToken = context.header("authorization");

        LogoutRequest logoutRequest = new LogoutRequest(authToken);

        try {
            userService.logout(logoutRequest);
        } catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized")) {
                context.status(401);
                context.json("{\"message\": \"Error: unauthorized" + e.getMessage() + "\"}");
            }
            else {
                context.status(500);
                context.json("{\"message\": \"" + e.getMessage() + "\"}");
            }
        }

    }

}
