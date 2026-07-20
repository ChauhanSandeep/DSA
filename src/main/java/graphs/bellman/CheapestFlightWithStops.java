package graphs.bellman;

import java.util.*;

    /**
     * Intuition: a normal shortest-path state is just the city, but the stop
     * limit means two arrivals at the same city are not equivalent. This method
     * keeps remaining edges in the heap state, so a path is useful when it reaches
     * a city with a cheaper cost for that exact remaining-edge budget.
     *
     * Algorithm:
     *   1. Build the exact adjacency list from each flight source to [destination, price].
     *   2. Push [cost, city, stopsLeft] for the source into a min-heap.
     *   3. Pop cheapest states; if destination is popped, return that cost.
     *   4. When stops remain, push neighbors if this route improves that city/stops-left state.
     *
     * Time:  O(K * E log(K * E)) - each stop layer can push outgoing flights into the heap.
     * Space: O(VK + E) - adjacency list, heap states, and best cost by city/stops-left.
     *
     * @param totalCities number of cities labeled 0 to totalCities - 1
     * @param flights directed flights [from, to, price]
     * @param source starting city
     * @param destination target city
     * @param maxStops maximum allowed intermediate stops
     * @return cheapest valid price, or -1 if no valid route exists
     */
public class CheapestFlightWithStops {

    public static void main(String[] args) {
        CheapestFlightWithStops solver = new CheapestFlightWithStops();
        int[][] flights1 = {{0, 1, 100}, {1, 2, 100}, {0, 2, 50}};
        int[][] flights2 = {{0, 1, 100}};
        int[][] flights3 = {{0, 1, 100}, {0, 2, 1}, {2, 1, 1}, {1, 3, 1}};

        System.out.printf("n=3 flights=%s src=0 dst=2 k=1 -> dijkstra=%d bellman=%d  expected=50%n",
            Arrays.deepToString(flights1),
            solver.findCheapestPriceDijkstra(3, flights1, 0, 2, 1),
            solver.findCheapestPriceBellmanFord(3, flights1, 0, 2, 1));
        System.out.printf("n=3 flights=%s src=0 dst=2 k=1 -> dijkstra=%d bellman=%d  expected=-1%n",
            Arrays.deepToString(flights2),
            solver.findCheapestPriceDijkstra(3, flights2, 0, 2, 1),
            solver.findCheapestPriceBellmanFord(3, flights2, 0, 2, 1));
        System.out.printf("n=4 flights=%s src=0 dst=3 k=2 -> dijkstra=%d bellman=%d  expected=3%n",
            Arrays.deepToString(flights3),
            solver.findCheapestPriceDijkstra(4, flights3, 0, 3, 2),
            solver.findCheapestPriceBellmanFord(4, flights3, 0, 3, 2));
    }


    /**
     * Approach 1: Modified Dijkstra's algorithm that tracks remaining flights.
     *
     * Intuition: reaching the same city with more stops left is not always better
     * if it costs much more. Keep cost by (city, flights-left), so a cheaper path
     * with fewer remaining flights can still continue and win when it has enough
     * budget to reach the destination.
     *
     * Steps:
     * 1. Build adjacency list of the graph.
     * 2. Use a MinHeap (PriorityQueue) to always expand the lowest-cost route.
     * 3. Track the best cost for every city and remaining-flight count.
     * 4. Push a neighbor only when it improves that exact state.
     *
     * Time Complexity: O(K * E log(K * E)) because each stop layer can relax flights.
     * Space Complexity: O(K * V + E) for best costs, heap states, and graph storage.
     */
    public int findCheapestPriceDijkstra(int totalCities, int[][] flights, int source, int destination, int maxStops) {
        // Step 1: Build the adjacency list {source -> [destination, price]}
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] flight : flights) {
            graph.computeIfAbsent(flight[0], k -> new ArrayList<>()).add(new int[]{flight[1], flight[2]});
        }

        // Step 2: Min-Heap (PriorityQueue) -> {costSoFar, currentCity, remainingStops}
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        minHeap.offer(new int[]{0, source, maxStops + 1}); // Cost, city, stops left

        // Step 3: Track the cheapest known cost for each city and remaining flight count
        int maxFlights = maxStops + 1;
        int[][] bestCost = new int[totalCities][maxFlights + 1];
        for (int[] costsByStopsLeft : bestCost) {
            Arrays.fill(costsByStopsLeft, Integer.MAX_VALUE);
        }
        bestCost[source][maxFlights] = 0;

        // Step 4: Process the queue
        while (!minHeap.isEmpty()) {
            int[] current = minHeap.poll();
            int currentCost = current[0], city = current[1], stopsLeft = current[2];

            if (city == destination) return currentCost; // If we reach the destination, return the cost

            if (stopsLeft > 0) { // Process only if we have stops left
                for (int[] neighbor : graph.getOrDefault(city, Collections.emptyList())) {
                    int nextCity = neighbor[0], flightCost = neighbor[1];
                    int newCost = currentCost + flightCost;
                    int nextStopsLeft = stopsLeft - 1;

                    // Only proceed if this exact city/stops-left state is cheaper
                    if (newCost < bestCost[nextCity][nextStopsLeft]) {
                        bestCost[nextCity][nextStopsLeft] = newCost;
                        minHeap.offer(new int[]{newCost, nextCity, nextStopsLeft});
                    }
                }
            }
        }
        return -1; // No valid route found
    }

    /**
     * Finds the cheapest price to travel from a source city to a destination city
     * with at most K stops using a Bellman-Ford style approach.
     *
     * 💡 Intuition:
     * - Each flight is an edge. We perform at most (K+1) "levels" of edge relaxation,
     *   where each level represents one extra stop.
     * - In each level, we try to improve the cost of reaching each city using the
     *   flights available, *without prematurely using updated values in the same level.*
     *   (hence we use a temp array for the update).
     *
     * 🔍 Why it works:
     * - We’re relaxing all edges up to K+1 times, just like Bellman-Ford.
     * - But we do it in a controlled way using copies of the distance array,
     *   which simulates layer-wise propagation (like BFS).
     *
     * ✅ Time Complexity: O(K * E), where:
     *      - K is the max number of stops
     *      - E is the number of flights (edges)
     * ✅ Space Complexity: O(V), where V is the number of cities (vertices)
     */
    public int findCheapestPriceBellmanFord(int totalCities, int[][] flights, int source, int destination, int maxStops) {
        // minCostFromSource[i] will hold the minimum cost to reach city i from source
        // Initialize all costs to infinity, except the source city
        int[] minCostFromSource = new int[totalCities];
        Arrays.fill(minCostFromSource, Integer.MAX_VALUE);
        minCostFromSource[source] = 0;

        // Perform up to (maxStops + 1) rounds of edge relaxation. Edge relaxation is the
        // process of updating the cost to reach a city if a cheaper route is found.
        for (int stops = 0; stops <= maxStops; stops++) {
            // Create a copy of the current cost array to prevent premature updates
            int[] copiedMinCostFromSource = Arrays.copyOf(minCostFromSource, totalCities);

            for (int[] flight : flights) {
                int from = flight[0];
                int to = flight[1];
                int price = flight[2];

                // Only relax if source city is reachable
                if (minCostFromSource[from] != Integer.MAX_VALUE) {
                    copiedMinCostFromSource[to] = Math.min(copiedMinCostFromSource[to], minCostFromSource[from] + price);
                }
            }

            // Move to the next level of relaxation
            minCostFromSource = copiedMinCostFromSource;
        }

        return minCostFromSource[destination] == Integer.MAX_VALUE ? -1 : minCostFromSource[destination];
    }
}
