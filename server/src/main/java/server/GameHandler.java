package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
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



    }


    public void listGames(Context context){
        String authToken = context.header("authorization");

        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);

        try {
            Collection<GameData> listGames = gameService.listGames(listGamesRequest);
            context.result(serializer.toJson(listGames));
        } catch (DataAccessException e) {
            context.status(401);
            context.json("{\"message\": \"Error: unauthorized" + "\"}");

        }

    }

}
