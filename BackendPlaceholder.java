import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class that acts as a placeholder for the backend implementation, that is made for Frontend.
 */
public class BackendPlaceholder implements BackendInterface{
    GraphADT graphtADT;

    /**
     * Constructor for Backend
     */
    public BackendPlaceholder(){
    }
    /**
     * Reads in data from a file.
     * @param filePath - file to read in
     */
    @Override
    public void readDataFromFile(String filePath) throws IOException{
        //We don't actually have to read in the file for this placeholder
        try {
            if (filePath == null) {
                throw new FileNotFoundException("filePath is null");
            }
            System.out.println(filePath);
        }catch (IOException e){
            throw e;
        }
    }
    /**
     * Shortest path in between two airports
     * @param start - start airport
     * @param destination - ending airport
     * @return shortestPath between two airports
     */
    @Override
    public ShortestPathInterface getShortestRoute(String start, String destination) throws NoSuchElementException {
        //Creates new shortest path
        ShortestPathInterface shortestPath = new ShortestPathPlaceholder();
        return shortestPath;
    }

    /**
     * Returns data for shortest path in between two airports
     * @return String of statistics for shortest route between two airports
     */
    @Override
    public String getDatasetStatistics() {
        String placeholder = "3 airports, 2 flights, 180 miles";
        return placeholder;
    }
}
