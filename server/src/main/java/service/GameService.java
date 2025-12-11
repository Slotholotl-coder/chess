package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.*;

import java.util.Collection;
import java.util.Objects;

public class GameService {

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    int gameIDCounter = 0;

    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest createGameRequest) throws DataAccessException {
        authDAO.getAuthToken(authToken);

        gameIDCounter++;
        GameData gameData = new GameData(gameIDCounter, null, null, createGameRequest.gameName(), new ChessGame());
        gameDAO.insertGame(gameData);

        return new CreateGameResult(gameIDCounter);

    }

    public JoinGameResult joinGame(String authToken, JoinGameRequest joinGameRequest) throws DataAccessException {
        AuthData authData = authDAO.getAuthToken(authToken);

        GameData game = gameDAO.getGame(joinGameRequest.gameID());

        String blackUsername =game.blackUsername();
        String whiteUsername = game.whiteUsername();

        if (Objects.equals(joinGameRequest.playerColor(), "WHITE")){
            if (whiteUsername != null && !whiteUsername.isEmpty()) {
                throw new DataAccessException("Error: already taken");
            }
            GameData updatedGame = new GameData(joinGameRequest.gameID(), authData.username(), blackUsername, game.gameName(), game.game());
            gameDAO.updateGame(updatedGame);
        }
        if ( Objects.equals(joinGameRequest.playerColor(), "BLACK")){
            if (blackUsername != null && !blackUsername.isEmpty()) {
                throw new DataAccessException("Error: already taken");
            }
            GameData updatedGame = new GameData(joinGameRequest.gameID(), whiteUsername, authData.username(), game.gameName(), game.game());
            gameDAO.updateGame(updatedGame);
        }

        return new JoinGameResult(game);

    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException {

        authDAO.getAuthToken(listGamesRequest.authToken());

        return new ListGamesResult(gameDAO.getAllGames());
    }

}
