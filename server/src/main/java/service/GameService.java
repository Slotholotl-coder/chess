package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.GameData;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class GameService {
    private MemoryUserDAO memoryUserDAO = MemoryUserDAO.getInstance();
    private MemoryAuthDAO memoryAuthDAO = MemoryAuthDAO.getInstance();
    private MemoryGameDAO memoryGameDAO = MemoryGameDAO.getInstance();

    int gameIdCounter = 1;

    public Collection<GameData> listGames(String authToken){
        if (authToken.isEmpty() || memoryAuthDAO.getAuthToken(authToken) == null){
            throw new RuntimeException("unauthorized");
        }
        Collection< GameData > games = memoryGameDAO.getAllGames();
        return games;
    }

    public int createGame(String authToken, String gameName){
        if (authToken.isEmpty() || memoryAuthDAO.getAuthToken(authToken) == null)
            throw new RuntimeException("unauthorized");
        int gameID = gameIdCounter;
        gameIdCounter++;
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        memoryGameDAO.insertGame(newGame);
        return newGame.getGameID();
    }

    public void joinGame(String authToken, int gameId, String teamColor){
        if (authToken == null || memoryAuthDAO.getAuthToken(authToken) == null){
            throw new RuntimeException("unauthorized");
        }
        GameData game = memoryGameDAO.getGame(gameId);
        if (game == null)
            throw new RuntimeException("invalid game");
        String username = memoryAuthDAO.getAuthToken(authToken).getUsername();
        if (username == null)
            throw new RuntimeException("unauthorized");
        if (Objects.equals(teamColor, "BLACK")){
            if (game.getBlackUsername() == null)
                game.setBlackUsername(memoryAuthDAO.getAuthToken(authToken).getUsername());
            else
                throw new RuntimeException("already taken");
        }
        else if (Objects.equals(teamColor, "WHITE")) {
            if (game.getWhiteUsername() == null)
                game.setWhiteUsername(memoryAuthDAO.getAuthToken(authToken).getUsername());
            else
                throw new RuntimeException("already taken");
        }
        else
            throw new RuntimeException("invalid team color");
    }

}
