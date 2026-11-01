package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class MySQLGameDAO implements GameDAO {

    private final Gson gson = new Gson();

    public MySQLGameDAO() throws DataAccessException {
        String createTable = """
                CREATE TABLE IF NOT EXISTS games (
                    gameID INT NOT NULL AUTO_INCREMENT,
                    whiteUsername VARCHAR(255),
                    blackUsername VARCHAR(255),
                    gameName VARCHAR(255) NOT NULL,
                    game TEXT NOT NULL,
                    PRIMARY KEY (gameID)
                )""";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(createTable)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to create games table", e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM games";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to clear games", e);
        }
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        String sql = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        String json = gson.toJson(game.game());
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, game.whiteUsername());
            statement.setString(2, game.blackUsername());
            statement.setString(3, game.gameName());
            statement.setString(4, json);
            statement.executeUpdate();

            var resultSet = statement.getGeneratedKeys();
            int newId = 0;
            if (resultSet.next()) {
                newId = resultSet.getInt(1);
            }
            return new GameData(newId, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
        } catch (SQLException e) {
            throw new DataAccessException("failed to create game", e);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.setInt(1, gameID);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return readGame(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get game", e);
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var games = new ArrayList<GameData>();
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    games.add(readGame(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to list games", e);
        }
        return games;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        String sql = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        String json = gson.toJson(game.game());
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.setString(1, game.whiteUsername());
            statement.setString(2, game.blackUsername());
            statement.setString(3, game.gameName());
            statement.setString(4, json);
            statement.setInt(5, game.gameID());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to update game", e);
        }
    }

    private GameData readGame(java.sql.ResultSet resultSet) throws SQLException {
        int gameID = resultSet.getInt("gameID");
        String whiteUsername = resultSet.getString("whiteUsername");
        String blackUsername = resultSet.getString("blackUsername");
        String gameName = resultSet.getString("gameName");
        String json = resultSet.getString("game");
        ChessGame chessGame = gson.fromJson(json, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
    }
}