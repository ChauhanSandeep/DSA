package graphs;

import java.util.*;

/**
 * On a 2D plane, we place n stones at some integer coordinate points. Each coordinate point may have at most one stone.
 * A stone can be removed if it shares either the same row or the same column as another stone that has not been removed.
 *
 * Given an array stones of length n where stones[i] = [xi, yi] represents the location of the ith stone,
 * return the largest possible number of stones that can be removed.
 *
 * Example 1:
 * Input: stones = [[0,0],[0,1],[1,0],[1,2],[2,1],[2,2]]
 * Output: 5
 * Explanation: One way to remove 5 stones is as follows:
 * 1. Remove stone [2,2] because it shares the same row as [2,1].
 * 2. Remove stone [2,1] because it shares the same column as [0,1].
 * 3. Remove stone [1,2] because it shares the same column as [1,0].
 * 4. Remove stone [1,0] because it shares the same row as [0,0].
 * 5. Remove stone [0,1] because it shares the same row as [0,0].
 * Stone [0,0] cannot be removed since it does not share a row/column with another stone.
 *
 * Example 2:
 * Input: stones = [[0,0],[0,2],[1,1],[2,0],[2,2]]
 * Output: 3
 *
 * LeetCode: https://leetcode.com/problems/most-stones-removed-with-same-row-or-column/
 *
 * Follow-up Questions:
 * 1. How would you modify the solution if stones could share both row and column with any number of other stones?
 *    - The current solution already handles this case as it's part of the problem statement.
 * 2. What if the grid is very large (e.g., 10^9 x 10^9)?
 *    - The union-find solution is efficient as it only considers the actual stone positions.
 * 3. How would you find which stones to remove to achieve the maximum removals?
 *    - We could modify the solution to track the removal order or the connected components.
 *
 * Related Problems:
 * - Number of Islands (https://leetcode.com/problems/number-of-islands/)
 * - Number of Provinces (https://leetcode.com/problems/number-of-provinces/)
 * LeetCode Contest Rating: 2035
 **/
public class MostStonesRemovedWithSameRowOrColumn {
    /**
     * Calculates the maximum number of stones that can be removed.
     *
     * @param stones Array of stone positions [x, y]
     * @return Maximum number of stones that can be removed
     */
    public int removeStones(int[][] stones) {
        // Use a map to represent the union-find data structure
        // Key: stone position as "x,y" string
        // Value: parent of the stone
        Map<String, String> parent = new HashMap<>();
        int count = 0;

        // Initialize each stone as its own parent
        for (int[] stone : stones) {
            String s = stone[0] + "," + stone[1];
            parent.put(s, s);
        }

        // Union stones that share the same row or column
        for (int[] stone1 : stones) {
            String s1 = stone1[0] + "," + stone1[1];

            for (int[] stone2 : stones) {
                // If stones share the same row or column
                if (stone1[0] == stone2[0] || stone1[1] == stone2[1]) {
                    String s2 = stone2[0] + "," + stone2[1];

                    // Union the two stones
                    String root1 = find(parent, s1);
                    String root2 = find(parent, s2);

                    if (!root1.equals(root2)) {
                        parent.put(root1, root2);
                        count++;
                    }
                }
            }
        }

        return count;
    }

    /**
     * Find operation for union-find with path compression.
     */
    private String find(Map<String, String> parent, String s) {
        if (!parent.get(s).equals(s)) {
            parent.put(s, find(parent, parent.get(s)));
        }
        return parent.get(s);
    }

    /**
     * Optimized solution using union-find with row and column mapping.
     * This is more efficient as it avoids string operations.
     */
    public int removeStonesOptimized(int[][] stones) {
        // For union find, we can represent each stone as an index
        // and use a parent array for the union-find operations

        int n = stones.length;
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i; // Each stone is its own parent initially
        }

        // For each stone, check all other stones to see if they share a row or column
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // If stones share the same row or column
                if (stones[i][0] == stones[j][0] || stones[i][1] == stones[j][1]) {
                    // Union the two stones
                    int rootI = find(parent, i);
                    int rootJ = find(parent, j);

                    if (rootI != rootJ) {
                        parent[rootJ] = rootI; // Union by setting parent
                    }
                }
            }
        }

        // Count the number of connected components
        int components = 0;
        for (int i = 0; i < n; i++) {
            if (parent[i] == i) {
                components++;
            }
        }

        // The maximum number of stones that can be removed is total stones - number of components
        return n - components;
    }

    /**
     * Find with path compression for the optimized solution.
     */
    private int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]); // Path compression
        }
        return parent[x];
    }
}
