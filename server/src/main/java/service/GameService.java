package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public CreateGameResult createGame(CreateGameRequest req) throws DataAccessException, UnauthorizedException, BadRequestException {
        AuthData auth = authDAO.getAuth(req.authToken());
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        if (req.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }

        ChessGame newGame = new ChessGame();
        GameData gameData = new GameData(0, null, null, req.gameName(), newGame);
        GameData createdGame = gameDAO.createGame(gameData);

        CreateGameResult result = new CreateGameResult(createdGame.gameID());
        return result;
    }

    public void joinGame(JoinGameRequest req) throws DataAccessException, UnauthorizedException, BadRequestException, AlreadyTakenException {
        AuthData auth = authDAO.getAuth(req.authToken());
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        if (req.playerColor() == null || (!req.playerColor().equals("WHITE") && !req.playerColor().equals("BLACK"))) {
            throw new BadRequestException("Error: bad request");
        }

        GameData game = gameDAO.getGame(req.gameID());
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }

        if (req.playerColor().equals("WHITE") && game.whiteUsername() != null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        if (req.playerColor().equals("BLACK") && game.blackUsername() != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        GameData updatedGame;
        if (req.playerColor().equals("WHITE")) {
            updatedGame = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
        } else {
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
        }

        gameDAO.updateGame(updatedGame);
    }

    public ListGamesResult listGames(ListGamesRequest req) throws DataAccessException, UnauthorizedException {
        AuthData auth = authDAO.getAuth(req.authToken());
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        Collection<GameData> games = gameDAO.listGames();
        ListGamesResult result = new ListGamesResult(games);
        return result;
    }


}