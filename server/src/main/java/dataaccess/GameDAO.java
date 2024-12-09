package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void insertGame(GameData game) throws DataAccessException;

    GameData getGame(int gameNum) throws DataAccessException;

    Collection<GameData> getAllGames();

    public void updateGame(GameData gameData) throws DataAccessException;

    void clear();
}
