package model;

import java.util.Collection;

public record ListGamesResult(Collection<GameData> games) {
    @Override
    public String toString() {
        return "games=" + games;
    }
}
