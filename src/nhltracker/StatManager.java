package nhltracker;

import nhltracker.db.DatabaseHandler;
import java.util.*;
import java.util.stream.Collectors;
/**
 * The {@code StatManager} class acts as the business logic layer for the NHL Stat Tracker system.
 * <p>
 * It manages player statistics, interacting directly with the {@link DatabaseHandler} to perform CRUD operations.
 * This class is used by both the CLI and GUI components.
 * </p>
 */
public class StatManager {
    private final DatabaseHandler dbHandler;

    /**
     * Constructor connects to the SQLite handler.
     *
     * @param dbHandler the {@link DatabaseHandler} responsible for database operations
     */
    //Constructor connects to the SQLite handler
    public StatManager(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    /**
     * Adds a player only if they don't already exist in the database.
     *
     * @param player the {@link Player} object to add
     * @return {@code true} if the player was added successfully; {@code false} if a duplicate exists
     */
    //Adds a player only if they don't already exist
    public boolean addPlayer(Player player) {
        Player existing = dbHandler.findPlayerByName(player.getName());
        if (existing != null) {
            return false;
        }
        return dbHandler.addPlayer(player);
    }

    /**
     * Returns all players currently stored in the database.
     *
     * @return a {@link List} of all {@link Player} objects
     */
    //Returns all players from the database
    public List<Player> getAllPlayers() {
        return dbHandler.getAllPlayers();
    }

    /**
     * Updates an existing player's statistics through the CLI.
     *
     * @param name    the name of the player to update
     * @param scanner a {@link Scanner} for reading new values from user input
     * @return {@code true} if the update was successful; {@code false} otherwise
     */
    //Updates a player by name (for CLI use)
    public boolean updatePlayer(String name, Scanner scanner) {
        Player existing = dbHandler.findPlayerByName(name);
        if (existing == null) return false;

        System.out.print("Enter new team: ");
        existing.setTeam(scanner.nextLine());
        System.out.print("Enter new goals: ");
        existing.setGoals(Integer.parseInt(scanner.nextLine()));
        System.out.print("Enter new assists: ");
        existing.setAssists(Integer.parseInt(scanner.nextLine()));
        System.out.print("Enter new plus/minus: ");
        existing.setPlusMinus(Integer.parseInt(scanner.nextLine()));

        return dbHandler.updatePlayer(existing);
    }

    /**
     * Updates a player's statistics from GUI input fields.
     *
     * @param name        the name of the player to update
     * @param newTeam     the updated team name
     * @param newGoals    the updated goal count
     * @param newAssists  the updated assist count
     * @param newPlusMinus the updated plus/minus value
     * @return {@code true} if the player was updated successfully; {@code false} if the player was not found
     */
    //Updates player from GUI
    public boolean updatePlayerGUI(String name, String newTeam, int newGoals, int newAssists, int newPlusMinus) {
        Player existing = dbHandler.findPlayerByName(name);
        if (existing == null) return false;

        existing.setTeam(newTeam);
        existing.setGoals(newGoals);
        existing.setAssists(newAssists);
        existing.setPlusMinus(newPlusMinus);

        return dbHandler.updatePlayer(existing);
    }

    /**
     * Removes a player from the database using their name.
     *
     * @param name the name of the player to remove
     * @return {@code true} if the player was removed; {@code false} if not found
     */
    //Removes player by name
    public boolean removePlayer(String name) {
        return dbHandler.removePlayer(name);
    }

    /**
     * Searches for a player by name.
     *
     * @param name the name of the player to search for
     * @return an {@link Optional} containing the player if found; otherwise, an empty Optional
     */
    //Search for a player by name
    public Optional<Player> searchByName(String name) {
        Player found = dbHandler.findPlayerByName(name);
        return Optional.ofNullable(found);
    }

    /**
     * Filters players with goals greater than or equal to the specified minimum.
     *
     * @param minGoals the minimum number of goals
     * @return a filtered {@link List} of {@link Player} objects
     */
    //Filter players by minimum goals
    public List<Player> filterByGoals(int minGoals) {
        return dbHandler.getAllPlayers().stream()
                .filter(p -> p.getGoals() >= minGoals)
                .collect(Collectors.toList());
    }

    /**
     * Filters players with assists greater than or equal to the specified minimum.
     *
     * @param minAssists the minimum number of assists
     * @return a filtered {@link List} of {@link Player} objects
     */
    //Filter players by minimum assists
    public List<Player> filterByAssists(int minAssists) {
        return dbHandler.getAllPlayers().stream()
                .filter(p -> p.getAssists() >= minAssists)
                .collect(Collectors.toList());
    }

    /**
     * Filters players whose total points (goals + assists) are greater than or equal to the specified minimum.
     *
     * @param minPoints the minimum total points
     * @return a filtered {@link List} of {@link Player} objects
     */
    //Filter players by total points
    public List<Player> filterByPoints(int minPoints) {
        return dbHandler.getAllPlayers().stream()
                .filter(p -> p.getPoints() >= minPoints)
                .collect(Collectors.toList());
    }

    /**
     * Filters players based on their team name.
     *
     * @param teamName the team name to match
     * @return a filtered {@link List} of {@link Player} objects
     */
    //Filter players by team name
    public List<Player> filterByTeam(String teamName) {
        return dbHandler.getAllPlayers().stream()
                .filter(p -> p.getTeam().equalsIgnoreCase(teamName))
                .collect(Collectors.toList());
    }

    /**
     * Formats a list of players into a readable text output for console display.
     *
     * @param list the list of players to format
     * @return a formatted {@link String} of player details
     */
    //Formats a player list into display text used by CLI
    public String formatPlayers(List<Player> list) {
        if (list.isEmpty()) {
            return "No players found for your criteria.";
        }
        StringBuilder sb = new StringBuilder();
        for (Player p : list) {
            sb.append(p.toString()).append("\n");
        }
        return sb.toString();
    }
}
