package client;

import java.util.Scanner;

public class ChessClient {
    private final ServerFacade server;
    private String authToken = null;
    private State state = State.PRELOGIN;

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

    private String help() {
        if (state == State.PRELOGIN) {
            return """
                register <username> <password> <email> - create an account
                login <username> <password> - log in
                quit - exit the program
                help - show this message
                """;
        }
        return "logged in - postlogin commands not built yet";
    }
}