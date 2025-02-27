import java.util.*;

public class CheapestFlightWithStopsOptimized {
    
    public static void main(String[] args) {
        int[][] flights = {
            {0, 1, 100},
            {1, 2, 100},
            {0, 2, 500}
        };

        CheapestFlightWithStopsOptimized solver = new CheapestFlightWithStopsOptimized();
        int cheapestCost = solver.findCheapestPrice(3, flights, 0, 2, 1);
        System.out.println("Cheapest cost: " + cheapestCost);
    }

    public int findCheapestPrice(int totalCities, int[][] flights, int source, int destination, int maxStops) {
        // Step 1: Build the graph -> adjacency list {source -> [destination, price]}
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] flight : flights) {
            graph.computeIfAbsent(flight[0], k -> new ArrayList<>()).add(new int[]{flight[1], flight[2]});
        }

        // Step 2: Min-Heap (PriorityQueue) -> {costSoFar, currentCity, stopsRemaining}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, source, maxStops + 1}); // Cost, city, stops

        // Step 3: Map to track min stops required for each city
        Map<Integer, Integer> minStops = new HashMap<>();
        minStops.put(source, maxStops + 1);

        // Step 4: Process queue
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int cost = current[0], city = current[1], stops = current[2];

            if (city == destination) return cost; // Early exit if we reach the destination

            if (stops > 0) { // Only process if we still have stops left
                for (int[] neighbor : graph.getOrDefault(city, new ArrayList<>())) {
                    int nextCity = neighbor[0], price = neighbor[1];

                    if (!minStops.containsKey(nextCity) || stops - 1 > minStops.get(nextCity)) {
                        minStops.put(nextCity, stops - 1);
                        pq.offer(new int[]{cost + price, nextCity, stops - 1});
                    }
                }
            }
        }
        
        return -1; // No valid route found
    }
}
