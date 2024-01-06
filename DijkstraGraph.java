import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     * @param map the map that the graph uses to map a data object to the node
     *        object it is stored in
     */
    public DijkstraGraph(MapADT<NodeType, Node> map) {
        super(map);
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        //If the graph does not contain the start or end node, path is impossible
        if (!containsNode(start) || !containsNode(end)){
            throw new NoSuchElementException("There is no start or end node");
        }

        //Creates PlaceholderMap to keep track of visited nodes
        PlaceholderMap<NodeType,Boolean> visitedNodes = new PlaceholderMap<>();
        //Creates priorityQueue to greedily keep track of edges and nodes
        PriorityQueue<SearchNode> priorityQueue = new PriorityQueue<>();

        //Creates the initial start node and adds it to priority queue
        Node initStartNode = nodes.get(start);
        SearchNode startNode = new SearchNode(initStartNode,0,null);
        priorityQueue.add(startNode);

        //Iterates through priority queue, cheapest node is at beginning
        while (!priorityQueue.isEmpty()){
            SearchNode currentNode = priorityQueue.poll();

            //If the node in the path has already been visited, ignore and move to next
            if (visitedNodes.containsKey(currentNode.node.data)){
                continue;
            }

            //We visit the node and put it into map
            visitedNodes.put(currentNode.node.data, true);

            //If the node we just visited has the data for the end node, we found the path
            if (currentNode.node.data.equals(end)){
                return currentNode;
            }

            //Traces through all neighboring nodes
            for (Edge edge : currentNode.node.edgesLeaving){
                Node neighbor = edge.successor;
                //If we have not already visited
                if (!visitedNodes.containsKey(neighbor.data)){
                    //Keep track of cost to get to node
                    double lowestCost = currentNode.cost + edge.data.doubleValue();
                    //Add node to priority queue
                    SearchNode neighborNode = new SearchNode(neighbor,lowestCost,currentNode);
                    priorityQueue.add(neighborNode);
                }
            }

        }

        //If we reach this statement then there is not a path from the start to end node
        throw new NoSuchElementException("No path from start to end node");
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        try {
            //Finds the nodes connected to end node
            SearchNode endNode = computeShortestPath(start, end);
            //Creates a new list to put path into
            LinkedList<NodeType> shortestPath = new LinkedList<>();

            //While the node is not null, we add its predecessor first into a linked list to keep
            // track of path
            while (endNode != null) {
                shortestPath.addFirst(endNode.node.data);
                endNode = endNode.predecessor;
            }
            return shortestPath;
            //Catch exceptions from computeShortestPath and throw the same exception
        }catch (NoSuchElementException e){
            throw e;
        }
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        try {
            //Find path from start to end node
            SearchNode endNode = computeShortestPath(start,end);

            //Returns cost of end node - is the total cost to get from start to end
            return endNode.cost;
            //Catch exceptions from computeShortestPath and throw the same exception
        }catch (NoSuchElementException e){
            throw e;
        }
    }

    @Test
    public void testShortestPath(){
        DijkstraGraph<String,Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        //Inserts A-F Nodes
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");

        //Connects nodes with edges of different weights
        graph.insertEdge("A","B",1);
        graph.insertEdge("A","C",2);
        graph.insertEdge("B","F",3);
        graph.insertEdge("C","D",2);
        graph.insertEdge("D","E",2);
        graph.insertEdge("E","F",2);

        //Makes sure shortest cost from A-F is 4
        assertEquals(4,graph.shortestPathCost("A","F"),0);
        //Shortest path contains all nodes stepped through
        assertEquals("[A, B, F]",graph.shortestPathData("A","F").toString());

    }

    @Test
    public void testShortestPath2(){
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        //Inserts A-F Nodes
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");

        //Connects nodes with edges of different weights
        graph.insertEdge("A","B",1);
        graph.insertEdge("A","C",2);
        graph.insertEdge("B","F",3);
        graph.insertEdge("C","D",2);
        graph.insertEdge("D","E",2);
        graph.insertEdge("E","F",2);


        //Makes sure shortest cost from A-E is 4
        Assertions.assertEquals(6, graph.shortestPathCost("A", "E"));
        //Shortest path contains all nodes stepped through
        Assertions.assertEquals("[A, C, D, E]",graph.shortestPathData("A","E").toString());

    }
    @Test
    public void testNoPath(){
        DijkstraGraph<String,Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        //Inserts A-F Nodes
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");

        //Connects nodes with edges of different weights
        graph.insertEdge("A","B",1);
        graph.insertEdge("A","C",2);
        graph.insertEdge("B","F",3);
        graph.insertEdge("C","D",2);
        graph.insertEdge("D","E",2);
        graph.insertEdge("E","F",2);

        //Returns null when there is no path
        Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathCost("B",
                "A"));
        //Returns NoSuchElementException when there is no path
        Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathData("B", "A"));
    }

    @Test
    public void testOneNodePath(){
        DijkstraGraph<String,Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        //Inserts A Node
        graph.insertNode("A");
        //Checks shortest path to itself is 0
        Assertions.assertEquals(0, graph.shortestPathCost("A", "A"));

    }

    @Test
    public void noPathTest(){
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        graph.insertNode("A");
        graph.insertNode("B");

        Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathData("B", "A"));
        Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathCost("B", "A"));
    }
}
