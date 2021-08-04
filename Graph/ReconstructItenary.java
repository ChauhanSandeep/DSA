package Graph;
import java.util.*;
import java.util.stream.Collectors;

/**
 * https://leetcode.com/problems/reconstruct-itinerary/
 */
public class ReconstructItenary {

    Map<String, PriorityQueue<String>> flights = new HashMap<>();
    LinkedList<String> path = new LinkedList<>();

    public static void main(String[] args) {
//        [["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
        List<List<String>> tickets = new ArrayList<>();
        tickets.add(Arrays.stream(new String[] {"JFK","SFO"}).collect(Collectors.toList()));
        tickets.add(Arrays.stream(new String[] {"JFK","ATL"}).collect(Collectors.toList()));
        tickets.add(Arrays.stream(new String[] {"SFO","ATL"}).collect(Collectors.toList()));
        tickets.add(Arrays.stream(new String[] {"ATL","JFK"}).collect(Collectors.toList()));
        tickets.add(Arrays.stream(new String[] {"ATL","SFO"}).collect(Collectors.toList()));

        List<String> result = new ReconstructItenary().findItinerary(tickets);
        System.out.println(result);
    }

    private List<String> findItinerary(List<List<String>> tickets) {
        for(List<String> ticket: tickets) {
            String source = ticket.get(0);
            String destination = ticket.get(1);

            flights.putIfAbsent(source, new PriorityQueue<>());
            flights.get(source).add(destination);
        }
        System.out.println(flights);

        dfs("JFK");
        return path;
    }

    private void dfs(String source) {
        PriorityQueue<String> destinations = flights.get(source);
        while(destinations != null && !destinations.isEmpty()) {
            dfs(destinations.poll());
        }

        path.addFirst(source);
        System.out.println(path);
    }


}
