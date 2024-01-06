import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Backend implements BackendInterface {

    private GraphADT graphADT;
    private long totalMiles = 0;//to record total distance of the whole graph

    public Backend(GraphADT graphADT){
        this.graphADT = graphADT;
    }

    /**
     * Reads graphics data from a file
     *
     * @param filePath The path to the DOT file.
     * @throws IOException If an error occurred while reading the file.
     */
    @Override
    public void readDataFromFile(String filePath) throws IOException {

        Scanner scanner = new Scanner(new File(filePath));
        Pattern pattern = Pattern.compile("^\\s+\"(\\w+)\"\\s\\[");
        while (scanner.hasNextLine()){//insert nodes at first, then insert edges in next steps
            String line = scanner.nextLine();
            Matcher matcher = pattern.matcher(line);
            if(matcher.find()){
                graphADT.insertNode(matcher.group(1));
            }
        }
        scanner = new Scanner(new File(filePath));
        pattern = Pattern.compile("^\\s+\"(\\w+)\"\\s--\\s\"(\\w+)\"\\s\\[miles=(\\d+)\\];");
        while (scanner.hasNextLine()){//insert edges
            String line = scanner.nextLine();
            Matcher matcher = pattern.matcher(line);
            if(matcher.find()){// build undirected graph
                graphADT.insertEdge(matcher.group(1),matcher.group(2),Integer.parseInt(matcher.group(3)));
                graphADT.insertEdge(matcher.group(2),matcher.group(1),Integer.parseInt(matcher.group(3)));
                totalMiles += Integer.parseInt(matcher.group(3));
            }
        }
        totalMiles *= 2;// due to the undirected graph, the total miles should be doubled
    }

    /**
     * Calculates the shortest path from the starting point to the destination airport.
     *
     * @param start The identifier of the start airport.
     * @param destination The identifier of the destination airport.
     * @return An instance of the shortest path search result.
     * @throws NoSuchElementException If the start or destination airport does not exist.
     */
    @Override
    public ShortestPath getShortestRoute(String start, String destination) throws NoSuchElementException {
        List<String> routeList = graphADT.shortestPathData(start,destination);//obtaining route list
        int totalCost = (int) graphADT.shortestPathCost(start,destination);
        List<Integer> milesList = new ArrayList<>();
        for(int i=0;i<routeList.size()-1;i++){//after obtaining the route list, use it to load miles list
            //each data of distance between joint airports can be calculated by calling shortestPathCost method
            milesList.add((int) graphADT.shortestPathCost(routeList.get(i),routeList.get(i+1)));
        }

        return new ShortestPath(routeList,milesList,totalCost);
    }

    /**
     * Get statistics for the dataset, including the number of airports, the number of flights, and the total number of miles for all flights.
     *
     * @return A string describing the statistics of the dataset.
     */
    @Override
    public String getDatasetStatistics() {
        return "Number of airports: "+graphADT.getNodeCount()+
                "\nNumber of flights: "+graphADT.getEdgeCount()+
                "\nTotal Miles for All Flights: "+totalMiles;
    }
}
