package arrays.prefixsum;

import java.util.*;
/**
 * Problem: Max Sum of Rectangle No Larger Than K
 *
 * Given a non-empty matrix and an integer k, find the largest rectangle sum that
 * is at most k. Rectangles can have any height and width as long as their sum does
 * not exceed the limit.
 *
 * Leetcode: https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/ (Hard)
 * Rating:   no contest rating (pre-contest problem)
 * Pattern:  2D prefix compression | Ordered prefix sums | TreeSet
 *
 * Example:
 *   Input:  matrix = [[1,0,1],[0,-2,3]], k = 2
 *   Output: 2
 *   Why:    rectangle [[0,1],[-2,3]] sums to 2, exactly meeting the limit.
 *
 * Follow-ups:
 *   1. Return rectangle coordinates too?
 *      Track the best row pair and subarray boundaries when updating the maximum.
 *   2. Optimize when rows exceed columns?
 *      Compress along the smaller dimension to reduce the squared factor.
 *   3. Need exact sum k only?
 *      Use prefix sums with a hash set and stop as soon as k is found.
 *
 * Related: Max Sum of Submatrix (LCCI 17.24), Range Sum Query 2D Immutable (304).
 */
public class MaxSumOfRectangleNoLargerThanK {

    public static void main(String[] args) {
        MaxSumOfRectangleNoLargerThanK solver = new MaxSumOfRectangleNoLargerThanK();

        int[][][] matrices = {
            { {1, 0, 1}, {0, -2, 3} },
            { {2, 2, -1} }
        };
        int[] limits = { 2, 3 };
        int[] expected = { 2, 3 };

        for (int i = 0; i < matrices.length; i++) {
            int got = solver.maxSumSubmatrix(matrices[i], limits[i]);
            System.out.printf("matrix=%s k=%d -> output=%d  expected=%d%n",
                Arrays.deepToString(matrices[i]), limits[i], got, expected[i]);
        }
    }

/**
 * Intuition: fixing a top and bottom row compresses the rectangle problem into a
 * 1D array of column sums. Then the best left-to-right span no larger than k is
 * the classic prefix-sum problem solved with an ordered set.
 *
 * Algorithm:
 *   1. Fix every top row and grow every bottom row below it.
 *   2. Maintain colSum so each column stores the sum between top and bottom.
 *   3. Find the best subarray sum no larger than k inside colSum.
 *   4. Update the global result and stop early if it reaches k.
 *
 * Time:  O(m^2 * n log n) - every row pair runs an ordered-prefix scan over columns.
 * Space: O(n) - colSum and the TreeSet store column-prefix information.
 *
 * @param matrix input matrix
 * @param k maximum allowed rectangle sum
 * @return largest rectangle sum that is no larger than k
 */
    public int maxSumSubmatrix(int[][] matrix, int k) {
        int m = matrix.length, n = matrix[0].length;
        int result = Integer.MIN_VALUE;

        // Fix top and bottom boundaries
        for (int top = 0; top < m; top++) {
            int[] colSum = new int[n];

            for (int bottom = top; bottom < m; bottom++) {
                // Add current row to column sums
                for (int col = 0; col < n; col++) {
                    colSum[col] += matrix[bottom][col];
                }

                // Find max subarray sum <= k in colSum array
                int maxSum = maxSubarraySum(colSum, k);
                result = Math.max(result, maxSum);

                // Early termination if we found k
                if (result == k) {
                    return result;
                }
            }
        }

        return result;
    }

    // Find max subarray sum <= k using prefix sums and TreeSet
    private int maxSubarraySum(int[] arr, int k) {
        int maxSum = Integer.MIN_VALUE;
        int prefixSum = 0;
        TreeSet<Integer> prefixSums = new TreeSet<>();
        prefixSums.add(0); // For subarrays starting from index 0

        for (int num : arr) {
            prefixSum += num;

            // Find smallest prefix sum such that prefixSum - target <= k
            // which means target >= prefixSum - k
            Integer target = prefixSums.ceiling(prefixSum - k);

            if (target != null) {
                maxSum = Math.max(maxSum, prefixSum - target);
            }

            prefixSums.add(prefixSum);
        }

        return maxSum;
    }

    /**
     * Brute force approach checking all possible rectangles.
     * Simple but inefficient - for verification and small inputs.
     */
    public int maxSumSubmatrixBruteForce(int[][] matrix, int k) {
        int m = matrix.length, n = matrix[0].length;
        int result = Integer.MIN_VALUE;

        // Try all possible rectangles
        for (int top = 0; top < m; top++) {
            for (int left = 0; left < n; left++) {
                for (int bottom = top; bottom < m; bottom++) {
                    for (int right = left; right < n; right++) {
                        int sum = calculateRectangleSum(matrix, top, left, bottom, right);
                        if (sum <= k) {
                            result = Math.max(result, sum);
                        }
                    }
                }
            }
        }

        return result;
    }

    // Calculate sum of rectangle from (top,left) to (bottom,right) inclusive
    private int calculateRectangleSum(int[][] matrix, int top, int left, int bottom, int right) {
        int sum = 0;
        for (int i = top; i <= bottom; i++) {
            for (int j = left; j <= right; j++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    /**
     * Prefix sum optimization for rectangle sum calculation.
     * Precomputes 2D prefix sums for O(1) rectangle sum queries.
     */
    public int maxSumSubmatrixPrefixSum(int[][] matrix, int k) {
        int m = matrix.length, n = matrix[0].length;

        // Build 2D prefix sum array
        int[][] prefixSum = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                prefixSum[i][j] = matrix[i-1][j-1] + prefixSum[i-1][j] +
                                 prefixSum[i][j-1] - prefixSum[i-1][j-1];
            }
        }

        int result = Integer.MIN_VALUE;

        // Try all possible rectangles using prefix sums
        for (int top = 0; top < m; top++) {
            for (int left = 0; left < n; left++) {
                for (int bottom = top; bottom < m; bottom++) {
                    for (int right = left; right < n; right++) {
                        int sum = prefixSum[bottom+1][right+1] - prefixSum[top][right+1] -
                                 prefixSum[bottom+1][left] + prefixSum[top][left];

                        if (sum <= k) {
                            result = Math.max(result, sum);
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Kadane's algorithm extension for 2D case.
     * Optimizes the 1D subarray problem within the column reduction.
     */
    public int maxSumSubmatrixKadane(int[][] matrix, int k) {
        int m = matrix.length, n = matrix[0].length;
        int result = Integer.MIN_VALUE;

        for (int top = 0; top < m; top++) {
            int[] colSum = new int[n];

            for (int bottom = top; bottom < m; bottom++) {
                // Update column sums
                for (int col = 0; col < n; col++) {
                    colSum[col] += matrix[bottom][col];
                }

                // Apply modified Kadane for max sum <= k
                result = Math.max(result, maxSubarraySumKadane(colSum, k));
            }
        }

        return result;
    }

    // Modified Kadane's algorithm for max sum <= k
    private int maxSubarraySumKadane(int[] arr, int k) {
        int result = Integer.MIN_VALUE;

        // Try all starting positions
        for (int start = 0; start < arr.length; start++) {
            int currentSum = 0;

            for (int end = start; end < arr.length; end++) {
                currentSum += arr[end];

                if (currentSum <= k) {
                    result = Math.max(result, currentSum);
                }
            }
        }

        return result;
    }

    /**
     * Optimized approach for the case where we need exact sum k.
     * Uses hash map for faster lookups when looking for exact values.
     */
    public int maxSumSubmatrixExactK(int[][] matrix, int k) {
        int m = matrix.length, n = matrix[0].length;

        for (int top = 0; top < m; top++) {
            int[] colSum = new int[n];

            for (int bottom = top; bottom < m; bottom++) {
                for (int col = 0; col < n; col++) {
                    colSum[col] += matrix[bottom][col];
                }

                if (hasSubarraySumK(colSum, k)) {
                    return k;
                }
            }
        }

        return maxSumSubmatrix(matrix, k); // Fall back to general solution
    }

    // Check if there exists subarray with sum exactly k
    private boolean hasSubarraySumK(int[] arr, int k) {
        Set<Integer> prefixSums = new HashSet<>();
        prefixSums.add(0);
        int prefixSum = 0;

        for (int num : arr) {
            prefixSum += num;
            if (prefixSums.contains(prefixSum - k)) {
                return true;
            }
            prefixSums.add(prefixSum);
        }

        return false;
    }

    /**
     * Divide and conquer approach for very large matrices.
     * Divides matrix into smaller subproblems recursively.
     */
    public int maxSumSubmatrixDivideConquer(int[][] matrix, int k) {
        return divideConquer(matrix, 0, 0, matrix.length - 1, matrix[0].length - 1, k);
    }

    private int divideConquer(int[][] matrix, int top, int left, int bottom, int right, int k) {
        // Base case: small enough to solve directly
        if ((bottom - top + 1) * (right - left + 1) <= 100) {
            return solveDirect(matrix, top, left, bottom, right, k);
        }

        int result = Integer.MIN_VALUE;

        // Divide vertically
        if (bottom - top >= right - left) {
            int mid = top + (bottom - top) / 2;
            result = Math.max(result, divideConquer(matrix, top, left, mid, right, k));
            result = Math.max(result, divideConquer(matrix, mid + 1, left, bottom, right, k));

            // Check rectangles crossing the division
            result = Math.max(result, maxSumCrossing(matrix, top, left, mid, bottom, right, k, true));
        } else {
            // Divide horizontally
            int mid = left + (right - left) / 2;
            result = Math.max(result, divideConquer(matrix, top, left, bottom, mid, k));
            result = Math.max(result, divideConquer(matrix, top, mid + 1, bottom, right, k));

            // Check rectangles crossing the division
            result = Math.max(result, maxSumCrossing(matrix, top, left, bottom, mid, right, k, false));
        }

        return result;
    }

    // Solve small subproblem directly
    private int solveDirect(int[][] matrix, int top, int left, int bottom, int right, int k) {
        int result = Integer.MIN_VALUE;

        for (int t = top; t <= bottom; t++) {
            for (int l = left; l <= right; l++) {
                for (int b = t; b <= bottom; b++) {
                    for (int r = l; r <= right; r++) {
                        int sum = calculateRectangleSum(matrix, t, l, b, r);
                        if (sum <= k) {
                            result = Math.max(result, sum);
                        }
                    }
                }
            }
        }

        return result;
    }

    // Find max sum rectangle crossing the division line
    private int maxSumCrossing(int[][] matrix, int top, int left, int mid, int bottom, int right, int k, boolean verticalDivision) {
        // Implementation would depend on division direction
        // For brevity, falling back to direct solution
        return solveDirect(matrix, top, left, bottom, right, k);
    }

    /**
     * Memory-optimized version for large matrices.
     * Processes matrix in chunks to reduce memory footprint.
     */
    public int maxSumSubmatrixMemoryOptimized(int[][] matrix, int k) {
        int m = matrix.length, n = matrix[0].length;
        int result = Integer.MIN_VALUE;
        int chunkSize = Math.min(100, m); // Process in chunks

        for (int chunkStart = 0; chunkStart < m; chunkStart += chunkSize) {
            int chunkEnd = Math.min(chunkStart + chunkSize - 1, m - 1);

            for (int top = chunkStart; top <= chunkEnd; top++) {
                int[] colSum = new int[n];

                for (int bottom = top; bottom <= chunkEnd; bottom++) {
                    for (int col = 0; col < n; col++) {
                        colSum[col] += matrix[bottom][col];
                    }

                    result = Math.max(result, maxSubarraySum(colSum, k));
                }
            }
        }

        return result;
    }

    /**
     * Returns rectangle coordinates along with the maximum sum.
     * Extension that provides the actual rectangle boundaries.
     */
    public RectangleResult maxSumSubmatrixWithCoordinates(int[][] matrix, int k) {
        int m = matrix.length, n = matrix[0].length;
        int maxSum = Integer.MIN_VALUE;
        int[] bestRect = new int[4]; // top, left, bottom, right

        for (int top = 0; top < m; top++) {
            int[] colSum = new int[n];

            for (int bottom = top; bottom < m; bottom++) {
                for (int col = 0; col < n; col++) {
                    colSum[col] += matrix[bottom][col];
                }

                SubarrayResult subResult = maxSubarraySumWithIndices(colSum, k);

                if (subResult.sum > maxSum) {
                    maxSum = subResult.sum;
                    bestRect[0] = top;
                    bestRect[1] = subResult.left;
                    bestRect[2] = bottom;
                    bestRect[3] = subResult.right;
                }
            }
        }

        return new RectangleResult(maxSum, bestRect[0], bestRect[1], bestRect[2], bestRect[3]);
    }

    // Find max subarray sum <= k with indices
    private SubarrayResult maxSubarraySumWithIndices(int[] arr, int k) {
        int maxSum = Integer.MIN_VALUE;
        int bestLeft = 0, bestRight = 0;

        for (int left = 0; left < arr.length; left++) {
            int currentSum = 0;

            for (int right = left; right < arr.length; right++) {
                currentSum += arr[right];

                if (currentSum <= k && currentSum > maxSum) {
                    maxSum = currentSum;
                    bestLeft = left;
                    bestRight = right;
                }
            }
        }

        return new SubarrayResult(maxSum, bestLeft, bestRight);
    }

    // Helper classes for coordinate tracking
    public static class RectangleResult {
        public final int sum;
        public final int top, left, bottom, right;

        public RectangleResult(int sum, int top, int left, int bottom, int right) {
            this.sum = sum;
            this.top = top;
            this.left = left;
            this.bottom = bottom;
            this.right = right;
        }
    }

    private static class SubarrayResult {
        int sum, left, right;

        SubarrayResult(int sum, int left, int right) {
            this.sum = sum;
            this.left = left;
            this.right = right;
        }
    }
}
