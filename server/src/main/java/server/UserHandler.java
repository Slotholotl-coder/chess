package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.RegisterRequest;
import service.UserService;

public class UserHandler {

    private Gson serializer = new Gson();
    private UserService userService;

    public void register(Context context) throws DataAccessException {
        RegisterRequest registerRequest = serializer.fromJson(context.body(), RegisterRequest.class);

        try {
            userService.register(registerRequest);
        } catch (DataAccessException e) {
            if (e.getMessage().isEmpty()){
                context.status(200);
            }
            else if (e.getMessage().contains("taken")){
                context.status(403);
            }
        }
    }
}
