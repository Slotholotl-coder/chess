package server;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import io.javalin.*;

public class Server {

    private final Javalin javalin;

    private UserHandler userHandler;
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        userHandler = new UserHandler(userDAO, authDAO);

        // Register your endpoints and exception handlers here.

        javalin.post("/user", context -> userHandler.register(context));
        javalin.post("/session", context -> userHandler.login(context));

    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);

        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
