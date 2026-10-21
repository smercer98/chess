package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService userService;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    public void registerSuccess() throws Exception {
        RegisterRequest req = new RegisterRequest("sam", "password", "sam@byu.edu");
        RegisterResult result = userService.register(req);

        assertEquals("sam", result.username());
        assertNotNull(result.authToken());
        assertNotNull(userDAO.getUser("sam"));
    }

    @Test
    public void registerDuplicateUsername() throws Exception {
        RegisterRequest req = new RegisterRequest("sam", "password", "sam@byu.edu");
        userService.register(req);

        assertThrows(AlreadyTakenException.class, () -> userService.register(req));
    }

    @Test
    public void loginSuccess() throws Exception {
        userService.register(new RegisterRequest("sam", "password", "sam@byu.edu"));

        LoginResult result = userService.login(new LoginRequest("sam", "password"));

        assertEquals("sam", result.username());
        assertNotNull(result.authToken());
    }

    @Test
    public void loginWrongPassword() throws Exception {
        userService.register(new RegisterRequest("sam", "password", "sam@byu.edu"));

        assertThrows(UnauthorizedException.class, () -> userService.login(new LoginRequest("sam", "wrongpassword")));
    }

    @Test
    public void logoutSuccess() throws Exception {
        RegisterResult reg = userService.register(new RegisterRequest("sam", "password", "sam@byu.edu"));

        userService.logout(new LogoutRequest(reg.authToken()));

        assertNull(authDAO.getAuth(reg.authToken()));
    }

    @Test
    public void logoutBadAuthToken() {
        assertThrows(UnauthorizedException.class, () -> userService.logout(new LogoutRequest("bad-token")));
    }
}