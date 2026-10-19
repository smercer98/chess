package dataaccess;

import model.GameData;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();
    private final AtomicInteger nextID = new AtomicInteger(1);

    public void clear() {
        games.clear();
    }

    public GameData createGame(GameData game) {
        int id = nextID.getAndIncrement();
        GameData newGame = new GameData(id, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
        games.put(id, newGame);
        return newGame;
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public Collection<GameData> listGames() {
        return games.values();
    }

    public void updateGame(GameData game) {
        games.put(game.gameID(), game);
    }
}