package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

import java.util.List;

public class GameService {
    private MemoryUserDAO memoryUserDAO = MemoryUserDAO.getInstance();
    private MemoryAuthDAO memoryAuthDAO = MemoryAuthDAO.getInstance();
    private MemoryGameDAO memoryGameDAO = MemoryGameDAO.getInstance();

    public List<ChessGame> listGames(String authToken){

    }

}
