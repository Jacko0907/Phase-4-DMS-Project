package nhltracker;

import nhltracker.db.DatabaseHandler;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
//Database handler replaces file handler from previous phases
        System.out.print("Enter the path to your database file (e.g., nhltracker.db): ");
        String dbPath = scanner.nextLine();
        DatabaseHandler dbHandler = new DatabaseHandler(dbPath);
        StatManager manager = new StatManager(dbHandler);


        //Boolean variable set to true so that the main loop starts running until user decides to exit
        boolean running = true;
        /*While loop repeats as long as running is True this allows the user to interact with
         the menu over and over again, only stops when the user hits 0 to set running to false and closes
         with an exit message*/
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addPlayerUI(manager, scanner);
                case "2" -> removePlayerUI(manager, scanner);
                case "3" -> updatePlayerUI(manager, scanner);
                case "4" -> displayAllPlayers(manager);
                case "5" -> filterPlayersUI(manager, scanner);
                case "6" -> searchPlayerUI(manager, scanner);
                case "0" -> {
                    System.out.println("Exiting program");
                    running = false;
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }

        scanner.close();
    }

    //Menu printing that the user will see
    private static void printMenu() {
        System.out.println("\nWelcome to NHL Stat Tracker");
        System.out.println("1. Add Player");
        System.out.println("2. Remove Player");
        System.out.println("3. Update Player");
        System.out.println("4. View All Players");
        System.out.println("5. Filter Players");
        System.out.println("6. Search Player");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    //Menu system that will guide users into adding a new player
    private static void addPlayerUI(StatManager manager, Scanner scanner) {
        try {
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            if (name.matches("\\d+")) {
                System.out.println("Error: Player name cannot be numeric.");
                return; // return to main menu
            }

            System.out.print("Enter team: ");
            String team = scanner.nextLine();
            if (team.matches("\\d+")) {
                System.out.println("Error: Team name cannot be numeric.");
                return; // return to main menu
            }

            System.out.print("Enter goals: ");
            String goalInput = scanner.nextLine();
            if (!goalInput.matches("-?\\d+")) {
                System.out.println("Error: Goals must be an integer value.");
                return;
            }
            int goals = Integer.parseInt(goalInput);

            System.out.print("Enter assists: ");
            String assistInput = scanner.nextLine();
            if (!assistInput.matches("-?\\d+")) {
                System.out.println("Error: Assists must be an integer value.");
                return;
            }
            int assists = Integer.parseInt(assistInput);

            System.out.print("Enter plus/minus: ");
            String plusMinusInput = scanner.nextLine();
            if (!plusMinusInput.matches("-?\\d+")) {
                System.out.println("Error: Plus/Minus must be an integer value.");
                return;
            }
            int plusMinus = Integer.parseInt(plusMinusInput);

            Player newPlayer = new Player(name, team, goals, assists, plusMinus);
            boolean success = manager.addPlayer(newPlayer);
            //If adding player is successful then it will confirm the addition if not an error message will display
            System.out.println(success ? "Player added successfully." : "Failed to add player, Player may already exist.");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    //Removing a player by name with success and error message
    private static void removePlayerUI(StatManager manager, Scanner scanner) {
        System.out.print("Enter player name to remove: ");
        String name = scanner.nextLine();
        boolean removed = manager.removePlayer(name);
        System.out.println(removed ? "Player removed." : "Player not found.");
    }

    //Updating a player will allow the user to change the team name, goals,assists, and plusminus of a player
    private static void updatePlayerUI(StatManager manager, Scanner scanner) {
        System.out.print("Enter player name to update: ");
        String name = scanner.nextLine();
        boolean updated = manager.updatePlayer(name, scanner);
        System.out.println(updated ? "Player updated." : "Player not found.");
    }

    //Displays all players in the system
    private static void displayAllPlayers(StatManager manager) {
        List<Player> players = manager.getAllPlayers();
        System.out.println("\nViewing All Players");
        System.out.println(manager.formatPlayers(players));
    }

    //Filter player will give the user 4 different filtering choices, goals, assists, points, or team
    private static void filterPlayersUI(StatManager manager, Scanner scanner) {
        System.out.println("\nFilter Options:");
        System.out.println("1. By Goals");
        System.out.println("2. By Assists");
        System.out.println("3. By Points");
        System.out.println("4. By Team");
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        List<Player> results = new ArrayList<>();
        //Switch case for player filtering scans for user input and displays the players with the user inputted parameters
        switch (choice) {
            case "1" -> {
                System.out.print("Minimum goals: ");
                String input = scanner.nextLine();
                if (!input.matches("-?\\d+")) {
                    System.out.println("Error: Minimum goals must be an integer value.");
                    return;
                }
                int g = Integer.parseInt(input);
                results = manager.filterByGoals(g);
            }
            case "2" -> {
                System.out.print("Minimum assists: ");
                String input = scanner.nextLine();
                if (!input.matches("-?\\d+")) {
                    System.out.println("Error: Minimum assists must be an integer value.");
                    return;
                }
                int a = Integer.parseInt(input);
                results = manager.filterByAssists(a);
            }
            case "3" -> {
                System.out.print("Minimum points: ");
                String input = scanner.nextLine();
                if (!input.matches("-?\\d+")) {
                    System.out.println("Error: Minimum points must be an integer value.");
                    return;
                }
                int p = Integer.parseInt(input);
                results = manager.filterByPoints(p);
            }
            case "4" -> {
                System.out.print("Team name: ");
                String t = scanner.nextLine();
                if (t.matches("\\d+")) {
                    System.out.println("Error: Team name cannot be numeric.");
                    return;
                }
                results = manager.filterByTeam(t);
            }
            default -> {
                System.out.println("Invalid filter option.");
                return;
            }
        }

        System.out.println("\nFiltered Results");
        System.out.println(manager.formatPlayers(results));
    }

    //Search player method allows specific player by name
    private static void searchPlayerUI(StatManager manager, Scanner scanner) {
        System.out.print("Enter player name to search: ");
        String name = scanner.nextLine();

        Optional<Player> found = manager.searchByName(name);
        if (found.isPresent()) {
            System.out.println("\nPlayer has been found successfully");
            System.out.println(found.get());
        } else {
            System.out.println("No player found with that name.");
        }
    }
}
