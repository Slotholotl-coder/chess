package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class mySQLGameDAO implements GameDAO {

    @Override
    public void insertGame(GameData game) {

    }

    @Override
    public GameData getGame(int gameNum) {
        return null;
    }

    @Override
    public Collection<GameData> getAllGames() {
        return List.of();
    }

    @Override
    public void clear() {

    }
}
