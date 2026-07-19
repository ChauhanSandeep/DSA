package graphs.dijkstra;

import java.util.*;


/**
 * Problem: Network Delay Time
 *
 * A signal starts at node k in a directed weighted network. Each edge [u, v, w]
 * means the signal takes w time to travel from u to v. Return the earliest time
 * when every node has received the signal, or -1 if some node is unreachable.
 *
 * Leetcode: https://leetcode.com/problems/network-delay-time/ (Medium)
 * Rating:   acceptance 61.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | Dijkstra | Single-source shortest paths
 *
 * Example:
 *   Input:  times = [[2,1,1],[2,3,1],[3,4,1]], n = 4, k = 2
 *   Output: 2
 *   Why:    nodes 1 and 3 receive the signal at time 1, and node 4 receives it at time 2.
 *
 * Follow-ups:
 *   1. Edges can have negative delays?
 *      Use Bellman-Ford and reject negative cycles because Dijkstra needs non-negative weights.
 *   2. Need the receiving path for every node?
 *      Track parent pointers whenever a node's best delay improves.
 *   3. Need many source queries on a dense graph?
 *      Precompute all-pairs shortest paths with Floyd-Warshall.
 *
 * Related: Cheapest Flights Within K Stops (787), Path With Minimum Effort (1631).
 */
public class NetworkDelayTimeSolver {

  public static void main(String[] args) {
    NetworkDelayTimeSolver solver = new NetworkDelayTimeSolver();
    int[][] times1 = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
    int[][] times2 = {{1, 2, 1}};

    System.out.printf("times=%s n=4 k=2 -> %d  expected=2%n",
        Arrays.deepToString(times1), solver.networkDelayTime(times1, 4, 2));
    System.out.printf("times=%s n=3 k=1 -> %d  expected=-1%n",
        Arrays.deepToString(times2), solver.networkDelayTime(times2, 3, 1));
  }

    /**
   * Intuition: the time a node receives the signal is its shortest-path distance
   * from k. Dijkstra finalizes the smallest unfinalized arrival time first; after
   * all reachable nodes are finalized, the network delay is the slowest arrival.
   *
   * Algorithm:
   *   1. Build the directed adjacency list from times.
   *   2. Push [k, 0] into a min-heap ordered by current time.
   *   3. Pop states, skipping nodes already finalized in shortestTimeMap.
   *   4. Push unfinalized neighbors with accumulated time, then return the maximum finalized time.
   *
   * Time:  O(E log E) - each edge may create a heap entry.
   * Space: O(V + E) - graph, heap, and finalized-time map.
   *
   * @param times directed edges [source, target, delay]
   * @param n total nodes labeled 1 through n
   * @param k starting node
   * @return earliest time all nodes receive the signal, or -1 if any node is unreachable
   */
  public int networkDelayTime(int[][] times, int n, int k) {
    // Step 1: Build the graph as an adjacency list
    Map<Integer, List<int[]>> graph = new HashMap<>();
    for (int[] edge : times) {
      int source = edge[0];
      int destination = edge[1];
      int weight = edge[2];
      graph.computeIfAbsent(source, x -> new ArrayList<>()).add(new int[]{destination, weight});
    }

    // Step 2: Min-heap to get the node with the smallest time delay
    PriorityQueue<int[]> minHeap = new PriorityQueue<>((x, y) -> x[1] - y[1]);
    minHeap.offer(new int[]{k, 0}); // {node, time to reach}

    // Step 3: Track shortest times to reach each node
    Map<Integer, Integer> shortestTimeMap = new HashMap<>();

    while (!minHeap.isEmpty()) {
      int[] current = minHeap.poll();
      int currentNode = current[0];
      int currentTime = current[1];

      // Skip if this currentNode is already finalized (visited)
      if (shortestTimeMap.containsKey(currentNode)) {
        continue;
      }

      // Mark currentNode as visited with shortest time
      shortestTimeMap.put(currentNode, currentTime);

      // Explore neighbors
      if (graph.containsKey(currentNode)) {
        for (int[] neighbor : graph.get(currentNode)) {
          int nextNode = neighbor[0];
          int timeToNext = neighbor[1];

          if (!shortestTimeMap.containsKey(nextNode)) {
            minHeap.offer(new int[]{nextNode, currentTime + timeToNext});
          }
        }
      }
    }

    // Step 4: Check if all nodes were reached and return the max delay
    if (shortestTimeMap.size() != n) {
      return -1;
    }

    int maxDelay = 0;
    for (int time : shortestTimeMap.values()) {
      maxDelay = Math.max(maxDelay, time);
    }

    return maxDelay;
  }
}