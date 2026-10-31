package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO {

    public MySQLUserDAO() throws DataAccessException {
        String createTable = """
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(255) NOT NULL PRIMARY KEY,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL
                )""";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(createTable)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to create users table", e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM users";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to clear users", e);
        }

    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.setString(1, user.username());
            statement.setString(2, hashedPassword);
            statement.setString(3, user.email());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to create user", e);
        }

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String sql = "SELECT username, password, email FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String dbUsername = resultSet.getString("username");
                    String dbPassword = resultSet.getString("password");
                    String dbEmail = resultSet.getString("email");
                    return new UserData(dbUsername, dbPassword, dbEmail);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get user", e);
        }
    }
}
