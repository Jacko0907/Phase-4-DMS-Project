package nhltracker;

//Player class that holds each players information
public class Player {
    private String name;
    private String team;
    private int goals;
    private int assists;
    private int plusMinus;
    //Player formats how each player will be displayed (Player name,Team name, Goals,Assists,PlusMinus)
    public Player(String name, String team, int goals, int assists, int plusMinus) {
        this.name = name;
        this.team = team;
        this.goals = goals;
        this.assists = assists;
        this.plusMinus = plusMinus;
    }
    //Adds in the getPoints method to add goals and assists together for a point total
    public String getName() { return name; }
    public String getTeam() { return team; }
    public int getGoals() { return goals; }
    public int getAssists() { return assists; }
    public int getPoints() { return goals + assists; }
    public int getPlusMinus() { return plusMinus; }

    public void setName(String name) { this.name = name; }
    public void setTeam(String team) { this.team = team; }
    public void setGoals(int goals) { this.goals = goals; }
    public void setAssists(int assists) { this.assists = assists; }
    public void setPlusMinus(int plusMinus) { this.plusMinus = plusMinus; }

    //Final display format (Player name, Team name, Goals, Assists, Points, plusMinus)
    @Override
    public String toString() {
        return String.format("%-20s %-15s Goals: %-3d Assists: %-3d Points: %-3d +/-: %-3d",
                name, team, goals, assists, getPoints(), plusMinus);
    }
}
