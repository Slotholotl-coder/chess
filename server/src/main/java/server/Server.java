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

    WebsocketHandler websocketHandler;

    private GameHandler gameHandler;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        userHandler = new UserHandler(userDAO, authDAO);
        clearAllDataHandler = new ClearAllDataHandler(userDAO, authDAO, gameDAO);
        gameHandler = new GameHandler(userDAO, authDAO, gameDAO);
        websocketHandler = new WebsocketHandler();

        // Register your endpoints and exception handlers here.

        javalin.post("/user", context -> userHandler.register(context));
        javalin.post("/session", context -> userHandler.login(context));
        javalin.delete("/session", context -> userHandler.logout(context));

        javalin.get("/game", context -> gameHandler.listGames(context));
        javalin.post("/game", context -> gameHandler.createGame(context));
        javalin.put("/game", context -> gameHandler.joinGame(context));

//        Javalin.create()
//                .get("/echo/{msg}", ctx -> ctx.result("HTTP response: " + ctx.pathParam("msg")))
//                .ws("/ws", ws -> {
//                    ws.onConnect(ctx -> {
//                        ctx.enableAutomaticPings();
//                        System.out.println("Websocket connected");
//                    });
//                    ws.onMessage(ctx -> ctx.send("WebSocket response:" + ctx.message()));
//                    ws.onClose(_ -> System.out.println("Websocket closed"));
//                });

        javalin.ws("/ws", wsConfig -> {
           wsConfig.onConnect(wsConnectContext -> {
               System.out.println("Connected");
           });
           wsConfig.onMessage(wsMessageContext -> {
               System.out.println(wsMessageContext.message());
           });
           wsConfig.onClose(websocketHandler);
        });

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
