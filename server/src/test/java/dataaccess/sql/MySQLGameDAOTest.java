package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLGameDAOTest {

    private MySQLGameDAO gameDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO = new MySQLGameDAO();
        gameDAO.clear();
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        GameData game = new GameData(0, null, null, "myGame", new ChessGame());
        GameData result = gameDAO.createGame(game);

        assertTrue(result.gameID() > 0);
        assertEquals("myGame", result.gameName());
    }

    @Test
    public void createGameNullNameFails() {
        GameData game = new GameData(0, null, null, null, new ChessGame());
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(game));
    }

    @Test
    public void getGameSuccess() throws DataAccessException {
        GameData game = new GameData(0, null, null, "myGame", new ChessGame());
        GameData created = gameDAO.createGame(game);

        GameData result = gameDAO.getGame(created.gameID());
        assertNotNull(result);
        assertEquals("myGame", result.gameName());
    }

    @Test
    public void getGameNotFound() throws DataAccessException {
        GameData result = gameDAO.getGame(99999);
        assertNull(result);
    }

    @Test
    public void listGamesSuccess() throws DataAccessException {
        gameDAO.createGame(new GameData(0, null, null, "game1", new ChessGame()));
        gameDAO.createGame(new GameData(0, null, null, "game2", new ChessGame()));

        var games = gameDAO.listGames();
        assertEquals(2, games.size());
    }

    @Test
    public void listGamesEmpty() throws DataAccessException {
        var games = gameDAO.listGames();
        assertEquals(0, games.size());
    }

    @Test
    public void updateGameSuccess() throws DataAccessException {
        GameData game = new GameData(0, null, null, "myGame", new ChessGame());
        GameData created = gameDAO.createGame(game);

        GameData updated = new GameData(created.gameID(), "sam", null, "myGame", created.game());
        gameDAO.updateGame(updated);

        GameData result = gameDAO.getGame(created.gameID());
        assertEquals("sam", result.whiteUsername());
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        gameDAO.createGame(new GameData(0, null, null, "myGame", new ChessGame()));

        gameDAO.clear();

        var games = gameDAO.listGames();
        assertEquals(0, games.size());
    }
}