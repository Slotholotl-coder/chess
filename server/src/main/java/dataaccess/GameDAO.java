package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.Map;

public interface GameDAO {
    void insertGame(GameData game);
    GameData getGame(int gameNum);
    void clear();
}
