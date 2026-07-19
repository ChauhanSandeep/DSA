package arrays.sorting;

import java.util.*;
/**
 * Problem: Make Lexicographically Smallest Array by Swapping Elements
 *
 * Given nums and limit, you may swap two positions whenever their values differ
 * by at most limit. Return the lexicographically smallest array reachable after
 * any number of swaps.
 *
 * Leetcode: https://leetcode.com/problems/make-lexicographically-smallest-array-by-swapping-elements/ (Medium)
 * Rating:   2046 (Weekly Contest 373)
 * Pattern:  Sorting | Connected components | Greedy placement
 *
 * Example:
 *   Input:  nums = [1,5,3,9,8], limit = 2
 *   Output: [1,3,5,8,9]
 *   Why:    values within each sorted-by-value component can be assigned to that component's sorted indices.
 *
 * Follow-ups:
 *   1. limit is zero?
 *      Only equal values can swap, so the visible array does not change.
 *   2. Support updates to nums?
 *      Maintain value-ordered components with a balanced tree and rebuild affected groups.
 *   3. Need smallest array after at most one swap?
 *      Greedily test the earliest improvable index under the direct swap rule.
 *
 * Related: Smallest String With Swaps (1202), Accounts Merge (721).
 */
public class MakeLexicographicallySmallestArrayBySwappingElements {

    public static void main(String[] args) {
        MakeLexicographicallySmallestArrayBySwappingElements solver =
            new MakeLexicographicallySmallestArrayBySwappingElements();

        int[][] inputs = { {1, 5, 3, 9, 8}, {1, 7, 6, 18, 2, 1}, {5} };
        int[] limits = { 2, 3, 10 };
        int[][] expected = { {1, 3, 5, 8, 9}, {1, 6, 7, 18, 1, 2}, {5} };

        for (int i = 0; i < inputs.length; i++) {
            int[] got = solver.lexicographicallySmallestArray(inputs[i], limits[i]);
            System.out.printf("nums=%s limit=%d -> output=%s  expected=%s%n",
                Arrays.toString(inputs[i]), limits[i], Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }

    /**
     * Disjoint Set Union approach.
     *
     * Intuition: Swap-eligibility is transitive, so indices form equivalence classes.
     * Naively there could be O(n^2) eligible (i, j) pairs, but after sorting values
     * we only need to union *consecutive* indices whose values differ by <= limit:
     * transitivity stitches the rest of the class together automatically. Once each
     * component is known, sort its indices and its values and align them.
     *
     * Algorithm:
     * 1. Build an index array sorted by nums[i] ascending.
     * 2. For each consecutive (prev, curr) in that order, union them if the value
     *    gap is <= limit.
     * 3. Bucket original indices by DSU root.
     * 4. For each bucket: sort indices ascending, sort values ascending, then
     *    place value[k] at index[k] in the result.
     *
     * Time Complexity:  O(n log n) -- sorting dominates; DSU ops are near O(1).
     * Space Complexity: O(n) for parent/rank arrays, sorted indices, and buckets.
     *
     * @param nums  input array of positive integers
     * @param limit maximum allowed absolute difference for a single swap
     * @return lexicographically smallest array achievable
     */
    public int[] lexicographicallySmallestArrayDSU(int[] nums, int limit) {
        int len = nums.length;

        // --- Step 1: sort indices by their value ------------------------------
        Integer[] indicesByValue = new Integer[len];
        for (int i = 0; i < len; i++) {
            indicesByValue[i] = i;
        }
        Arrays.sort(indicesByValue, Comparator.comparingInt(i -> nums[i]));

        // --- Step 2: union adjacent (by sorted value) indices within limit ---
        DisjointSetUnion dsu = new DisjointSetUnion(len);
        for (int k = 1; k < len; k++) {
            int prevIdx = indicesByValue[k - 1];
            int currIdx = indicesByValue[k];
            if (nums[currIdx] - nums[prevIdx] <= limit) {
                dsu.union(prevIdx, currIdx);
            }
        }

        // --- Step 3: bucket original indices by component root ---------------
        Map<Integer, List<Integer>> componentIndices = new HashMap<>();
        for (int i = 0; i < len; i++) {
            componentIndices.computeIfAbsent(dsu.find(i), r -> new ArrayList<>()).add(i);
        }

        // --- Step 4: sorted values to sorted indices, per component ----------
        int[] result = new int[len];
        for (List<Integer> indices : componentIndices.values()) {
            int size = indices.size();
            int[] values = new int[size];
            for (int k = 0; k < size; k++) {
                values[k] = nums[indices.get(k)];
            }
            Collections.sort(indices);
            Arrays.sort(values);
            for (int k = 0; k < size; k++) {
                result[indices.get(k)] = values[k];
            }
        }

        return result;
    }

/**
 * Intuition: after sorting by value, a run whose adjacent gaps are all <= limit
 * is one connected swap component. To minimize lexicographic order, put the
 * smallest values of that component into its smallest original indices.
 *
 * Algorithm:
 *   1. Pair every value with its original index and sort pairs by value.
 *   2. Split the sorted pairs whenever the adjacent value gap exceeds limit.
 *   3. For each group, sort its original indices.
 *   4. Assign the already sorted group values to those sorted indices.
 *
 * Time:  O(n log n) - sorting values and group indices dominates.
 * Space: O(n) - value-index pairs, group indices, and result are stored.
 *
 * @param nums input array
 * @param limit maximum value difference allowed for one swap
 * @return lexicographically smallest reachable array
 */
    public int[] lexicographicallySmallestArray(int[] nums, int limit) {
        int len = nums.length;

        // Pair (value, originalIndex) and sort by value
        int[][] valueIndex = new int[len][2];
        for (int i = 0; i < len; i++) {
            valueIndex[i][0] = nums[i];
            valueIndex[i][1] = i;
        }
        Arrays.sort(valueIndex, Comparator.comparingInt(a -> a[0]));

        int[] result = new int[len];

        int start = 0;
        while (start < len) {
            // Extend group while consecutive sorted values are within limit
            int end = start + 1;
            while (end < len && valueIndex[end][0] - valueIndex[end - 1][0] <= limit) {
                end++;
            }

            // Collect indices of this group and sort them ascending
            int size = end - start;
            Integer[] sortedIndex = new Integer[size];

            for (int i = 0; i < size; i++) {
                sortedIndex[i] = valueIndex[start + i][1];
            }
            Arrays.sort(sortedIndex);

            // Sorted values go to sorted indices to minimize lexicographic order
            for (int k = 0; k < size; k++) {
                result[sortedIndex[k]] = valueIndex[start + k][0];
            }

            start = end;
        }

        return result;
    }

    /**
     * Disjoint Set Union with path compression (halving) and union by rank.
     * Both operations run in amortized O(α(n)) time.
     */
    private static class DisjointSetUnion {
        private final int[] parent;
        private final int[] rank;

        DisjointSetUnion(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        int find(int x) {
            while (parent[x] != x) {
                parent[x] = parent[parent[x]]; // path compression by halving
                x = parent[x];
            }
            return x;
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;
            if (rank[ra] < rank[rb]) {
                parent[ra] = rb;
            } else if (rank[ra] > rank[rb]) {
                parent[rb] = ra;
            } else {
                parent[rb] = ra;
                rank[ra]++;
            }
        }
    }
}

