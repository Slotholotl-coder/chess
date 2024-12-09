package service;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;

import java.util.Collection;
import java.util.Objects;

public class GameService {
    int gameIdCounter = 1;

    private final AuthDAO memoryAuthDAO = MySQLAuthDAO.getInstance();
    private final GameDAO memoryGameDAO = MySQLGameDAO.getInstance();

    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        if (authToken.isEmpty() || memoryAuthDAO.getAuthToken(authToken) == null) {
            throw new RuntimeException("unauthorized");
        }
        return memoryGameDAO.getAllGames();
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        if (authToken.isEmpty() || memoryAuthDAO.getAuthToken(authToken) == null) {
            throw new DataAccessException("unauthorized");
        }
        int gameID = gameIdCounter;
        gameIdCounter++;
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        memoryGameDAO.insertGame(newGame);
        return newGame.getGameID();
    }

    public void joinGame(String authToken, int gameId, String teamColor) throws DataAccessException {
        if (authToken == null || memoryAuthDAO.getAuthToken(authToken) == null) {
            throw new RuntimeException("unauthorized");
        }
        GameData game = memoryGameDAO.getGame(gameId);
        if (game == null) {
            throw new RuntimeException("invalid game");
        }
        String username = memoryAuthDAO.getAuthToken(authToken).getUsername();
        if (username == null) {
            throw new DataAccessException("unauthorized");
        }
        if (Objects.equals(teamColor, "BLACK")) {
            if (game.getBlackUsername() == null) {
                game.setBlackUsername(memoryAuthDAO.getAuthToken(authToken).getUsername());
                memoryGameDAO.updateGame(game);
            } else {
                throw new DataAccessException("already taken");
            }
        } else if (Objects.equals(teamColor, "WHITE")) {
            if (game.getWhiteUsername() == null) {
                game.setWhiteUsername(memoryAuthDAO.getAuthToken(authToken).getUsername());
                memoryGameDAO.updateGame(game);
            } else {
                throw new DataAccessException("already taken");
            }
        } else {
            throw new DataAccessException("Bad Request");
        }
    }

}
