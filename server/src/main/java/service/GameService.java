package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class GameService {
    private MemoryUserDAO memoryUserDAO = MemoryUserDAO.getInstance();
    private MemoryAuthDAO memoryAuthDAO = MemoryAuthDAO.getInstance();
    private MemoryGameDAO memoryGameDAO = MemoryGameDAO.getInstance();

    int gameIdCounter = 0;

    public Collection<GameData> listGames(String authToken){
        if (authToken.isEmpty() || memoryAuthDAO.getAuthToken(authToken) == null){
            throw new RuntimeException("unauthorized");
        }
        Collection< GameData > games = memoryGameDAO.getAllGames();
        return games;
    }

    public GameData createGame(String authToken, String gameName){
        if (authToken.isEmpty() || memoryAuthDAO.getAuthToken(authToken) == null)
            throw new RuntimeException("unauthorized");
        int gameID = gameIdCounter;
        gameIdCounter++;
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        memoryGameDAO.insertGame(newGame);
        return newGame;
    }

}
