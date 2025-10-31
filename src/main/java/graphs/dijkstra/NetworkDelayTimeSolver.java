package graphs.dijkstra;

import java.util.*;


/**
 * There are n nodes (labelled 1 to n) in a directed weighted graph. You are given a list of times,
 * where times[i] = [u, v, w] represents that the node u can transmit a signal to node v with a delay of w seconds.
 *
 * Given that a signal is sent from a starting node k, return the minimum time it takes for all the nodes
 * to receive the signal. If it is impossible for all the nodes to receive the signal, return -1.
 *
 * Example:
 * Input: times = [[2,1,1],[2,3,1],[3,4,1]], n = 4, k = 2
 * Output: 2
 *
 * LeetCode Link: https://leetcode.com/problems/network-delay-time/
 */
public class NetworkDelayTimeSolver {

  /**
   * Intuition:
   *  - Treat the input as a directed, weighted graph.
   *  - Use Dijkstra’s algorithm to find the shortest path from the source node `k` to all other nodes.
   *  - The answer is the maximum distance among all nodes.
   *
   * Steps:
   * 1. Build an adjacency list to represent the graph.
   * 2. Use a min-heap (priority queue) to always process the node with the current shortest known distance.
   * 3. Keep updating the shortest distance to each node using relaxation.
   * 4. At the end, if any node is unreachable, return -1. Otherwise, return the maximum of all shortest distances.
   *
   * Time Complexity:
   *  - O(E log V), where E = number of edges, V = number of nodes.
   *
   * Space Complexity:
   *  - O(E + V) for the graph and distance tracking.
   *
   * @param times 2D array representing edges [u, v, w]
   * @param n     Total number of nodes
   * @param k     Starting node
   * @return Minimum time to reach all nodes, or -1 if unreachable
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