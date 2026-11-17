package server;

public class ServerMain {
    public static void main(String[] args) {
        var server = new Server();
        var port = server.run(8080);
        System.out.println("Started server on port " + port);
    }
}