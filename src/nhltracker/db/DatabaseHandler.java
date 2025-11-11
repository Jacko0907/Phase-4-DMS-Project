package nhltracker.db;

import nhltracker.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * The {@code DatabaseHandler} class manages all interactions with the SQLite database
 * used by the NHL Stat Tracker application.
 * <p>
 * This class handles connecting to the database, creating tables, and performing
 * CRUD operations for {@link Player} objects.
 * </p>
 */
public class DatabaseHandler {
    private Connection conn;

    /**
     * Constructor connects to the database file and ensures the player table exists.
     *
     * @param dbPath the file path to the SQLite database
     */
    //Constructor connects to database file
    public DatabaseHandler(String dbPath) {
        connect(dbPath);
        createTableIfNotExists();
    }

    /**
     * Establishes a connection to the SQLite database.
     *
     * @param dbPath the file path of the database
     */
    //Connects to the SQLite database
    private void connect(String dbPath) {
        try {
            String url = "jdbc:sqlite:" + dbPath;
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to database: " + dbPath);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    /**
     * Creates the {@code players} table if it does not already exist in the database.
     */
    //Creates the players table if it does not exist
    private void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS players (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    team TEXT NOT NULL,
                    goals INTEGER,
                    assists INTEGER,
                    plus_minus INTEGER
                );
                """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    /**
     * Inserts a new {@link Player} record into the {@code players} table.
     *
     * @param player the player to add to the database
     * @return {@code true} if the player was successfully added, {@code false} otherwise
     */
    //Adds a new player to the database
    public boolean addPlayer(Player player) {
        String sql = "INSERT INTO players(name, team, goals, assists, plus_minus) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.getName());
            pstmt.setString(2, player.getTeam());
            pstmt.setInt(3, player.getGoals());
            pstmt.setInt(4, player.getAssists());
            pstmt.setInt(5, player.getPlusMinus());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding player: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all players from the {@code players} table.
     *
     * @return a {@link List} of {@link Player} objects containing all players in the database
     */
    //Retrieves all players from the database
    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM players ORDER BY name ASC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                players.add(new Player(
                        rs.getString("name"),
                        rs.getString("team"),
                        rs.getInt("goals"),
                        rs.getInt("assists"),
                        rs.getInt("plus_minus")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving players: " + e.getMessage());
        }
        return players;
    }

    /**
     * Finds a player by their name (case-insensitive).
     *
     * @param name the name of the player to search for
     * @return the {@link Player} object if found, otherwise {@code null}
     */
    //Finds a specific player by name
    public Player findPlayerByName(String name) {
        String sql = "SELECT * FROM players WHERE LOWER(name) = LOWER(?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Player(
                        rs.getString("name"),
                        rs.getString("team"),
                        rs.getInt("goals"),
                        rs.getInt("assists"),
                        rs.getInt("plus_minus")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error finding player: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates an existing player's information in the {@code players} table.
     *
     * @param player the {@link Player} object containing updated data
     * @return {@code true} if the update was successful, {@code false} otherwise
     */
    //Updates a player's data
    public boolean updatePlayer(Player player) {
        String sql = """
                UPDATE players
                SET team = ?, goals = ?, assists = ?, plus_minus = ?
                WHERE LOWER(name) = LOWER(?);
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.getTeam());
            pstmt.setInt(2, player.getGoals());
            pstmt.setInt(3, player.getAssists());
            pstmt.setInt(4, player.getPlusMinus());
            pstmt.setString(5, player.getName());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating player: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes a player from the {@code players} table by their name.
     *
     * @param name the name of the player to remove
     * @return {@code true} if the player was successfully removed, {@code false} otherwise
     */
    //Removes a player from the database
    public boolean removePlayer(String name) {
        String sql = "DELETE FROM players WHERE LOWER(name) = LOWER(?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error removing player: " + e.getMessage());
            return false;
        }
    }

    /**
     * Closes the active database connection if it is open.
     */
    //Closes the database connection
    public void close() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Error closing database: " + e.getMessage());
        }
    }
}
