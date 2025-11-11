package test.nhltracker;
import nhltracker.FileHandler;
import nhltracker.Player;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
/* Unit test for the FileHandler class. This class verifies that saving and loading player data to or from a file
   works correctly, it will also check for missing text files.  */
public class FileHandlerTest {

    //Creating a temporary file for testing
    private File tempFile;
    private FileHandler fileHandler;

    //This method runs automatically
    //It creates a temp file and has FileHandler use the file
    @BeforeEach
    public void setUp() throws IOException {
        //Create a temporary file for each test
        tempFile = File.createTempFile("players_test", ".txt");
        fileHandler = new FileHandler(tempFile.getAbsolutePath());
    }

    //This method will delete the players_test file after each test so that a new file is created each time
    @AfterEach
    public void tearDown() {
        tempFile.delete();
    }

    //Test for saving players makes a valid data entry in the players_test file
    //This test will ensure that saving will return true and that the file exists
    @Test
    public void testSavePlayersCreatesFile() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Connor McDavid", "Edmonton Oilers", 35, 60, 25));
        players.add(new Player("Nathan MacKinnon", "Colorado Avalanche", 40, 55, 22));
        boolean saved = fileHandler.savePlayers(players);
        //Success message will verify that the save was successful
        assertTrue(saved, "FileHandler should return true on successful save");
        //Verify that the test file exists and is not empty
        assertTrue(tempFile.exists(), "File should exist after saving");
        assertTrue(tempFile.length() > 0, "File should not be empty after saving");
    }

    //Test that the loadPlayers() can read player data from a valid file
    //This test will manually write CSV data to the file then load it
    @Test
    public void testLoadPlayersReadsCorrectData() throws IOException {
        //Write test data manually to file
        String testData = "Connor McDavid,Edmonton Oilers,35,60,25\n" +
                "Nathan MacKinnon,Colorado Avalanche,40,55,22\n";
        Files.writeString(tempFile.toPath(), testData);

        //Load players from players_test file
        List<Player> players = fileHandler.loadPlayers();

        //Verify that 2 players were loaded from the test file
        assertEquals(2, players.size(), "Should load 2 players from file");

        assertEquals("Connor McDavid", players.get(0).getName());
        assertEquals("Edmonton Oilers", players.get(0).getTeam());
        assertEquals(35, players.get(0).getGoals());
        assertEquals(60, players.get(0).getAssists());
        assertEquals(25, players.get(0).getPlusMinus());
    }

    //This test checks to see if loadPlayers() handles the missing file error correctly
    @Test
    public void testLoadPlayersHandlesMissingFile() {
        //Create a Filehandler that leads to a non-existent file
        FileHandler missingFileHandler = new FileHandler("non_existent_file.txt");
        List<Player> players = missingFileHandler.loadPlayers();
        //Verify that the list is empty instead of throwing an error
        assertTrue(players.isEmpty(), "Should return an empty list if file does not exist");
    }

    //The integration test verifies that data is being saved to savePlayers() and loaded using loadPlayers()
    @Test
    public void testSaveAndLoadIntegration() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Sidney Crosby", "Pittsburgh Penguins", 33, 47, 10));
        players.add(new Player("Auston Matthews", "Toronto Maple Leafs", 45, 35, 15));

        boolean saved = fileHandler.savePlayers(players);
        assertTrue(saved, "Saving should succeed");

        List<Player> loadedPlayers = fileHandler.loadPlayers();

        assertEquals(2, loadedPlayers.size(), "Should reload the same number of players");
        assertEquals("Sidney Crosby", loadedPlayers.get(0).getName());
        assertEquals("Pittsburgh Penguins", loadedPlayers.get(0).getTeam());
    }
}
