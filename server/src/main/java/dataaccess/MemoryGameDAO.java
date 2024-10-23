package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    public static MemoryGameDAO Instance;

    public static MemoryGameDAO getInstance() {
        // Lazy initialization
        if (Instance == null) {
            Instance = new MemoryGameDAO();
        }
        return Instance;
    }

    private Map<Integer, GameData> games = new HashMap<>();

    @Override
    public void insertGame(GameData game) {
        games.put(game.getGameID(), game);
    }

    @Override
    public GameData getGame(int gameNum) {
        return games.get(gameNum);
    }

    @Override
    public void clear() {
        games.clear();
    }
}
