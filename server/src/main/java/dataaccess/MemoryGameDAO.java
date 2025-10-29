package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO{

    List<GameData> gameDataList = new ArrayList<>();

    @Override
    public void insertGame(GameData game) throws DataAccessException {
        gameDataList.add(game);
    }

    @Override
    public GameData getGame(String gameName) throws DataAccessException {
        for (GameData gameData : gameDataList){
            if (Objects.equals(gameData.gameName(), gameName)){
                return gameData;
            }
        }
        throw new DataAccessException("Error: Wrong game name");
    }

    @Override
    public Collection<GameData> getAllGames() {
        return gameDataList;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public void clear() {
        gameDataList.clear();
    }
}
