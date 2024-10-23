package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GameDAO {
    void insertGame(GameData game);
    GameData getGame(int gameNum);
    Collection<GameData> getAllGames();
    void clear();
}
