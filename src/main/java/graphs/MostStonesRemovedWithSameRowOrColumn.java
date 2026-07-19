package graphs;

import java.util.*;

/**
 * Problem: Most Stones Removed with Same Row or Column
 *
 * Stones sit on integer grid coordinates. A stone may be removed if another
 * remaining stone shares its row or column. Return the maximum number of stones
 * that can be removed.
 *
 * Leetcode: https://leetcode.com/problems/most-stones-removed-with-same-row-or-column/ (Medium)
 * Rating:   2035 (zerotrac Elo)
 * Pattern:  Graph | Union-find | Connected components
 *
 * Example:
 *   Input:  stones = [[0,0],[0,1],[1,0],[1,2],[2,1],[2,2]]
 *   Output: 5
 *   Why:    all six stones are connected through shared rows or columns, so one
 *           stone must remain and the other five can be removed.
 *
 * Follow-ups:
 *   1. Return a valid removal order?
 *      For each component, keep one representative and remove other stones from leaves inward.
 *   2. Coordinates are up to 10^9?
 *      Use hash maps for row and column ids instead of dense arrays.
 *   3. Add stones dynamically and query removals?
 *      Maintain DSU components and update removable count after each union.
 *
 * Related: Number of Provinces (547), Number of Islands II (305).
 */
public class MostStonesRemovedWithSameRowOrColumn {

    public static void main(String[] args) {
        MostStonesRemovedWithSameRowOrColumn solver = new MostStonesRemovedWithSameRowOrColumn();
        int[][][] inputs = {
            {{0, 0}, {0, 1}, {1, 0}, {1, 2}, {2, 1}, {2, 2}},
            {{0, 0}}
        };
        int[] expected = {5, 0};
        for (int i = 0; i < inputs.length; i++) {
            int output = solver.removeStones(inputs[i]);
            System.out.printf("stones=%s  ->  %d  expected=%d%n", Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }
    /**
     * Intuition: stones connected by sharing a row or column form a component. In
     * each component, all but one stone can be removed because there is always some
     * remaining stone sharing its row or column until one representative is left.
     * Union-find groups stones by shared rows and columns.
     *
     * Algorithm:
     *   1. Give each stone its own union-find parent.
     *   2. Union pairs of stones that share a row or a column.
     *   3. Count the number of connected components after all unions.
     *   4. Return stones.length - components.
     *
     * Time:  O(n^2 * alpha(n)) - every stone pair is checked and union-find is nearly constant time.
     * Space: O(n) - parent and rank arrays store one entry per stone.
     *
     * @param stones stone coordinates [row, col]
     * @return maximum removable stones
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
