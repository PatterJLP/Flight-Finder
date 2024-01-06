import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;

/**
 * This class implements the frontend code that drives an interactive loop of prompting the user to
 * select a command, then requests any required details about that command from the user, and then
 * displays the results of the command.
 */
public class Frontend implements FrontendInterface {
    private BackendInterface backend;
    private Scanner scanner;

    public static void main(String[] args){
        Backend backend = new Backend(new DijkstraGraph(new PlaceholderMap()));
        Frontend frontend = new Frontend(backend, new Scanner(System.in));

        frontend.loadDataFile(backend,new Scanner(System.in));

    }

    /**
     * Constructor for Frontend class. Takes reference from the backend and scanner.
     *
     * @param backend - Backend reference that we will use to extract information
     * @param scanner - scanner to read user's input
     */
    public Frontend(BackendInterface backend, Scanner scanner) {
        System.out.println("Welcome to the Flight Router app.");
        //Initializes backend and scanner
        this.backend = backend;
        this.scanner = scanner;
    }

    /**
     * Main loop that prompts the user. Asks the user to pick between 3 different commands.
     */
    public void runMainLoop() {
        //Prompts user to select a command and saves it in a string
        System.out.println("Please choose a following command: \n1:Show flight statistics " +
                "\n2:Get the shortest route between two airports \n3:Exit the app");
        String command = scanner.nextLine();

        //Switch statement based on what user inputted
        switch (command) {
            case "1":
                //Case 1 shows flight statistics
                System.out.println("You selected: Show flight statistics.");
                //Calls printFlightStatistics method in Frontend
                printFlightStatistics();
                runMainLoop();
                break;
            case "2":
                //Case 2 gets the shortest route between two airports
                System.out.println("You selected: Get the shortest route between two airports.");

                //Prompts the user for starting and ending airport
                System.out.println("Please enter starting airport: ");
                String startingAirport = scanner.nextLine();
                System.out.println("Please enter destination airport: ");
                String destinationAirport = scanner.nextLine();

                //Calls shortestRoute method in Frontend.
                shortestRoute(startingAirport, destinationAirport);
                runMainLoop();
                break;
            case "3":
                //Case 3 exits the app
                quit();
                break;
            default:
                //If the user does not input 1,2,or 3, outputs error.
                System.out.println("Error: Invalid command. Please enter 1, 2, or 3.");
                //Calls main loop again for user to re-input commands
                runMainLoop();
                break;
        }

    }

    /**
     * Asks the user for a file that contains flight information. Calls backend to read the file if
     * it exists, or if it doesn't outputs an error.
     *
     * @param backend - the backend we will call to read the file.
     * @param scanner - scanner to read user's input
     */
    public void loadDataFile(BackendInterface backend, Scanner scanner) {
        //Prompts user to input a file name
        System.out.print("Please enter a file: ");
        String file = scanner.nextLine();

        try {
            //Calls backend read data method
            backend.readDataFromFile(file);
            //If reached file exists, so we call Main Loop
            runMainLoop();
        } catch (Exception e) {
            //File was not found and prompts user to re-input file
            System.out.println("Error: Please enter a valid file.");
            loadDataFile(backend, scanner);
        }
    }

    /**
     * Outputs flight statistics for the shortest path. Includes number of airports, number of
     * flights, and total miles. Calls backend to get this information
     */
    public void printFlightStatistics() {
        //Calls backend's getDatasetStatistics method and outputs stats into terminal
        System.out.println(backend.getDatasetStatistics() + "\n");

    }

    /**
     * Lists the shortest route between the start and end airports, and the distance between each of
     * them. Also lists the total miles of the flights.
     *
     * @param start - the starting airport the user input
     * @param end   - the destination airport the user input
     */
    public void shortestRoute(String start, String end) {
        //Creates a shortestPath from start to end airport
        ShortestPathInterface shortestRoute = backend.getShortestRoute(start, end);

        //Cannot fly to the same airport so goes back to main prompt
        if (start.equals(end)) {
            System.out.println("Cannot fly to same airport");
            return;
        }

        //If shortest route is possible
        if (shortestRoute != null) {
            //Gets the route and miles from shortest route.
            List<String> route = shortestRoute.getRoute();
            List<Integer> miles = shortestRoute.getMiles();
            int totalMiles = shortestRoute.getTotalMiles();

            System.out.println("Shortest route from " + start + " to " + end + ":");

            //Goes through each airport and gets their miles as well
            for (int i = 0; i < route.size() - 1; i++) {
                System.out.println("From " + route.get(i) + " to " + route.get(i + 1)
                        + ": " + miles.get(i) + " miles");
            }

            System.out.println("Total number of miles from start is: " + totalMiles + "\n");

            //Shortest route is not possible, goes back to main loop.
        } else {
            System.out.println("No route from " + start + "to" + end + "\n");
            return;

        }
    }

    /**
     * A method to exit the app.
     */
    public void quit() {
        //Quits app
        System.out.println("Exiting app.\n");
        return;
    }
}
