import java.util.ArrayList;
import java.util.List;

/**
 * Placeholder class for shortest path object
 */
public class ShortestPathPlaceholder implements ShortestPathInterface{
    /**
     * Gets the route from starting airport to end airport
     * @return list of route between two airports
     */
    @Override
    public List<String> getRoute() {
        ArrayList<String> placeHolder = new ArrayList<>();
        placeHolder.add("Milwaukee");
        placeHolder.add("Madison");
        placeHolder.add("Denver");
        return placeHolder;
    }

    /**
     * Gets the miles between different airports
     * @return list of miles between airports
     */
    @Override
    public List<Integer> getMiles() {
        ArrayList<Integer> placeHolderInt = new ArrayList<>();
        placeHolderInt.add(60);
        placeHolderInt.add(60);
        return placeHolderInt;
    }

    /**
     * Total miles from beginning to end airport
     * @return int of total miles
     */
    @Override
    public int getTotalMiles() {
        return 120;
    }
}
