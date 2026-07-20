package dynamicprogramming.MiscellaneousDP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Problem: Number Of Ways To Reconstruct A Tree
 *
 * Given ancestor-descendant pairs from a rooted tree, decide whether no tree, one
 * tree, or multiple trees can explain those relationships. Return 0, 1, or 2.
 *
 * Leetcode: https://leetcode.com/problems/number-of-ways-to-reconstruct-a-tree/
 * Rating:   3018 (zerotrac Elo)
 * Pattern:  Graph | Tree reconstruction | Degree ordering
 *
 * Example:
 *   Input:  pairs = [[1,2],[2,3],[1,3]]
 *   Output: 2
 *   Why:    node 1 or node 3 can be placed above the other while preserving all
 *           ancestor relationships, so the reconstruction is not unique.
 *
 * Follow-ups:
 *   1. Return the actual tree when it is unique?
 *      Store the chosen parent for each node while validating containment.
 *   2. What if the input pairs are directed?
 *      Use indegree and ancestor-set checks instead of symmetric adjacency.
 *   3. What if all valid trees must be enumerated?
 *      Backtrack over same-degree interchangeable parent choices; output may be exponential.
 *
 * Related: Validate Binary Tree Nodes (1361), Minimum Height Trees (310).
 */
public class NumberOfWaysToReconstructATree {

    /**
     * Intuition: a root must be connected to every other node because every node is
     * either its ancestor or descendant. For any other node, its parent must be a
     * neighbour with at least as many relationships, and all of the node's other
     * neighbours must also be neighbours of that parent. Equal-degree parent and
     * child choices mean at least two valid trees exist, so the answer is the
     * multiple-reconstruction sentinel 2.
     *
     * Algorithm:
     *   1. Build adjacency sets and degrees from every pair.
     *   2. Sort nodes by descending degree and require the first node to connect to all others.
     *   3. For each remaining node, choose the earlier adjacent node with minimum degree as parent.
     *   4. Verify every other neighbor is also a parent neighbor, and return 2 if any equal-degree edge appears.
     *
     * Time:  O(n^2) - each node may compare its neighbours against a candidate parent set.
     * Space: O(n^2) - dense pair input can store a neighbour set for every node pair.
     *
     * @param pairs unordered ancestor-descendant pairs
     * @return 0 if impossible, 1 if unique, or 2 if multiple trees are possible
     */
    public int checkWays(int[][] pairs) {
        // Build adjacency list and degree count
        Map<Integer, Set<Integer>> adj = new HashMap<>();
        Map<Integer, Integer> degree = new HashMap<>();

        for (int[] pair : pairs) {
            int u = pair[0], v = pair[1];
            adj.computeIfAbsent(u, k -> new HashSet<>()).add(v);
            adj.computeIfAbsent(v, k -> new HashSet<>()).add(u);
            degree.put(u, degree.getOrDefault(u, 0) + 1);
            degree.put(v, degree.getOrDefault(v, 0) + 1);
        }

        // Sort nodes by degree in descending order
        List<Integer> nodes = new ArrayList<>(degree.keySet());
        nodes.sort((a, b) -> Integer.compare(degree.get(b), degree.get(a)));

        // Check if there's a root (node with degree n-1)
        int n = nodes.size();
        if (degree.get(nodes.get(0)) != n - 1) {
            return 0;
        }

        int result = 1;
        // For each node, check if it can be a child of any previous node
        for (int i = 1; i < n; i++) {
            int u = nodes.get(i);
            int parent = -1;
            int minDegree = Integer.MAX_VALUE;

            // Find parent with minimum degree that contains all neighbors of u
            for (int j = 0; j < i; j++) {
                int v = nodes.get(j);
                if (adj.get(u).contains(v)) {
                    if (degree.get(v) < minDegree) {
                        parent = v;
                        minDegree = degree.get(v);
                    }
                }
            }

            // Check if parent contains all neighbors of u
            if (parent == -1) {
                return 0;
            }

            for (int v : adj.get(u)) {
                if (v != parent && !adj.get(parent).contains(v)) {
                    return 0;
                }
            }

            // If parent has the same degree as u, we can swap them
            if (degree.get(parent) == degree.get(u)) {
                result = 2;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        NumberOfWaysToReconstructATree solver = new NumberOfWaysToReconstructATree();
        int[][][] inputs = {
            {{1, 2}, {2, 3}},
            {{1, 2}, {2, 3}, {2, 4}, {1, 5}},
            {{1, 2}, {1, 3}, {1, 4}, {2, 3}, {2, 4}, {3, 4}}
        };
        int[] expected = {1, 0, 2};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.checkWays(inputs[i]);
            System.out.printf("pairs=%s -> %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }
}
