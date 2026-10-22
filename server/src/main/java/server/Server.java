package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import io.javalin.*;
import service.*;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();
    private final UserService userService = new UserService(userDAO, authDAO);
    private final GameService gameService = new GameService(gameDAO, authDAO);
    private final Gson gson = new Gson();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", ctx -> {
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
            ctx.status(200);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(Map.of()));
        });

        javalin.post("/user", ctx -> {
            try {
                RegisterRequest req = gson.fromJson(ctx.body(), RegisterRequest.class);
                RegisterResult result = userService.register(req);
                ctx.status(200);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(result));
            } catch (BadRequestException e) {
                ctx.status(400);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            } catch (AlreadyTakenException e) {
                ctx.status(403);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            } catch (Exception e) {
                ctx.status(500);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
            }
        });

        javalin.post("/session", ctx -> {
            try {
                LoginRequest req = gson.fromJson(ctx.body(), LoginRequest.class);
                LoginResult result = userService.login(req);
                ctx.status(200);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(result));
            } catch (BadRequestException e) {
                ctx.status(400);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            } catch (UnauthorizedException e) {
                ctx.status(401);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            } catch (Exception e) {
                ctx.status(500);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
            }
        });

        javalin.delete("/session", ctx -> {
            try {
                String authToken = ctx.header("authorization");
                userService.logout(new LogoutRequest(authToken));
                ctx.status(200);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of()));
            } catch (UnauthorizedException e) {
                ctx.status(401);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            } catch (Exception e) {
                ctx.status(500);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
            }
        });

        javalin.get("/game", ctx -> {
            try {
                String authToken = ctx.header("authorization");
                ListGamesResult result = gameService.listGames(new ListGamesRequest(authToken));
                ctx.status(200);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(result));
            } catch (UnauthorizedException e) {
                ctx.status(401);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            } catch (Exception e) {
                ctx.status(500);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
            }
        });

        javalin.post("/game", ctx -> {
            try {
                String authToken = ctx.header("authorization");
                CreateGameRequest body = gson.fromJson(ctx.body(), CreateGameRequest.class);
                CreateGameRequest req = new CreateGameRequest(authToken, body.gameName());
                CreateGameResult result = gameService.createGame(req);
                ctx.status(200);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(result));
            } catch (BadRequestException e) {
                ctx.status(400);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            } catch (UnauthorizedException e) {
                ctx.status(401);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            } catch (Exception e) {
                ctx.status(500);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
            }
        });

        javalin.put("/game", ctx -> {
            try {
                String authToken = ctx.header("authorization");
                JoinGameRequest body = gson.fromJson(ctx.body(), JoinGameRequest.class);
                JoinGameRequest req = new JoinGameRequest(authToken, body.playerColor(), body.gameID());
                gameService.joinGame(req);
                ctx.status(200);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of()));
            } catch (BadRequestException e) {
                ctx.status(400);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            } catch (UnauthorizedException e) {
                ctx.status(401);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            } catch (AlreadyTakenException e) {
                ctx.status(403);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            } catch (Exception e) {
                ctx.status(500);
                ctx.contentType("application/json");
                ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
            }
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}