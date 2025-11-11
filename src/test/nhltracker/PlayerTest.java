package test.nhltracker;
import nhltracker.Player;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/*
Unit test for the player class. This test will verify that the constructor starts fields correctly, the getters and
setters behave as expected, players are formatted correctly, and negative values in +/- are allowed
 */
public class PlayerTest {

    private Player player;


    //@BeforeEach will make a new player object so that test data does not carry over
    //The setUp() method will create a test player with set values
    @BeforeEach
    public void setUp() {
        player = new Player("Connor McDavid", "Edmonton Oilers", 35, 60, 25);
    }

    /* This method will test the constructors assign all player stats and that the getters will return the same value
       Also verifies that the getPoints() method correctly calculates goals + assists
     */
    @Test
    public void testConstructorAndGetters() {
        assertEquals("Connor McDavid", player.getName(), "Name should match constructor input");
        assertEquals("Edmonton Oilers", player.getTeam(), "Team should match constructor input");
        assertEquals(35, player.getGoals(), "Goals should match constructor input");
        assertEquals(60, player.getAssists(), "Assists should match constructor input");
        assertEquals(25, player.getPlusMinus(), "Plus/Minus should match constructor input");
        assertEquals(95, player.getPoints(), "Points should be goals + assists");
    }

    /*
     Test that all setter methods correctly update player data.
     After changing the values the getters should return updated results.
     */
    @Test
    public void testSettersUpdateValues() {
        //Each player field being updated by setters
        player.setName("Leon Draisaitl");
        player.setTeam("Edmonton Oilers");
        player.setGoals(40);
        player.setAssists(55);
        player.setPlusMinus(10);

        //Verify that updated values are shown correctly
        assertEquals("Leon Draisaitl", player.getName());
        assertEquals("Edmonton Oilers", player.getTeam());
        assertEquals(40, player.getGoals());
        assertEquals(55, player.getAssists());
        assertEquals(10, player.getPlusMinus());
        //Checking to make sure points were recalculated after a player was edited
        assertEquals(95, player.getPoints(), "Points should update correctly after stat changes");
    }

    /* testToStringFormatting() method will make sure all player information is formatted correctly
    in the test text file and is formatted for display when output
     */

    @Test
    public void testToStringFormatting() {
        String output = player.toString();
        assertTrue(output.contains("Connor McDavid"), "toString() should include player name");
        assertTrue(output.contains("Edmonton Oilers"), "toString() should include team");
        assertTrue(output.contains("35"), "toString() should include goals");
        assertTrue(output.contains("60"), "toString() should include assists");
        assertTrue(output.contains("25"), "toString() should include plus/minus");
    }

    /*
     Test that negative plus/minus values are supported
     Ensures that the setter doesn’t restrict plus/minus to positive numbers.
     */
    @Test
    public void testNegativePlusMinusAllowed() {
        player.setPlusMinus(-10);
        assertEquals(-10, player.getPlusMinus(), "Plus/minus should allow negative values");
    }
}
