package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.GameData;

import java.util.Collection;
import java.util.Objects;

public class GameService {
    int gameIdCounter = 1;
    private final MemoryUserDAO memoryUserDAO = MemoryUserDAO.getInstance();
    private final MemoryAuthDAO memoryAuthDAO = MemoryAuthDAO.getInstance();
    private final MemoryGameDAO memoryGameDAO = MemoryGameDAO.getInstance();

    public Collection<GameData> listGames(String authToken) {
        if (authToken.isEmpty() || memoryAuthDAO.getAuthToken(authToken) == null) {
            throw new RuntimeException("unauthorized");
        }
        return memoryGameDAO.getAllGames();
    }

    public int createGame(String authToken, String gameName) {
        if (authToken.isEmpty() || memoryAuthDAO.getAuthToken(authToken) == null)
            throw new RuntimeException("unauthorized");
        int gameID = gameIdCounter;
        gameIdCounter++;
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        memoryGameDAO.insertGame(newGame);
        return newGame.getGameID();
    }

    public void joinGame(String authToken, int gameId, String teamColor) {
        if (authToken == null || memoryAuthDAO.getAuthToken(authToken) == null) {
            throw new RuntimeException("unauthorized");
        }
        GameData game = memoryGameDAO.getGame(gameId);
        if (game == null)
            throw new RuntimeException("invalid game");
        String username = memoryAuthDAO.getAuthToken(authToken).getUsername();
        if (username == null)
            throw new RuntimeException("unauthorized");
        if (Objects.equals(teamColor, "BLACK")) {
            if (game.getBlackUsername() == null)
                game.setBlackUsername(memoryAuthDAO.getAuthToken(authToken).getUsername());
            else
                throw new RuntimeException("already taken");
        } else if (Objects.equals(teamColor, "WHITE")) {
            if (game.getWhiteUsername() == null)
                game.setWhiteUsername(memoryAuthDAO.getAuthToken(authToken).getUsername());
            else
                throw new RuntimeException("already taken");
        } else
            throw new RuntimeException("invalid team color");
    }

}
