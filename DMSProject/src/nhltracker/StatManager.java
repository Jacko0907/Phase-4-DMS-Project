package nhltracker;

import nhltracker.db.DatabaseHandler;
import java.util.*;
import java.util.stream.Collectors;

public class StatManager {
    private final DatabaseHandler dbHandler;

    //Constructor connects to the SQLite handler
    public StatManager(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    //Adds a player only if they don't already exist
    public boolean addPlayer(Player player) {
        Player existing = dbHandler.findPlayerByName(player.getName());
        if (existing != null) {
            return false; // Duplicate
        }
        return dbHandler.addPlayer(player);
    }

    //Returns all players from the database
    public List<Player> getAllPlayers() {
        return dbHandler.getAllPlayers();
    }

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

    //Removes player by name
    public boolean removePlayer(String name) {
        return dbHandler.removePlayer(name);
    }

    //Search for a player by name
    public Optional<Player> searchByName(String name) {
        Player found = dbHandler.findPlayerByName(name);
        return Optional.ofNullable(found);
    }

    //Filter players by minimum goals
    public List<Player> filterByGoals(int minGoals) {
        return dbHandler.getAllPlayers().stream()
                .filter(p -> p.getGoals() >= minGoals)
                .collect(Collectors.toList());
    }

    //Filter players by minimum assists
    public List<Player> filterByAssists(int minAssists) {
        return dbHandler.getAllPlayers().stream()
                .filter(p -> p.getAssists() >= minAssists)
                .collect(Collectors.toList());
    }

    //Filter players by total points
    public List<Player> filterByPoints(int minPoints) {
        return dbHandler.getAllPlayers().stream()
                .filter(p -> p.getPoints() >= minPoints)
                .collect(Collectors.toList());
    }

    //Filter players by team name
    public List<Player> filterByTeam(String teamName) {
        return dbHandler.getAllPlayers().stream()
                .filter(p -> p.getTeam().equalsIgnoreCase(teamName))
                .collect(Collectors.toList());
    }

    //Formats a player list into display text
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
