package dynamicprogramming.MiscellaneousDP;

import java.util.*;

/**
 * You are given an array pairs, where pairs[i] = [xi, yi], and:
 * - There are no duplicates, and xi < yi
 * - The pairs are unordered, meaning [x, y] and [y, x] are considered the same
 *
 * A tree is a valid tree if it follows these rules:
 * 1. The tree has exactly one root node
 * 2. Each node has exactly one parent except the root node which has no parent
 * 3. The given pairs must appear as edges in the tree
 *
 * Return the number of different valid trees you can form with the given pairs.
 * Since the answer may be large, return it modulo 10^9 + 7.
 *
 * Example 1:
 * Input: pairs = [[1,2],[2,3]]
 * Output: 1
 * Explanation: The tree [1,2,3] is the only valid tree.
 *
 * Example 2:
 * Input: pairs = [[1,2],[2,3],[1,3]]
 * Output: 2
 * Explanation: The trees [1,2,3] and [1,3,2] are both valid.
 *
 * LeetCode: https://leetcode.com/problems/number-of-ways-to-reconstruct-a-tree/
 *
 * Follow-up Questions:
 * 1. How would you modify the solution if the tree was required to be a binary tree?
 *    - We would need to ensure no node has more than 2 children.
 * 2. What if we needed to return all possible valid trees instead of just counting them?
 *    - We would need to modify the solution to track and construct all valid trees.
 * 3. How would you handle very large inputs (e.g., 1000+ nodes)?
 *    - The current solution is O(n^2) which might be too slow for very large inputs.
 *
 * Related Problems:
 * - Reconstruct Itinerary (https://leetcode.com/problems/reconstruct-itinerary/)
 * - Minimum Height Trees (https://leetcode.com/problems/minimum-height-trees/)
 * LeetCode Contest Rating: 3018
 **/
public class NumberOfWaysToReconstructATree {
    private static final int MOD = 1000000007;

    /**
     * Counts the number of ways to reconstruct a valid tree from the given pairs.
     *
     * @param pairs Array of pairs representing edges
     * @return Number of valid trees modulo 10^9 + 7
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
        nodes.sort((a, b) -> degree.get(b) - degree.get(a));

        // Check if there's a root (node with degree n-1)
        int n = nodes.size();
        if (degree.get(nodes.get(0)) != n - 1) {
            return 0;
        }

        int res = 1;
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
                res = (res * 2) % MOD;
            }
        }

        return res;
    }
}
