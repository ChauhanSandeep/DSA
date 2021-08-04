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

    /**
     * When you run random custom test cases in editor you will get to know that they require a topological sort to be done on the input.
     * For ex feeding [["JFK",NRT],["JFK",KUL]] returns ["JFK","NRT","KUL"] which seems wrong as per the explanation but since input is not a valid itinerary hence the result.
     * This problem needs a topological sort in short. Hence do a topological sort after storing nodes in a sorted order.
     * Note :-
     **Topological sort is used only for DAGs** hence we need to *remove the edges* once it is visited. Thats why the solution uses a priority queue which sorts the nodes as well as helps in removing it in an efficient way.
     */
    public List<String> findItinerary(List<List<String>> tickets) {
        LinkedList<String> result = new LinkedList<>();
        HashMap<String,PriorityQueue<String>> graph = new HashMap<>();
        for(List<String> ticket : tickets){
            String source = ticket.get(0);
            String dest = ticket.get(1);
            graph.putIfAbsent(source,new PriorityQueue<>());
            graph.get(source).offer(dest);
        }
        DFS("JFK",graph,result); // we need to do DFS/topological sort only from "JFK"
        return result;
    }
    /*DFS doing topological sort*/
    private void DFS(String source,HashMap<String,PriorityQueue<String>> graph,LinkedList<String> result ){
        PriorityQueue<String> destinations = graph.get(source);
        while(destinations!= null && !destinations.isEmpty()) {
            DFS(destinations.poll(),graph,result);
        }
        result.addFirst(source); // this is the key, instead of reversing add to the head of linked list.
    }


}
