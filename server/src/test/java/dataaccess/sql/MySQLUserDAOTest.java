package dataaccess.sql;

import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLUserDAOTest {

    private MySQLUserDAO userDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new MySQLUserDAO();
        userDAO.clear();
    }

    @Test
    public void createUserSuccess() throws DataAccessException {
        UserData user = new UserData("sam", "password123", "sam@byu.edu");
        userDAO.createUser(user);

        UserData result = userDAO.getUser("sam");
        assertEquals("sam", result.username());
    }

    @Test
    public void createUserDuplicateFails() throws DataAccessException {
        UserData user = new UserData("sam", "password123", "sam@byu.edu");
        userDAO.createUser(user);

        assertThrows(DataAccessException.class, () -> userDAO.createUser(user));
    }

    @Test
    public void getUserSuccess() throws DataAccessException {
        UserData user = new UserData("sam", "password123", "sam@byu.edu");
        userDAO.createUser(user);

        UserData result = userDAO.getUser("sam");
        assertNotNull(result);
        assertEquals("sam@byu.edu", result.email());
    }

    @Test
    public void getUserNotFound() throws DataAccessException {
        UserData result = userDAO.getUser("nonexistent");
        assertNull(result);
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        UserData user = new UserData("sam", "password123", "sam@byu.edu");
        userDAO.createUser(user);

        userDAO.clear();

        UserData result = userDAO.getUser("sam");
        assertNull(result);
    }
}