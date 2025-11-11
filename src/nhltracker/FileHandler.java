package nhltracker;

import java.io.*;
import java.util.*;
/**
 * The {@code FileHandler} class provides functionality to load and save {@link Player}
 * data from and to a text file. Each player's record is stored as a single line of
 * comma-separated values in the format:
 * <pre>
 * name, team, goals, assists, plusMinus
 * </pre>
 * <p>
 * This class was used in earlier phases of the NHL Stat Tracker before the migration
 * to SQLite database storage.
 * </p>
 */
public class FileHandler {
    private String fileName;
    /**
     * Constructs a {@code FileHandler} for the specified file name.
     *
     * @param fileName the name (or path) of the file to read from and write to
     */
    public FileHandler(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Loads player data from the text file and returns it as a list of {@link Player} objects.
     * <p>
     * If the file does not exist, an empty list is returned and a message is printed to the console.
     * Each valid line must contain exactly 5 comma-separated values corresponding to:
     * name, team, goals, assists, and plusMinus.
     * </p>
     *
     * @return a list of players read from the file; empty if the file is missing or unreadable
     */
    //Load players from players.txt file
    public List<Player> loadPlayers() {
        List<Player> players = new ArrayList<>();
        File file = new File(fileName);

        //If file does not exist print the error message
        if (!file.exists()) {
            System.out.println("No data file found. A new one will be created on save.");
            return players;
        }
        //If file does exist read it and parse together the stats for display
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String name = parts[0].trim();
                    String team = parts[1].trim();
                    int goals = Integer.parseInt(parts[2].trim());
                    int assists = Integer.parseInt(parts[3].trim());
                    int plusMinus = Integer.parseInt(parts[4].trim());
                    players.add(new Player(name, team, goals, assists, plusMinus));
                }
            }
            //Will display error message if file is unreadable
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        //List players using Array list
        return players;
    }

    /**
     * Saves a list of {@link Player} objects to the text file.
     * <p>
     * Each player is written in CSV format, one player per line.
     * The file is overwritten each time this method is called.
     * </p>
     *
     * @param players the list of players to save
     * @return {@code true} if the save operation was successful, {@code false} otherwise
     */
    public boolean savePlayers(List<Player> players) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Player p : players) {
                writer.printf("%s,%s,%d,%d,%d%n",
                        p.getName(), p.getTeam(), p.getGoals(), p.getAssists(), p.getPlusMinus());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
