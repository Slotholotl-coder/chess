package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemoryGameDAO implements GameDAO{

    List<GameData> gameDataList = new ArrayList<>();

    @Override
    public void insertGame(GameData game) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameNum) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> getAllGames() {
        return List.of();
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public void clear() {
        gameDataList.clear();
    }
}
