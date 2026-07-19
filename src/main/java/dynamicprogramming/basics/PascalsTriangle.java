package dynamicprogramming.basics;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Pascal's Triangle
 *
 * Given numRows, generate the first numRows rows of Pascal's triangle. Each
 * border value is 1, and each interior value is the sum of the two values
 * directly above it in the previous row.
 *
 * Leetcode: https://leetcode.com/problems/pascals-triangle/ (Easy)
 * Rating:   acceptance 79.2% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Row construction | Combinatorics
 *
 * Example:
 *   Input:  numRows = 5
 *   Output: [[1], [1, 1], [1, 2, 1], [1, 3, 3, 1], [1, 4, 6, 4, 1]]
 *   Why:    every middle entry is formed by adding the two entries above it.
 *
 * Follow-ups:
 *   1. Return only the kth row?
 *      Update one row in place from right to left in O(k) space.
 *   2. How do you handle very large row values?
 *      Use BigInteger or compute answers modulo a requested value.
 *   3. Can one element be computed directly?
 *      Use the binomial coefficient recurrence C(r, c) = C(r, c - 1) * (r - c + 1) / c.
 *
 * Related: Pascal's Triangle II (119).
 */
public class PascalsTriangle {

    public static void main(String[] args) {
        PascalsTriangle solver = new PascalsTriangle();
        int[] inputs = { 5, 1, 0 };
        String[] expected = {
            "[[1], [1, 1], [1, 2, 1], [1, 3, 3, 1], [1, 4, 6, 4, 1]]",
            "[[1]]",
            "[]"
        };

        for (int i = 0; i < inputs.length; i++) {
            List<List<Integer>> got = solver.generate(inputs[i]);
            System.out.printf("numRows=%d -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }

        /**
     * Intuition: each row can be built once the previous row is known. The two
     * ends are always 1 because they have only one parent above them. Every
     * interior column j has exactly two parents, prevRow[j - 1] and prevRow[j],
     * so their sum is the next value.
     *
     * Algorithm:
     *   1. Return an empty result for non-positive row counts.
     *   2. Add the first row [1].
     *   3. For each later row, add border 1s and fill middle values from the previous row.
     *
     * Time:  O(n^2) - the triangle contains 1 + 2 + ... + n values to write.
     * Space: O(n^2) - the returned triangle stores every generated value.
     *
     * @param numRows number of rows to generate
     * @return first numRows rows of Pascal's triangle
     */
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<>();

        if (numRows <= 0) {
            return result;
        }

        // First row is always [1]
        List<Integer> firstRow = new ArrayList<>();
        firstRow.add(1);
        result.add(firstRow);

        for (int rowNum = 1; rowNum < numRows; rowNum++) {
            List<Integer> prevRow = result.get(rowNum - 1);
            List<Integer> currentRow = new ArrayList<>();

            // First element is always 1
            currentRow.add(1);

            // Calculate middle elements
            for (int j = 1; j < rowNum; j++) {
                currentRow.add(prevRow.get(j - 1) + prevRow.get(j));
            }

            // Last element is always 1
            currentRow.add(1);

            result.add(currentRow);
        }

        return result;
    }

    /**
     * Mathematical Approach (Using Combinations)
     *
     * Approach:
     * 1. Each element at position (i, j) can be calculated using combinations: C(i, j)
     * 2. C(i, j) = C(i, j-1) * (i - j + 1) / j
     * 3. This approach is more efficient for getting a specific element but not better for the whole triangle
     *
     * Time Complexity: O(n²)
     * Space Complexity: O(1) excluding output
     */
    public List<List<Integer>> generateMath(int numRows) {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < numRows; i++) {
            List<Integer> row = new ArrayList<>();
            int value = 1;  // C(i, 0) = 1

            for (int j = 0; j <= i; j++) {
                row.add(value);
                // Calculate C(i, j+1) using C(i, j)
                // C(i, j+1) = C(i, j) * (i - j) / (j + 1)
                value = value * (i - j) / (j + 1);
            }

            result.add(row);
        }

        return result;
    }


    // Helper method to print the triangle
    private static void printTriangle(List<List<Integer>> triangle) {
        for (List<Integer> row : triangle) {
            System.out.println(row);
        }
    }
}
