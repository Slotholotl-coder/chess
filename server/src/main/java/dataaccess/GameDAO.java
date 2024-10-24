package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void insertGame(GameData game);

    GameData getGame(int gameNum);

    Collection<GameData> getAllGames();

    void clear();
}
