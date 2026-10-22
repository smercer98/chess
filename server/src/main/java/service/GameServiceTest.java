package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private GameService gameService;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private String validAuthToken;

    @BeforeEach
    public void setup() throws Exception {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        gameService = new GameService(gameDAO, authDAO);

        validAuthToken = "test-token";
        authDAO.createAuth(new AuthData(validAuthToken, "testuser"));
    }

    @Test
    public void createGameSuccess() throws Exception {
        CreateGameRequest req = new CreateGameRequest(validAuthToken, "myGame");
        CreateGameResult result = gameService.createGame(req);

        assertNotNull(result);
        assertTrue(result.gameID() > 0);
    }

    @Test
    public void createGameUnauthorized() {
        CreateGameRequest req = new CreateGameRequest("bad-token", "myGame");
        assertThrows(UnauthorizedException.class, () -> gameService.createGame(req));
    }
}