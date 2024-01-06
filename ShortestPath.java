import java.util.List;

public class ShortestPath implements ShortestPathInterface{

    private List<String> route;
    private List<Integer> miles;
    private int totalMiles;

    public ShortestPath(List<String> route, List<Integer> miles, int totalMiles){
        this.route = route;
        this.miles = miles;
        this.totalMiles = totalMiles;
    }

    @Override
    public List<String> getRoute() {
        return route;
    }

    @Override
    public List<Integer> getMiles() {
        return miles;
    }

    @Override
    public int getTotalMiles() {
        return totalMiles;
    }
}
