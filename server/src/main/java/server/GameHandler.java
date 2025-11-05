package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import model.*;
import service.GameService;

import java.util.Collection;

public class GameHandler {

    private Gson serializer = new Gson();

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    GameService gameService;

    public GameHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;

        gameService = new GameService(userDAO, authDAO, gameDAO);
    }

    public void createGame(Context context){
        String authToken = context.header("authorization");

        CreateGameRequest createGameRequest = serializer.fromJson(context.body(), CreateGameRequest.class);

        if (authToken == null || authToken.isEmpty() || createGameRequest.gameName() == null || createGameRequest.gameName().isEmpty()){
            context.status(400);
            context.json("{\"message\": \"Error: bad request" + "\"}");
            return;
        }

        try {
            CreateGameResult createGameResult = gameService.createGame(authToken, createGameRequest);
            context.result(serializer.toJson(createGameResult));
        } catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized")){
                context.status(401);
                context.json("{\"message\": \"Error: unauthorized" + "\"}");
            }
            else {
                context.status(500);
                context.json("{\"message\": \"" + e.getMessage() + "\"}");
            }
        }

    }

    public void joinGame(Context context) {
        String authToken = context.header("authorization");

        JoinGameRequest joinGameRequest = serializer.fromJson(context.body(), JoinGameRequest.class);

        if (authToken == null || authToken.isEmpty() || joinGameRequest.playerColor() == null ||
                !joinGameRequest.playerColor().equals("BLACK") && !joinGameRequest.playerColor().equals("WHITE") ){
            context.status(400);
            context.json("{\"message\": \"Error: bad request" + "\"}");
            return;
        }

        try {
            gameService.joinGame(authToken, joinGameRequest);
        } catch (DataAccessException e) {
            if (e.getMessage().contains("unauthorized")){
                context.status(401);
                context.json("{\"message\": \"Error: unauthorized" + "\"}");
            } else if (e.getMessage().contains("taken")) {
                context.status(403);
                context.json("{\"message\": \"Error: already taken" + "\"}");
            } else if (e.getMessage().contains("Not Found")) {
                context.status(400);
                context.json("{\"message\": \"Error: bad request" + "\"}");
            } else {
                context.status(500);
                context.json("{\"message\": \"" + e.getMessage() + "\"}");
            }
        }

    }

    public void listGames(Context context){
        String authToken = context.header("authorization");

        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);

        try {
            ListGamesResult listGames = gameService.listGames(listGamesRequest);
            context.result(serializer.toJson(listGames));
        } catch (DataAccessException e) {
            context.status(500);
            if (e.getMessage().contains("unauthorized")) {
                context.json("{\"message\": \"Error: unauthorized" + e.getMessage() + "\"}");
                context.status(401);
            }
            else {
                context.json("{\"message\": \"" + e.getMessage() + "\"}");
            }
        }

    }

}
