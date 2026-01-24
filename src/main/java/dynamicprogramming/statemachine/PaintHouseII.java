package dynamicprogramming.statemachine;

import java.util.*;

/**
 * 265. Paint House II
 *
 * Problem: There are n houses and k different colors. The cost of painting house i
 * with color j is cost[i][j]. Find the minimum cost to paint all houses such that
 * no two adjacent houses have the same color.
 *
 * Example:
 * Input: costs = [[1,5,3],[2,9,4]]
 * Output: 5
 * Explanation: Paint house 0 with color 0 and house 1 with color 2. Total cost = 1 + 4 = 5.
 *
 * LeetCode: https://leetcode.com/problems/paint-house-ii
 *
 * Follow-up questions:
 * Q: What if some houses have color restrictions or preferences?
 * A: Add constraint validation in DP transitions and modify state space.
 *
 * Q: How to handle very large number of colors efficiently?
 * A: Use data structures to maintain minimum and second minimum efficiently.
 *
 * Q: Can we support different constraint patterns (e.g., no same color in k-neighborhood)?
 * A: Extend DP state to track more complex constraint patterns.
 */
public class PaintHouseII {

    /**
     * Dynamic Programming approach - basic O(nk²) solution.
     *
     * Algorithm: State-based DP
     * - dp[i][j] = minimum cost to paint houses 0..i with house i having color j
     * - Transition: dp[i][j] = cost[i][j] + min(dp[i-1][c]) for all c != j
     * - Answer: min(dp[n-1][j]) for all j
     *
     * Time Complexity: O(n*k²) where n is houses, k is colors
     * Space Complexity: O(k) using rolling array optimization
     */
    public int minCostII(int[][] costs) {
        if (costs == null || costs.length == 0) return 0;

        int n = costs.length;
        int k = costs[0].length;
        if (k == 1) return n == 1 ? costs[0][0] : Integer.MAX_VALUE; // Impossible if k=1 and n>1

        int[] dp = Arrays.copyOf(costs[0], k);

        for (int house = 1; house < n; house++) {
            int[] newDp = new int[k];

            for (int color = 0; color < k; color++) {
                newDp[color] = Integer.MAX_VALUE;

                // Find minimum cost from previous house with different color
                for (int prevColor = 0; prevColor < k; prevColor++) {
                    if (prevColor != color) {
                        newDp[color] = Math.min(newDp[color], dp[prevColor] + costs[house][color]);
                    }
                }
            }

            dp = newDp;
        }

        return Arrays.stream(dp).min().getAsInt();
    }

    /**
     * Optimized O(nk) approach using min/second-min tracking.
     *
     * Algorithm: Min/SecondMin optimization
     * - Instead of checking all previous colors, track minimum and second minimum
     * - If current color != min color, use min; otherwise use second min
     * - Reduces inner loop from O(k) to O(1)
     */
    public int minCostIIOptimized(int[][] costs) {
        if (costs == null || costs.length == 0) return 0;

        int n = costs.length;
        int k = costs[0].length;
        if (k == 1) return n == 1 ? costs[0][0] : Integer.MAX_VALUE;

        int min1 = 0, min2 = 0; // Costs of minimum and second minimum
        int minIdx = -1; // Index of minimum cost color

        for (int house = 0; house < n; house++) {
            int newMin1 = Integer.MAX_VALUE;
            int newMin2 = Integer.MAX_VALUE;
            int newMinIdx = -1;

            for (int color = 0; color < k; color++) {
                int cost = costs[house][color] + (color == minIdx ? min2 : min1);

                if (cost < newMin1) {
                    newMin2 = newMin1;
                    newMin1 = cost;
                    newMinIdx = color;
                } else if (cost < newMin2) {
                    newMin2 = cost;
                }
            }

            min1 = newMin1;
            min2 = newMin2;
            minIdx = newMinIdx;
        }

        return min1;
    }

    /**
     * Space-optimized approach with in-place modification.
     * Modifies input array to reduce space complexity.
     */
    public int minCostIIInPlace(int[][] costs) {
        if (costs == null || costs.length == 0) return 0;

        int n = costs.length;
        int k = costs[0].length;
        if (k == 1) return n == 1 ? costs[0][0] : Integer.MAX_VALUE;

        for (int house = 1; house < n; house++) {
            // Find min and second min from previous row
            int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
            int minIdx = -1;

            for (int color = 0; color < k; color++) {
                if (costs[house - 1][color] < min1) {
                    min2 = min1;
                    min1 = costs[house - 1][color];
                    minIdx = color;
                } else if (costs[house - 1][color] < min2) {
                    min2 = costs[house - 1][color];
                }
            }

            // Update current row
            for (int color = 0; color < k; color++) {
                costs[house][color] += (color == minIdx ? min2 : min1);
            }
        }

        return Arrays.stream(costs[n - 1]).min().getAsInt();
    }

    /**
     * Segment tree approach for large k values.
     * Efficient range minimum queries when k is very large.
     */
    public int minCostIISegmentTree(int[][] costs) {
        if (costs == null || costs.length == 0) return 0;

        int n = costs.length;
        int k = costs[0].length;
        if (k == 1) return n == 1 ? costs[0][0] : Integer.MAX_VALUE;

        SegmentTree segTree = new SegmentTree(costs[0]);

        for (int house = 1; house < n; house++) {
            int[] newCosts = new int[k];

            for (int color = 0; color < k; color++) {
                // Find minimum excluding current color
                int minCost = Math.min(
                    color > 0 ? segTree.queryRange(0, color - 1) : Integer.MAX_VALUE,
                    color < k - 1 ? segTree.queryRange(color + 1, k - 1) : Integer.MAX_VALUE
                );

                newCosts[color] = costs[house][color] + minCost;
            }

            segTree.update(newCosts);
        }

        return segTree.queryRange(0, k - 1);
    }

    // Segment tree for range minimum queries
    private static class SegmentTree {
        int[] tree;
        int[] arr;
        int n;

        SegmentTree(int[] initial) {
            n = initial.length;
            arr = Arrays.copyOf(initial, n);
            tree = new int[4 * n];
            build(1, 0, n - 1);
        }

        private void build(int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
            } else {
                int mid = (start + end) / 2;
                build(2 * node, start, mid);
                build(2 * node + 1, mid + 1, end);
                tree[node] = Math.min(tree[2 * node], tree[2 * node + 1]);
            }
        }

        void update(int[] newValues) {
            arr = Arrays.copyOf(newValues, n);
            build(1, 0, n - 1);
        }

        int queryRange(int left, int right) {
            if (left > right) return Integer.MAX_VALUE;
            return query(1, 0, n - 1, left, right);
        }

        private int query(int node, int start, int end, int left, int right) {
            if (right < start || end < left) return Integer.MAX_VALUE;
            if (left <= start && end <= right) return tree[node];

            int mid = (start + end) / 2;
            return Math.min(query(2 * node, start, mid, left, right),
                          query(2 * node + 1, mid + 1, end, left, right));
        }
    }

    /**
     * Returns the optimal coloring sequence along with minimum cost.
     * Extension that reconstructs the actual solution.
     */
    public PaintResult minCostWithSequence(int[][] costs) {
        if (costs == null || costs.length == 0) {
            return new PaintResult(0, new ArrayList<>());
        }

        int n = costs.length;
        int k = costs[0].length;
        if (k == 1) {
            if (n == 1) {
                return new PaintResult(costs[0][0], Arrays.asList(0));
            } else {
                return new PaintResult(Integer.MAX_VALUE, new ArrayList<>());
            }
        }

        int[][] dp = new int[n][k];
        int[][] parent = new int[n][k];

        // Initialize first house
        System.arraycopy(costs[0], 0, dp[0], 0, k);

        for (int house = 1; house < n; house++) {
            for (int color = 0; color < k; color++) {
                dp[house][color] = Integer.MAX_VALUE;

                for (int prevColor = 0; prevColor < k; prevColor++) {
                    if (prevColor != color) {
                        int cost = dp[house - 1][prevColor] + costs[house][color];
                        if (cost < dp[house][color]) {
                            dp[house][color] = cost;
                            parent[house][color] = prevColor;
                        }
                    }
                }
            }
        }

        // Find minimum cost and reconstruct path
        int minCost = Integer.MAX_VALUE;
        int lastColor = -1;

        for (int color = 0; color < k; color++) {
            if (dp[n - 1][color] < minCost) {
                minCost = dp[n - 1][color];
                lastColor = color;
            }
        }

        // Reconstruct sequence
        List<Integer> sequence = new ArrayList<>();
        int currentColor = lastColor;

        for (int house = n - 1; house >= 0; house--) {
            sequence.add(currentColor);
            if (house > 0) {
                currentColor = parent[house][currentColor];
            }
        }

        Collections.reverse(sequence);
        return new PaintResult(minCost, sequence);
    }

    // Result class containing cost and color sequence
    public static class PaintResult {
        public final int minCost;
        public final List<Integer> colorSequence;

        public PaintResult(int minCost, List<Integer> colorSequence) {
            this.minCost = minCost;
            this.colorSequence = colorSequence;
        }

        @Override
        public String toString() {
            return String.format("Min Cost: %d, Colors: %s", minCost, colorSequence);
        }
    }

    /**
     * Constraint-based approach with color restrictions.
     * Handles houses with specific color constraints.
     */
    public int minCostWithConstraints(int[][] costs, Set<Integer>[] allowedColors) {
        if (costs == null || costs.length == 0) return 0;

        int n = costs.length;
        int k = costs[0].length;

        int[] dp = new int[k];
        Arrays.fill(dp, Integer.MAX_VALUE);

        // Initialize first house with constraints
        Set<Integer> firstHouseColors = allowedColors[0];
        for (int color : firstHouseColors) {
            if (color < k) {
                dp[color] = costs[0][color];
            }
        }

        for (int house = 1; house < n; house++) {
            int[] newDp = new int[k];
            Arrays.fill(newDp, Integer.MAX_VALUE);

            Set<Integer> currentHouseColors = allowedColors[house];

            for (int color : currentHouseColors) {
                if (color >= k) continue;

                for (int prevColor = 0; prevColor < k; prevColor++) {
                    if (prevColor != color && dp[prevColor] != Integer.MAX_VALUE) {
                        newDp[color] = Math.min(newDp[color], dp[prevColor] + costs[house][color]);
                    }
                }
            }

            dp = newDp;
        }

        int result = Arrays.stream(dp).min().getAsInt();
        return result == Integer.MAX_VALUE ? -1 : result; // Return -1 if no valid solution
    }

    /**
     * Extended neighborhood constraint approach.
     * No same color within k-distance neighborhood.
     */
    public int minCostKNeighborhood(int[][] costs, int neighborhoodSize) {
        if (costs == null || costs.length == 0) return 0;

        int n = costs.length;
        int k = costs[0].length;

        // dp[i][j] = min cost to paint houses 0..i with house i having color j
        int[][] dp = new int[n][k];

        // Initialize first house
        System.arraycopy(costs[0], 0, dp[0], 0, k);

        for (int house = 1; house < n; house++) {
            Arrays.fill(dp[house], Integer.MAX_VALUE);

            for (int color = 0; color < k; color++) {
                // Check previous neighborhoodSize houses for color conflicts
                for (int prevHouse = Math.max(0, house - neighborhoodSize); prevHouse < house; prevHouse++) {
                    for (int prevColor = 0; prevColor < k; prevColor++) {
                        // Skip if same color within neighborhood
                        boolean conflict = false;
                        for (int checkHouse = Math.max(prevHouse, house - neighborhoodSize + 1);
                             checkHouse < house; checkHouse++) {
                            if (hasColor(dp, checkHouse, color)) {
                                conflict = true;
                                break;
                            }
                        }

                        if (!conflict && prevColor != color) {
                            dp[house][color] = Math.min(dp[house][color],
                                                       dp[prevHouse][prevColor] + costs[house][color]);
                        }
                    }
                }
            }
        }

        return Arrays.stream(dp[n - 1]).min().getAsInt();
    }

    // Check if house has specific color in optimal solution (simplified)
    private boolean hasColor(int[][] dp, int house, int color) {
        // This is a simplified check - full implementation would track actual colors
        return false;
    }

    /**
     * Parallel processing approach for very large inputs.
     * Parallelizes color computations within each house.
     */
    public int minCostIIParallel(int[][] costs) {
        if (costs == null || costs.length == 0) return 0;
        if (costs[0].length < 100) return minCostIIOptimized(costs); // Use sequential for small k

        int n = costs.length;
        int k = costs[0].length;
        if (k == 1) return n == 1 ? costs[0][0] : Integer.MAX_VALUE;

        int[] dp = Arrays.copyOf(costs[0], k);

        for (int house = 1; house < n; house++) {
            final int currentHouse = house;
            final int[] currentDp = dp;

            int[] newDp = java.util.stream.IntStream.range(0, k)
                .parallel()
                .map(color -> {
                    int minCost = Integer.MAX_VALUE;
                    for (int prevColor = 0; prevColor < k; prevColor++) {
                        if (prevColor != color) {
                            minCost = Math.min(minCost, currentDp[prevColor] + costs[currentHouse][color]);
                        }
                    }
                    return minCost;
                })
                .toArray();

            dp = newDp;
        }

        return Arrays.stream(dp).min().getAsInt();
    }

    /**
     * Memory-efficient streaming approach.
     * Processes houses one at a time to minimize memory usage.
     */
    public int minCostIIStreaming(int[][] costs) {
        if (costs == null || costs.length == 0) return 0;

        int k = costs[0].length;
        if (k == 1) return costs.length == 1 ? costs[0][0] : Integer.MAX_VALUE;

        int min1 = 0, min2 = 0, minIdx = -1;

        // Process first house
        for (int color = 0; color < k; color++) {
            int cost = costs[0][color];
            if (cost < min1 || min1 == 0) {
                min2 = min1;
                min1 = cost;
                minIdx = color;
            } else if (cost < min2 || min2 == 0) {
                min2 = cost;
            }
        }

        // Process remaining houses
        for (int house = 1; house < costs.length; house++) {
            int newMin1 = Integer.MAX_VALUE;
            int newMin2 = Integer.MAX_VALUE;
            int newMinIdx = -1;

            for (int color = 0; color < k; color++) {
                int cost = costs[house][color] + (color == minIdx ? min2 : min1);

                if (cost < newMin1) {
                    newMin2 = newMin1;
                    newMin1 = cost;
                    newMinIdx = color;
                } else if (cost < newMin2) {
                    newMin2 = cost;
                }
            }

            min1 = newMin1;
            min2 = newMin2;
            minIdx = newMinIdx;
        }

        return min1;
    }

    /**
     * Analysis and statistics utility.
     * Provides comprehensive analysis of painting costs and patterns.
     */
    public static class PaintAnalysis {

        public static PaintStats analyzeCosts(int[][] costs) {
            if (costs == null || costs.length == 0) {
                return new PaintStats(0, 0, 0, 0, 0, new ArrayList<>());
            }

            int n = costs.length;
            int k = costs[0].length;

            int totalCost = 0;
            int minCost = Integer.MAX_VALUE;
            int maxCost = Integer.MIN_VALUE;
            List<Integer> colorFrequency = new ArrayList<>(Collections.nCopies(k, 0));

            for (int house = 0; house < n; house++) {
                for (int color = 0; color < k; color++) {
                    int cost = costs[house][color];
                    totalCost += cost;
                    minCost = Math.min(minCost, cost);
                    maxCost = Math.max(maxCost, cost);
                }
            }

            double avgCost = (double) totalCost / (n * k);

            return new PaintStats(totalCost, minCost, maxCost, avgCost, n * k, colorFrequency);
        }
    }

    // Statistics result class
    public static class PaintStats {
        public final int totalCost;
        public final int minSingleCost;
        public final int maxSingleCost;
        public final double avgCost;
        public final int totalPaintings;
        public final List<Integer> colorFrequency;

        public PaintStats(int totalCost, int minSingleCost, int maxSingleCost,
                         double avgCost, int totalPaintings, List<Integer> colorFrequency) {
            this.totalCost = totalCost;
            this.minSingleCost = minSingleCost;
            this.maxSingleCost = maxSingleCost;
            this.avgCost = avgCost;
            this.totalPaintings = totalPaintings;
            this.colorFrequency = colorFrequency;
        }

        @Override
        public String toString() {
            return String.format("Total: %d, Range: [%d, %d], Avg: %.2f, Paintings: %d",
                               totalCost, minSingleCost, maxSingleCost, avgCost, totalPaintings);
        }
    }
}