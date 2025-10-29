package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import model.CreateGameRequest;
import model.CreateGameResult;
import model.GameData;
import model.ListGamesRequest;
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

        if (authToken.isEmpty() || createGameRequest.gameName().isEmpty()){
            context.status(400);
            context.json("{\"message\": \"Error: bad request" + "\"}");
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


    public void listGames(Context context){
        String authToken = context.header("authorization");

        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);

        try {
            Collection<GameData> listGames = gameService.listGames(listGamesRequest);
            context.result(serializer.toJson(listGames.toString()));
        } catch (DataAccessException e) {
            context.status(401);
            context.json("{\"message\": \"Error: unauthorized" + "\"}");

        }

    }

}
