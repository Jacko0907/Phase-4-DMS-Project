package nhltracker;
//Player class that holds each players information
/**
 * Represents an NHL player with their associated statistics such as Name, Team name, goals, assists, and plus/minus.
 * <p>
 * This class acts as a data model for both CLI and GUI operations in the NHL Stat Tracker system.
 * </p>
 */
public class Player {
    /** The player's full name. */
    private String name;
    /** The team the player belongs to. */
    private String team;
    /** Number of goals scored by the player. */
    private int goals;
    /** Number of assists recorded by the player. */
    private int assists;
    /** The player's plus/minus rating. */
    private int plusMinus;
    /**
     * Constructs a Player object with the given attributes.
     *
     * @param name      the player's name
     * @param team      the team the player belongs to
     * @param goals     the number of goals scored by the player
     * @param assists   the number of assists made by the player
     * @param plusMinus the player's plus/minus rating
     */
    //Player formats how each player will be displayed (Player name,Team name, Goals,Assists,PlusMinus)
    public Player(String name, String team, int goals, int assists, int plusMinus) {
        this.name = name;
        this.team = team;
        this.goals = goals;
        this.assists = assists;
        this.plusMinus = plusMinus;
    }
    //Adds in the getPoints method to add goals and assists together for a point total
    /**
     * Returns the player's name.
     *
     * @return the player's name
     */
    public String getName() { return name; }
    /**
     * Returns the team the player belongs to.
     *
     * @return the player's team name
     */
    public String getTeam() { return team; }
    /**
     * Returns the total number of goals scored by the player.
     *
     * @return the player's goals
     */
    public int getGoals() { return goals; }
    /**
     * Returns the total number of assists recorded by the player.
     *
     * @return the player's assists
     */
    public int getAssists() { return assists; }
    /**
     * Returns the total points (goals + assists) for the player.
     *
     * @return the player's total points
     */
    public int getPoints() { return goals + assists; }
    /**
     * Returns the player's plus/minus rating.
     *
     * @return the player's plus/minus value
     */
    public int getPlusMinus() { return plusMinus; }
    /**
     * Updates the player's name.
     *
     * @param name the new name for the player
     */
    public void setName(String name) { this.name = name; }
    /**
     * Updates the player's team.
     *
     * @param team the new team name
     */
    public void setTeam(String team) { this.team = team; }
    /**
     * Updates the player's goal count.
     *
     * @param goals the new goal total
     */
    public void setGoals(int goals) { this.goals = goals; }
    /**
     * Updates the player's assist count.
     *
     * @param assists the new assist total
     */
    public void setAssists(int assists) { this.assists = assists; }
    /**
     * Updates the player's plus/minus rating.
     *
     * @param plusMinus the new plus/minus value
     */
    public void setPlusMinus(int plusMinus) { this.plusMinus = plusMinus; }

    /**
     * Returns a formatted string representing the player's statistics.
     * <p>
     * The output includes the player's name, team, goals, assists, total points, and plus/minus rating.
     * </p>
     *
     * @return a formatted string representation of the player's stats
     */
    //Final display format (Player name, Team name, Goals, Assists, Points, plusMinus)
    @Override
    public String toString() {
        return String.format("%-20s %-15s Goals: %-3d Assists: %-3d Points: %-3d +/-: %-3d",
                name, team, goals, assists, getPoints(), plusMinus);
    }
}
