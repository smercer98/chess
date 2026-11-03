package dataaccess.sql;

import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLAuthDAOTest {

    private MySQLAuthDAO authDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO = new MySQLAuthDAO();
        authDAO.clear();
    }

    @Test
    public void createAuthSuccess() throws DataAccessException {
        AuthData auth = new AuthData("token123", "sam");
        authDAO.createAuth(auth);

        AuthData result = authDAO.getAuth("token123");
        assertEquals("sam", result.username());
    }

    @Test
    public void createAuthDuplicateFails() throws DataAccessException {
        AuthData auth = new AuthData("token123", "sam");
        authDAO.createAuth(auth);

        assertThrows(DataAccessException.class, () -> authDAO.createAuth(auth));
    }

    @Test
    public void getAuthSuccess() throws DataAccessException {
        AuthData auth = new AuthData("token123", "sam");
        authDAO.createAuth(auth);

        AuthData result = authDAO.getAuth("token123");
        assertNotNull(result);
        assertEquals("token123", result.authToken());
    }

    @Test
    public void getAuthNotFound() throws DataAccessException {
        AuthData result = authDAO.getAuth("nonexistent");
        assertNull(result);
    }

    @Test
    public void deleteAuthSuccess() throws DataAccessException {
        AuthData auth = new AuthData("token123", "sam");
        authDAO.createAuth(auth);

        authDAO.deleteAuth("token123");

        AuthData result = authDAO.getAuth("token123");
        assertNull(result);
    }

    @Test
    public void deleteAuthNonexistentDoesNotFail() throws DataAccessException {
        authDAO.deleteAuth("nonexistent");
        // no exception expected, deleting something that doesn't exist is a no-op
        assertNull(authDAO.getAuth("nonexistent"));
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        AuthData auth = new AuthData("token123", "sam");
        authDAO.createAuth(auth);

        authDAO.clear();

        AuthData result = authDAO.getAuth("token123");
        assertNull(result);
    }
}