package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import service.RegisterRequest;
import service.UserService;

public class Server {

    private final Javalin javalin;

    private UserHandler userHandler;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        Javalin.create()
                .post("/user{username}{password}{email}", context -> userHandler.register(context));

    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);

        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
