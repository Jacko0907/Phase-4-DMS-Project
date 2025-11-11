package test.nhltracker;
import nhltracker.FileHandler;
import nhltracker.Player;
import nhltracker.StatManager;
import org.junit.jupiter.api.*;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/*
Unit test for the StatManager class. These tests will verify that StatManager correctly handles player management
like adding players and preventing duplicate players.
 */
public class StatManagerTest {   
    private StatManager manager;
    private FileHandler fileHandler;
    private File tempFile;

    //This method will create a temp file for storing players before a test and a StatManager instance
    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("players", ".txt");
        fileHandler = new FileHandler(tempFile.getAbsolutePath());
        manager = new StatManager(fileHandler);
    }

    //Deletes testing file so that a new one is created each test and test data does not get carried over
    @AfterEach
    public void tearDown() {
        tempFile.delete();
    }

    /* testAddPlayer() method will create a player entry, then add it to the StatManager,
    and verify that the player was added
     */
    @Test
    public void testAddPlayer() {
        Player player = new Player("Connor McDavid", "Edmonton Oilers", 35, 60, 25);
        boolean added = manager.addPlayer(player);
        assertTrue(added, "Player should be added successfully");
    }
    //This method will test to make sure StatManager does not allow for duplicate players.
    //Adding a second player with the same name will return false
    @Test
    public void testPreventDuplicatePlayers() {
        Player p1 = new Player("Connor McDavid", "Edmonton Oilers", 35, 60, 25);
        Player p2 = new Player("Connor McDavid", "Edmonton Oilers", 30, 55, 20);
        manager.addPlayer(p1);
        boolean added = manager.addPlayer(p2);
        assertFalse(added,  "Duplicate player should not be added");
    }
}
