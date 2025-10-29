package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.CreateGameRequest;
import model.CreateGameResult;
import model.GameData;
import model.ListGamesRequest;

import java.util.Collection;

public class GameService {

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    int gameIDCounter = 1;

    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest createGameRequest) throws DataAccessException {
        authDAO.getAuthToken(authToken);

        GameData gameData = new GameData(gameIDCounter++, null, null, createGameRequest.gameName(), new ChessGame());
        gameDAO.insertGame(gameData);

        return new CreateGameResult(gameIDCounter);

    }


    public Collection<GameData> listGames(ListGamesRequest listGamesRequest) throws DataAccessException {

        authDAO.getAuthToken(listGamesRequest.authToken());

        return gameDAO.getAllGames();
    }

}
