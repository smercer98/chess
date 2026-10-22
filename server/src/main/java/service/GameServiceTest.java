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

    @Test
    public void joinGameSuccess() throws Exception {
        CreateGameRequest createReq = new CreateGameRequest(validAuthToken, "myGame");
        CreateGameResult createResult = gameService.createGame(createReq);

        JoinGameRequest joinReq = new JoinGameRequest(validAuthToken, "WHITE", createResult.gameID());
        gameService.joinGame(joinReq);

        var game = gameDAO.getGame(createResult.gameID());
        assertEquals("testuser", game.whiteUsername());
    }

    @Test
    public void joinGameAlreadyTaken() throws Exception {
        CreateGameRequest createReq = new CreateGameRequest(validAuthToken, "myGame");
        CreateGameResult createResult = gameService.createGame(createReq);

        JoinGameRequest firstJoin = new JoinGameRequest(validAuthToken, "WHITE", createResult.gameID());
        gameService.joinGame(firstJoin);

        authDAO.createAuth(new AuthData("second-token", "seconduser"));
        JoinGameRequest secondJoin = new JoinGameRequest("second-token", "WHITE", createResult.gameID());

        assertThrows(AlreadyTakenException.class, () -> gameService.joinGame(secondJoin));
    }

    @Test
    public void listGamesSuccess() throws Exception {
        gameService.createGame(new CreateGameRequest(validAuthToken, "game1"));
        gameService.createGame(new CreateGameRequest(validAuthToken, "game2"));

        ListGamesRequest req = new ListGamesRequest(validAuthToken);
        ListGamesResult result = gameService.listGames(req);

        assertEquals(2, result.games().size());
    }

    @Test
    public void listGamesUnauthorized() {
        ListGamesRequest req = new ListGamesRequest("bad-token");
        assertThrows(UnauthorizedException.class, () -> gameService.listGames(req));
    }
}