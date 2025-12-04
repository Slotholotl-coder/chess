package server;

import dataaccess.*;
import io.javalin.*;

public class Server {

    private final Javalin javalin;

    private UserHandler userHandler;
    UserDAO userDAO = new SQLUserDAO();
    AuthDAO authDAO = new SQLAuthDAO();
    GameDAO gameDAO = new SQLGameDAO();

    ClearAllDataHandler clearAllDataHandler;

    private GameHandler gameHandler;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        userHandler = new UserHandler(userDAO, authDAO);
        clearAllDataHandler = new ClearAllDataHandler(userDAO, authDAO, gameDAO);
        gameHandler = new GameHandler(userDAO, authDAO, gameDAO);

        // Register your endpoints and exception handlers here.

        javalin.post("/user", context -> userHandler.register(context));
        javalin.post("/session", context -> userHandler.login(context));
        javalin.delete("/session", context -> userHandler.logout(context));

        javalin.get("/game", context -> gameHandler.listGames(context));
        javalin.post("/game", context -> gameHandler.createGame(context));
        javalin.put("/game", context -> gameHandler.joinGame(context));

        javalin.delete("/db", context -> {
           clearAllDataHandler.clear(context);
        });

    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);

        System.out.println("Server Running");

        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
