import java.util.Scanner;
/**
 * An interface for the frontend
 */
public interface FrontendInterface {
    
    /**
     * Constructor for the FrontendInterface which accepts a reference to the
     * backend and a java.util.Scanner instance to read user input.
     *
     * @param backend The backend instance.
     * @param scanner The scanner instance to read user input.
     */
    //public FrontendInterface(BackendInterface backend, Scanner scanner)

    /**
     * A method to interactively loop prompting the user to select a command and input their data.
     */
    public void runMainLoop();

    /**
     * Prompts the user to specify and load data from a file.
     */
    public void loadDataFile(BackendInterface backend, Scanner scanner);
    /**
     * A method to print flight statistics such as the number of airports, the number of flights,
     * and the total number of miles
     */
    public void printFlightStatistics();

    /**
     * Asks user for a start and destination airport, then lists the shortest route between those
     * airports.
     *
     * @param start The original location
     * @param end The final location to reach
     */
    public void shortestRoute(String start, String end);

    /**
     * A method to quit the app.
     */
    public void quit();
}

