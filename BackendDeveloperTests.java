import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class BackendDeveloperTests {

    /**
     * Test read data method
     * A data path should be found, otherwise an exception will be thrown
     */
    @Test
    public void testReadData(){
        String validDataPath = "flights.dot";
        String invalidDataPath = "invalid.pdf";
        BackendInterface backend = new Backend(new DijkstraGraph(new PlaceholderMap()));
        Assertions.assertDoesNotThrow(()->backend.readDataFromFile(validDataPath),
                "The data path should be valid !");
        Assertions.assertThrows(IOException.class,()->backend.readDataFromFile(invalidDataPath),
                "The data path should be invalid !");
    }

    /**
     * Test getShortestRoute method
     * the start airport and end airport should not be null, otherwise an exception will be thrown
     */
    @Test
    public void testGetShortestRoute(){
        BackendInterface backend = new Backend(new DijkstraGraph(new PlaceholderMap()));
        String start = "ORD", end = "SMF";//connected start and end
        try {
            backend.readDataFromFile("flights.dot");
            Assertions.assertDoesNotThrow(()->backend.getShortestRoute(start,end));
            Assertions.assertThrows(NullPointerException.class,()->backend.getShortestRoute(start,null),
                    "An exception should be thrown when end airport is null !");
            Assertions.assertThrows(NullPointerException.class,()->backend.getShortestRoute(null,end),
                    "An exception should be thrown when start airport is null !");
            Assertions.assertThrows(NullPointerException.class,()->backend.getShortestRoute(null,null),
                    "An exception should be thrown when both start and end airport are null !");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Test getDatasetStatistics method
     * Before loading data, this method should return a string contains data with all '0'
     * After loading data, this method should return a string contains data without '0
     */
    @Test
    public void testGetDatasetStatistics(){
        BackendInterface backend = new Backend(new DijkstraGraph(new PlaceholderMap()));
        String expectedResult = "Number of airports: 0\n" +
                "Number of flights: 0\nTotal Miles for All Flights: 0";// expected result without loading data
        Assertions.assertEquals(expectedResult,backend.getDatasetStatistics(),
                "the expected result should be: "+expectedResult+" while the data is not loaded");

        try{
            expectedResult = "Number of airports: 58\n" +
                "Number of flights: 3196\nTotal Miles for All Flights: 4284914";//assuming the data are more than 0, after loading data
            backend.readDataFromFile("flights.dot");
            Assertions.assertEquals(expectedResult,backend.getDatasetStatistics(),
                    "the expected result should be: "+expectedResult+" while the data is loaded");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Test getRoute method in ShortestPath.java, with specific route
     * From AUS to LGA, check the data in ShortestPath: route list,miles list and total miles
     */
    @Test
    public void testSpecificRoute1(){
        BackendInterface backend = new Backend(new DijkstraGraph(new PlaceholderMap()));
        String start = "AUS", end = "LGA";//connected start and end
        //the airport on route should be "AUS","BNA","LGA" in order
        String[] expectedRoute = new String[]{"AUS","BNA","LGA"};
        int[] expectedMiles = new int[]{753,764};//the distance between airports: AUS--BNA:753, BNA--LGA:764
        int expectedTotalMiles = 1517;
        try {
            backend.readDataFromFile("flights.dot");
            ShortestPathInterface shortestPath = backend.getShortestRoute(start,end);
            List<String> routList = shortestPath.getRoute();
            List<Integer> milesList = shortestPath.getMiles();
            int index = 0;
            Assertions.assertEquals(expectedTotalMiles,shortestPath.getTotalMiles(),
                    "The total miles should be "+expectedTotalMiles+" !");
            for(int mile:expectedMiles){
                String expectedString = "The route should be "+expectedRoute[index]+" -> "+expectedRoute[index+1]+" with "+mile;
                Assertions.assertEquals(expectedString,"The route should be "+routList.get(index)+" -> "+routList.get(index+1)+" with "+milesList.get(index),
                        expectedString+"!");
                index++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
