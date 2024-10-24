package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    public static MemoryGameDAO Instance;
    private final Map<Integer, GameData> games = new HashMap<>();

    public static MemoryGameDAO getInstance() {
        // Lazy initialization
        if (Instance == null) {
            Instance = new MemoryGameDAO();
        }
        return Instance;
    }

    @Override
    public void insertGame(GameData game) {
        games.put(game.getGameID(), game);
    }

    @Override
    public GameData getGame(int gameNum) {
        return games.get(gameNum);
    }

    @Override
    public Collection<GameData> getAllGames() {
        return games.values();
    }

    public int getNumberOfGames() {
        return games.size();
    }

    @Override
    public void clear() {
        games.clear();
    }
}
