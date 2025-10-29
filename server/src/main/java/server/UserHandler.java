package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import model.RegisterResult;
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
            context.json("{\"message\": \"bad request" + "\"}");
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
            context.json("{\"message\": \"bad request" + "\"}");
            return;
        }

        LoginResult loginResult = null;

        try {
            loginResult = userService.login(loginRequest);
            context.result(serializer.toJson(loginResult));
        } catch (DataAccessException e) {
            context.status(401);
            context.json("{\"message\": \" unauthorized" + "\"}");
        }

    }

}
