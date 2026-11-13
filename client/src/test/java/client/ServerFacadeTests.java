package client;

import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    public void registerSuccess() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void registerDuplicateFails() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        assertThrows(Exception.class, () -> facade.register("player1", "password", "p1@email.com"));
    }

    @Test
    public void loginSuccess() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void loginWrongPasswordFails() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        assertThrows(Exception.class, () -> facade.login("player1", "wrongpassword"));
    }

    @Test
    public void logoutSuccess() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertDoesNotThrow(() -> facade.logout(authData.authToken()));
    }

    @Test
    public void logoutInvalidTokenFails() {
        assertThrows(Exception.class, () -> facade.logout("invalid-token"));
    }

    @BeforeEach
    public void setup() throws Exception {
        facade.clear();
    }

}
