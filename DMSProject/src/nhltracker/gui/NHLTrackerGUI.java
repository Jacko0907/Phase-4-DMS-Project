package nhltracker.gui;

import nhltracker.*;
import nhltracker.db.DatabaseHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/*
 Phase 4 GUI for the NHL Stat Tracker.
  Displays player data from nhltracker database in a JTable.
 Adds functionality to view, add, delete, update, search, filter, and sort players through the GUI.
 */
public class NHLTrackerGUI extends JFrame {
    //References to helper classes and UI components
    private StatManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    //Input fields for adding or updating player data
    private JTextField nameField, teamField, goalsField, assistsField, plusMinusField;
    //Combo boxes and text fields for filtering and sorting
    private JComboBox<String> filterTypeBox, sortBox;
    private JTextField filterValueField;
    //Summary label will show how many total players are in the table
    private JLabel summaryLabel;

    //Contructor that sets up the GUI along with a file handler to read and write player data to players.txt
    public NHLTrackerGUI() {
        // === NEW CODE: Ask user for database location ===
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Please select your NHL Stats database file");
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(this, "No database selected. The application will now exit.");
            System.exit(0);
        }

        File dbFile = fileChooser.getSelectedFile();
        String dbPath = dbFile.getAbsolutePath();

        DatabaseHandler dbHandler = new DatabaseHandler(dbPath);
        manager = new StatManager(dbHandler);

        //Configuring the main window
        setTitle("NHL Stat Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 750);
        setLocationRelativeTo(null);

        //Defining the columns for the player information table
        String[] columns = {"Name", "Team", "Goals", "Assists", "Points", "+/-"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        //Text fields to allow user to add or update player information
        JPanel inputPanel = new JPanel(new GridLayout(2, 6, 10, 5));
        nameField = new JTextField();
        teamField = new JTextField();
        goalsField = new JTextField();
        assistsField = new JTextField();
        plusMinusField = new JTextField();

        //Labels for each input field
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(new JLabel("Team:"));
        inputPanel.add(new JLabel("Goals:"));
        inputPanel.add(new JLabel("Assists:"));
        inputPanel.add(new JLabel("Plus/Minus:"));
        inputPanel.add(new JLabel(""));

        //text fields for user input
        inputPanel.add(nameField);
        inputPanel.add(teamField);
        inputPanel.add(goalsField);
        inputPanel.add(assistsField);
        inputPanel.add(plusMinusField);

        //Button to add new players to the table
        JButton addButton = new JButton("Add Player");
        inputPanel.add(addButton);

        //A control ribbon which allows the user to filter, sort, remove, update, and reload player data
        JPanel controlRibbon = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));

        //Filtering section will allow users to filter by goals, assists, points, team, and player name
        controlRibbon.add(new JLabel("Filter:"));
        filterTypeBox = new JComboBox<>(new String[]{"Goals", "Assists", "Points", "Team", "Player Name"});
        filterValueField = new JTextField(10);
        JButton filterButton = new JButton("Apply");
        JButton clearFilterButton = new JButton("Clear");
        controlRibbon.add(filterTypeBox);
        controlRibbon.add(filterValueField);
        controlRibbon.add(filterButton);
        controlRibbon.add(clearFilterButton);

        controlRibbon.add(new JLabel(" | "));

        //Sorting section for users to sort either ascending or descending goals, assists, and points
        controlRibbon.add(new JLabel("Sort:"));
        sortBox = new JComboBox<>(new String[]{
                "None", "Goals ↑", "Goals ↓", "Assists ↑", "Assists ↓", "Points ↑", "Points ↓"
        });
        JButton sortButton = new JButton("Apply");
        controlRibbon.add(sortBox);
        controlRibbon.add(sortButton);

        controlRibbon.add(new JLabel(" | "));

        //Buttons for removing, updating, and reloading player data
        JButton removeButton = new JButton("Remove Selected");
        JButton updateButton = new JButton("Update Selected");
        JButton refreshButton = new JButton("Reload");

        controlRibbon.add(removeButton);
        controlRibbon.add(updateButton);
        controlRibbon.add(refreshButton);

        //Summary bar that will show total players in the system
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryLabel = new JLabel("Total Players: 0", SwingConstants.CENTER);
        summaryPanel.add(summaryLabel, BorderLayout.CENTER);
        summaryPanel.setBorder(BorderFactory.createEtchedBorder());

        //Wrap the bottom section in a scrollable area for better viewing
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(controlRibbon, BorderLayout.CENTER);
        bottomPanel.add(summaryPanel, BorderLayout.SOUTH);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JScrollPane bottomScroll = new JScrollPane(bottomPanel);
        bottomScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        bottomScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        //Main section where all major panels are added to the window
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomScroll, BorderLayout.SOUTH);

        //Event listeners that will perform an action when clicked
        addButton.addActionListener(e -> addPlayer());
        removeButton.addActionListener(e -> removeSelectedPlayer());
        updateButton.addActionListener(e -> updateSelectedPlayer());
        refreshButton.addActionListener(e -> loadPlayersIntoTable());
        filterButton.addActionListener(e -> applyFilter());
        clearFilterButton.addActionListener(e -> loadPlayersIntoTable());
        sortButton.addActionListener(e -> applySort());


        loadPlayersIntoTable();
        setVisible(true);
    }

    //Loads players from the statmanager class
    private void loadPlayersIntoTable() {
        tableModel.setRowCount(0);
        List<Player> players = manager.getAllPlayers();
        for (Player p : players) {
            tableModel.addRow(new Object[]{
                    p.getName(), p.getTeam(), p.getGoals(),
                    p.getAssists(), p.getPoints(), p.getPlusMinus()
            });
        }
        //Updates the total player count on the summary bar
        updateSummary(players);
    }

    //Adds a new player when the user fills out the text fields and click add player
    private void addPlayer() {
        try {
            String name = nameField.getText().trim();
            String team = teamField.getText().trim();

            if (name.isEmpty() || team.isEmpty() || name.matches("\\d+") || team.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Invalid input: Name and Team must be text.");
                return;
            }
            //Converts the numeric fields
            int goals = Integer.parseInt(goalsField.getText().trim());
            int assists = Integer.parseInt(assistsField.getText().trim());
            int plusMinus = Integer.parseInt(plusMinusField.getText().trim());

            Player newPlayer = new Player(name, team, goals, assists, plusMinus);
            boolean success = manager.addPlayer(newPlayer);

            //Refreshes the table and clears the input fields
            if (success) {
                JOptionPane.showMessageDialog(this, "Player added successfully!");
                loadPlayersIntoTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Duplicate player detected.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Goals, assists, and plus/minus must be integers.");
        }
    }

    //Removes the selected player from the table and file
    private void removeSelectedPlayer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a player first.");
            return;
        }
        String name = tableModel.getValueAt(row, 0).toString();
        if (manager.removePlayer(name)) {
            JOptionPane.showMessageDialog(this, "Player removed.");
            loadPlayersIntoTable();
        }
    }

    //Updates the selected players data based on the input fields
    private void updateSelectedPlayer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a player first.");
            return;
        }
        try {
            String name = tableModel.getValueAt(row, 0).toString();
            String team = teamField.getText().trim();
            int goals = Integer.parseInt(goalsField.getText().trim());
            int assists = Integer.parseInt(assistsField.getText().trim());
            int plusMinus = Integer.parseInt(plusMinusField.getText().trim());

            if (manager.updatePlayerGUI(name, team, goals, assists, plusMinus)) {
                JOptionPane.showMessageDialog(this, "Player updated successfully!");
                loadPlayersIntoTable();
                clearFields();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Goals, assists, and plus/minus must be integers.");
        }
    }

    //Allows the user to filter the player data by goals, assists, points, team name, or player name
    private void applyFilter() {
        String type = filterTypeBox.getSelectedItem().toString();
        String value = filterValueField.getText().trim();

        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a value to filter.");
            return;
        }

        try {
            List<Player> filtered = switch (type) {
                case "Goals" -> manager.filterByGoals(Integer.parseInt(value));
                case "Assists" -> manager.filterByAssists(Integer.parseInt(value));
                case "Points" -> manager.filterByPoints(Integer.parseInt(value));
                case "Team" -> manager.filterByTeam(value);
                case "Player Name" -> manager.searchByName(value)
                        .map(List::of).orElse(List.of());
                default -> List.of();
            };
            populateTable(filtered);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Use numbers for Goals, Assists, or Points.");
        }
    }

    //Sorts the players by a user selected sorting criteria
    private void applySort() {
        String sortType = sortBox.getSelectedItem().toString();
        List<Player> players = new ArrayList<>(manager.getAllPlayers());

        Comparator<Player> comparator = switch (sortType) {
            case "Goals ↑" -> Comparator.comparingInt(Player::getGoals);
            case "Goals ↓" -> Comparator.comparingInt(Player::getGoals).reversed();
            case "Assists ↑" -> Comparator.comparingInt(Player::getAssists);
            case "Assists ↓" -> Comparator.comparingInt(Player::getAssists).reversed();
            case "Points ↑" -> Comparator.comparingInt(Player::getPoints);
            case "Points ↓" -> Comparator.comparingInt(Player::getPoints).reversed();
            default -> null;
        };

        if (comparator != null) {
            players = players.stream().sorted(comparator).collect(Collectors.toList());
        }

        populateTable(players);
    }

    //Refreshes the table to display and changes to player data
    private void populateTable(List<Player> list) {
        tableModel.setRowCount(0);
        for (Player p : list) {
            tableModel.addRow(new Object[]{
                    p.getName(), p.getTeam(), p.getGoals(),
                    p.getAssists(), p.getPoints(), p.getPlusMinus()
            });
        }
        updateSummary(list);
    }

    //Updates the total player count in the summary bar
    private void updateSummary(List<Player> list) {
        summaryLabel.setText("Total Players: " + list.size());
    }

    //Clears the text fields after a command is executed
    private void clearFields() {
        nameField.setText("");
        teamField.setText("");
        goalsField.setText("");
        assistsField.setText("");
        plusMinusField.setText("");
    }

    //Main method to launch the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(NHLTrackerGUI::new);
    }
}
