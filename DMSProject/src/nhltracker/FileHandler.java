package nhltracker;

import java.io.*;
import java.util.*;

public class FileHandler {
    private String fileName;

    public FileHandler(String fileName) {
        this.fileName = fileName;
    }

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

    //Save players to file in the correct format
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
