package dynamicprogramming.MiscellaneousDP;

import java.util.*;

/**
 * Problem: Restore the Array From Adjacent Pairs
 *
 * An unknown array has unique values, and you are given all of its adjacent pairs
 * in arbitrary order and direction. Reconstruct one valid original array. The
 * reverse of the original is also acceptable.
 *
 * Leetcode: https://leetcode.com/problems/restore-the-array-from-adjacent-pairs/
 * Rating:   1579 (zerotrac Elo)
 * Pattern:  Graph | Path reconstruction | Degree-one endpoint
 *
 * Example:
 *   Input:  adjacentPairs = [[2,1],[3,4],[3,2]]
 *   Output: [1,2,3,4]
 *   Why:    1 and 4 are endpoints, and following unused neighbours from 1 gives
 *           1 next to 2, 2 next to 3, and 3 next to 4.
 *
 * Follow-ups:
 *   1. What if values can repeat?
 *      The graph no longer identifies positions uniquely; include occurrence ids or extra constraints.
 *   2. What if the pairs describe several disconnected arrays?
 *      Start from every degree-one node in each component and reconstruct each path separately.
 *   3. Can we detect invalid input?
 *      Verify exactly two endpoints, all degrees are at most two, and the walk visits every node once.
 *
 * Related: Reconstruct Itinerary (332), Find Center of Star Graph (1791).
 */
public class RestoreTheArrayFromAdjacentPairs {
/**
     * Intuition: the adjacent pairs form a simple path graph. Interior values have
     * two neighbours, while the two ends have only one. Once we start from either
     * endpoint, each next value is the neighbour that is not the value we just came
     * from. Walking that path reconstructs the whole array because every number is
     * unique and the original structure has no branches.
     *
     * Algorithm:
     *   1. Build an undirected adjacency list and occurrence count from adjacentPairs.
     *   2. Pick any value with count 1 as the starting endpoint.
     *   3. Walk the path by taking the unvisited neighbor of the previous result value.
     *   4. Stop after adjacentPairs.length + 1 values are written.
     *
     * Time:  O(n) - each pair and each array value is processed a constant number of times.
     * Space: O(n) - the graph and result store all n values.
     *
     * @param adjacentPairs unordered adjacent pairs from the hidden array
     * @return one valid restored array
     */
    public int[] restoreArray(int[][] adjacentPairs) {
        // Build adjacency list
        Map<Integer, List<Integer>> graph = new HashMap<>();

        // Count occurrences to find the ends of the array (which appear once)
        Map<Integer, Integer> count = new HashMap<>();

        // Build the graph and count occurrences
        for (int[] pair : adjacentPairs) {
            int u = pair[0], v = pair[1];

            // Build adjacency list
            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, k -> new ArrayList<>()).add(u);

            // Count occurrences
            count.put(u, count.getOrDefault(u, 0) + 1);
            count.put(v, count.getOrDefault(v, 0) + 1);
        }

        // Find the start node (a node with only one connection)
        int start = 0;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            if (entry.getValue() == 1) {
                start = entry.getKey();
                break;
            }
        }

        // Reconstruct the array
        int n = adjacentPairs.length + 1;
        int[] result = new int[n];
        result[0] = start;

        // Use a set to keep track of visited nodes
        Set<Integer> visited = new HashSet<>();
        visited.add(start);

        // Reconstruct the array
        for (int i = 1; i < n; i++) {
            int current = result[i - 1];
            for (int neighbor : graph.get(current)) {
                if (!visited.contains(neighbor)) {
                    result[i] = neighbor;
                    visited.add(neighbor);
                    break;
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        RestoreTheArrayFromAdjacentPairs solver = new RestoreTheArrayFromAdjacentPairs();
        int[][][] inputs = {
            {{2, 1}, {3, 4}, {3, 2}},
            {{4, -2}, {1, 4}, {-3, 1}}
        };
        String[] expected = {"[1, 2, 3, 4] or reverse", "[-2, 4, 1, -3] or reverse"};

        for (int i = 0; i < inputs.length; i++) {
            int[] got = solver.restoreArray(inputs[i]);
            System.out.printf("adjacentPairs=%s -> %s  expected=%s%n",
                Arrays.deepToString(inputs[i]), Arrays.toString(got), expected[i]);
        }
    }
}
