import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.util.Scanner;


/**
 * This class contains tests for the FrontendDeveloper class.
 */
public class FrontendDeveloperTests {

    /**
     * Tests trying to read an invalid and valid file for the Backend ReadDataFromFile method.
     */
    @Test
    public void postMergeBackendTest(){
        //Initializes backend
        Backend backend = new Backend(new DijkstraGraph(new PlaceholderMap()));

        //Initializes a real and invalid file
        String invalidFile = "test.dot";
        String realFile = "flights.dot";


        Assertions.assertDoesNotThrow(()->backend.readDataFromFile(realFile));
        Assertions.assertThrows(IOException.class,()->backend.readDataFromFile(invalidFile));

    }
    /**
     * Tests getDataSetStatistic method from backend. Tests the edge case of flights being zero.
     */
    @Test
    public void postMergeBackendTests2(){
        //Initializes backend
        Backend backend = new Backend(new DijkstraGraph(new PlaceholderMap()));

        //Tests actual
        String actual = backend.getDatasetStatistics();
        //Tests expected
        String expected = "Number of airports: 0\n" + "Number of flights: 0\n" + "Total Miles for All Flights: 0";

        //Should all be zero airports, flights, miles
        Assertions.assertEquals(actual,expected);

    }

    /**
     * Tests the integration of backend. This method tests the getDatasetStatistics method from
     * backend.
     */
    @Test
    public void integrationTestDatasetStatistics(){
        TextUITester tester = new TextUITester("flights.dot\n1\n3");

        //Initialize backend and scanner
        BackendInterface backend = new Backend(new DijkstraGraph(new PlaceholderMap()));
        Scanner scnr = new Scanner(System.in);

        //Initializes frontend, will be using to test backend
        Frontend testBackend = new Frontend(backend,scnr);
        testBackend.loadDataFile(backend,scnr);

        //Should contain the correct information
        String output = tester.checkOutput();
        String expected = "Number of airports: ";

        assertTrue(output.contains(expected));

    }
    /**
     * Tests the integration of backend. This method tests the getShortestRoute method from backend.
     */
    @Test
    public void integrationTestShortestRoute(){
        TextUITester tester = new TextUITester("flights.dot\n2\nMIA\nPHL\n3");

        //Initialize backend and scanner
        BackendInterface backend = new Backend(new DijkstraGraph(new PlaceholderMap()));
        Scanner scnr = new Scanner(System.in);

        //Initializes frontend, will be using to test backend
        Frontend testBackend = new Frontend(backend,scnr);
        testBackend.loadDataFile(backend,scnr);

        //Should contain the correct information
        String output = tester.checkOutput();
        String expected = "From MIA to PHL: 1013";
        assertTrue(output.contains(expected));
    }

    /**
     * Tests selecting command 1 in the loop, which calls printFlightStatistics method
     */
    @Test
    public void testCommand1(){
        TextUITester tester = new TextUITester("flights.dot\n1\n3");

        //Create backend and scnr for frontend constructor
        BackendPlaceholder backend = new BackendPlaceholder();
        Scanner scnr = new Scanner(System.in);

        //Create frontend and call prompt loop
        Frontend frontend = new Frontend(backend, scnr);
        frontend.loadDataFile(backend, scnr);

        // Check output
        String output = tester.checkOutput();
        String expectedOutput =  "You selected: Show flight statistics.";
        assertTrue(output.contains(expectedOutput));
    }

    /**
     * Tests selecting command 2 in the loop, which calls shortestRoute method
     */
    @Test
    public void testCommand2(){
        TextUITester tester = new TextUITester("flights.dot\n2\nMilwaukee\nDenver\n3");

        //Create backend and scnr for frontend constructor
        BackendPlaceholder backend = new BackendPlaceholder();
        Scanner scnr = new Scanner(System.in);

        //Create frontend and call prompt loop
        Frontend frontend = new Frontend(backend, scnr);
        frontend.loadDataFile(backend, scnr);

        // Check output
        String output = tester.checkOutput();
        String expectedOutput =  "You selected: Get the shortest route between two airports.";
        assertTrue(output.contains(expectedOutput));
    }

    /**
     * Tests invalid command input into frontend
     */
    @Test
    public void testInvalidInput() {
        TextUITester tester = new TextUITester("flights.dot\n 51\n3");

        //Create backend and scnr for frontend constructor
        BackendPlaceholder backend = new BackendPlaceholder();
        Scanner scnr = new Scanner(System.in);

        //Create frontend and call prompt loop
        Frontend frontend = new Frontend(backend, scnr);
        frontend.loadDataFile(backend, scnr);

        String output = tester.checkOutput();

        //Correctly handled invalid input
        assertTrue(output.contains("Error: Invalid command. "));

    }

    /**
     * Tests loadDataFile method, should load file into frontend
     */
    @Test
    public void testLoadDataFile(){
        //Calls a valid file to test if frontend can load it
        TextUITester tester = new TextUITester("flights.dot\n3");

        //Create backend and scnr for frontend constructor
        BackendPlaceholder backend = new BackendPlaceholder();
        Scanner scnr = new Scanner(System.in);

        //Create frontend and call main loop
        Frontend frontend = new Frontend(backend, scnr);
        frontend.loadDataFile(backend, scnr);

        //Check output
        String output = tester.checkOutput();
        //If output contains select a command, we know the file path worked.
        assertTrue(output.contains("Please choose a following command: \n"));
    }

    /**
     * Tests the quit method, should exit the frontend loop.
     */
    @Test
    public void testQuit(){
        //Choosing command 3 exits the app
        TextUITester tester = new TextUITester("flights.dot\n3\n");

        //Create backend and scnr for frontend constructor
        BackendPlaceholder backend = new BackendPlaceholder();
        Scanner scnr = new Scanner(System.in);

        //Create frontend and call prompt loop
        Frontend frontend = new Frontend(backend, scnr);
        frontend.loadDataFile(backend, scnr);

        // Check output
        String output = tester.checkOutput();
        assertTrue(output.contains("Exiting app."));
    }
}






