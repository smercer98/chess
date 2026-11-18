package client;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;
import ui.BoardPrinter;

public class ChessClient {
    private final ServerFacade server;
    private String authToken = null;
    private State state = State.PRELOGIN;
    private List<GameData> lastListedGames = new ArrayList<>();

    private enum State {
        PRELOGIN, POSTLOGIN
    }

    public ChessClient(String serverUrl) {
        int port = Integer.parseInt(serverUrl.split(":")[2]);
        server = new ServerFacade(port);
    }

    public void run() {
        System.out.println("Welcome to Chess. Type 'help' to get started.");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print("\n[" + state + "] >>> ");
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(result);
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }
        }
    }

    private String eval(String input) throws Exception {
        var tokens = input.toLowerCase().split(" ");
        var cmd = tokens.length > 0 ? tokens[0] : "help";
        return switch (cmd) {
            case "login" -> login(tokens);
            case "register" -> register(tokens);
            case "logout" -> logout();
            case "create" -> create(tokens);
            case "list" -> list();
            case "join" -> join(tokens);
            case "observe" -> observe(tokens);
            case "quit" -> "quit";
            default -> help();
        };
    }

    private String login(String[] tokens) throws Exception {
        if (tokens.length != 3) {
            return "Expected: login <username> <password>";
        }
        var authData = server.login(tokens[1], tokens[2]);
        authToken = authData.authToken();
        state = State.POSTLOGIN;
        return "Logged in as " + tokens[1];
    }

    private String register(String[] tokens) throws Exception {
        if (tokens.length != 4) {
            return "Expected: register <username> <password> <email>";
        }
        var authData = server.register(tokens[1], tokens[2], tokens[3]);
        authToken = authData.authToken();
        state = State.POSTLOGIN;
        return "Registered and logged in as " + tokens[1];
    }

    private String logout() throws Exception {
        server.logout(authToken);
        authToken = null;
        state = State.PRELOGIN;
        return "Logged out";
    }

    private String create(String[] tokens) throws Exception {
        if (tokens.length != 2) {
            return "Expected: create <gameName>";
        }
        var result = server.createGame(tokens[1], authToken);
        return "Created game \"" + tokens[1] + "\" (id " + result.gameID() + ")";
    }

    private String list() throws Exception {
        var result = server.listGames(authToken);
        lastListedGames = new ArrayList<>(result.games());
        if (lastListedGames.isEmpty()) {
            return "No games yet";
        }
        var sb = new StringBuilder();
        for (int i = 0; i < lastListedGames.size(); i++) {
            var game = lastListedGames.get(i);
            var white = game.whiteUsername() != null ? game.whiteUsername() : "none";
            var black = game.blackUsername() != null ? game.blackUsername() : "none";
            sb.append(i + 1).append(": ").append(game.gameName())
                    .append(" (white: ").append(white).append(", black: ").append(black).append(")\n");
        }
        return sb.toString();
    }

    private String join(String[] tokens) throws Exception {
        if (tokens.length != 3) {
            return "Expected: join <list number> <WHITE|BLACK>";
        }
        var listNumber = Integer.parseInt(tokens[1]);
        if (listNumber < 1 || listNumber > lastListedGames.size()) {
            return "Invalid game number. Run 'list' first to see available games.";
        }
        ChessGame.TeamColor color;
        try {
            color = ChessGame.TeamColor.valueOf(tokens[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            return "Color must be WHITE or BLACK.";
        }
        var gameID = lastListedGames.get(listNumber - 1).gameID();
        server.joinGame(gameID, color.toString(), authToken);

        var board = new ChessBoard();
        board.resetBoard();
        return "Joined game " + listNumber + " as " + color + "\n" + BoardPrinter.boardToString(board, color);
    }

    private String observe(String[] tokens) throws Exception {
        if (tokens.length != 2) {
            return "Expected: observe <list number>";
        }
        var listNumber = Integer.parseInt(tokens[1]);
        if (listNumber < 1 || listNumber > lastListedGames.size()) {
            return "Invalid game number. Run 'list' first to see available games.";
        }
        var game = lastListedGames.get(listNumber - 1);

        var board = new ChessBoard();
        board.resetBoard();
        return "Observing game " + listNumber + " (\"" + game.gameName() + "\")\n"
                + BoardPrinter.boardToString(board, ChessGame.TeamColor.WHITE);
    }

    private String help() {
        if (state == State.PRELOGIN) {
            return """
                register <username> <password> <email> - create an account
                login <username> <password> - log in
                quit - exit the program
                help - show this message
                """;
        }
        return """
            create <gameName> - create a game
            list - list games
            join <list number> <WHITE|BLACK> - join a game
            observe <list number> - observe a game
            logout - log out
            quit - exit the program
            help - show this message
            """;
    }
}