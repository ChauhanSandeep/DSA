package arrays.twopointers;

import java.util.Arrays;


/**
 * Problem: Sort Transformed Array
 *
 * Given sorted nums and a quadratic function f(x) = ax^2 + bx + c, return the
 * transformed values in sorted order. The primary method uses the parabola shape
 * to merge the transformed extremes in linear time.
 *
 * Leetcode: https://leetcode.com/problems/sort-transformed-array/ (Medium)
 * Rating:   acceptance 58.2% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Two pointers | Quadratic extremes
 *
 * Example:
 *   Input:  nums = [-4,-2,2,4], a = 1, b = 3, c = 5
 *   Output: [3,9,15,33]
 *   Why:    transformed values are [9,3,15,33], which sort to [3,9,15,33].
 *
 * Follow-ups:
 *   1. What if the function is linear?
 *      The same code treats a = 0 with the a >= 0 branch, but explicit reversal can be clearer.
 *   2. What if coefficients are floating point?
 *      Use double transformation and compare doubles carefully for sorted output.
 *   3. Can this work for cubic functions?
 *      Not with only the two extremes; higher-degree functions may have more turning points.
 *
 * Related: Squares of a Sorted Array (977), Merge Sorted Array (88).
 */
public class SortTransformedArray {

public static void main(String[] args) {
    SortTransformedArray solver = new SortTransformedArray();
    int[][] inputs = { {-4, -2, 2, 4}, {-4, -2, 2, 4} };
    int[][] coefficients = { {1, 3, 5}, {-1, 3, 5} };
    int[][] expected = { {3, 9, 15, 33}, {-23, -5, 1, 7} };

    for (int i = 0; i < inputs.length; i++) {
        int[] got = solver.sortTransformedArray(inputs[i], coefficients[i][0], coefficients[i][1], coefficients[i][2]);
        System.out.printf("nums=%s coeff=%s -> %s  expected=%s%n",
            Arrays.toString(inputs[i]), Arrays.toString(coefficients[i]),
            Arrays.toString(got), Arrays.toString(expected[i]));
    }
}

    /**
 * Intuition: on a sorted input, a quadratic's extreme transformed values are
 * at the ends when the parabola opens upward, and the smallest transformed
 * values are at the ends when it opens downward. Comparing transformed left
 * and right values lets us fill the result from the correct side.
 *
 * Algorithm:
 *   1. Start left at 0 and right at the last index.
 *   2. If a >= 0, compare transformed ends and fill result from right to left.
 *   3. If a < 0, compare transformed ends and fill result from left to right.
 *   4. Move the pointer whose transformed value was written.
 *
 * Time:  O(n) - each pointer moves across the array once.
 * Space: O(n) - result stores all transformed values.
 *
 * @param nums sorted input values
 * @param a coefficient of x^2
 * @param b coefficient of x
 * @param c constant term
 * @return transformed values in sorted order
 */
    public int[] sortTransformedArray(int[] nums, int a, int b, int c) {
        int length = nums.length;
        int[] result = new int[length];
        int left = 0, right = length - 1;

        if (a >= 0) {
            // Parabola opens upward or is linear with positive slope
            // Fill result from right to left (largest to smallest)
            int index = length - 1;
            while (left <= right) {
                int leftVal = transform(nums[left], a, b, c);
                int rightVal = transform(nums[right], a, b, c);

                if (leftVal >= rightVal) {
                    result[index--] = leftVal;
                    left++;
                } else {
                    result[index--] = rightVal;
                    right--;
                }
            }
        } else {
            // Parabola opens downward
            // Fill result from left to right (smallest to largest)
            int index = 0;
            while (left <= right) {
                int leftVal = transform(nums[left], a, b, c);
                int rightVal = transform(nums[right], a, b, c);

                if (leftVal <= rightVal) {
                    result[index++] = leftVal;
                    left++;
                } else {
                    result[index++] = rightVal;
                    right--;
                }
            }
        }

        return result;
    }

    // Helper method to compute quadratic transformation
    private int transform(int x, int a, int b, int c) {
        return a * x * x + b * x + c;
    }

    /**
     * Brute force approach: transform all then sort
     * Time Complexity: O(n log n), Space Complexity: O(n)
     */
    public int[] sortTransformedArrayBruteForce(int[] nums, int a, int b, int c) {
        int[] result = new int[nums.length];

        // Transform all values
        for (int i = 0; i < nums.length; i++) {
            result[i] = transform(nums[i], a, b, c);
        }

        // Sort the result
        Arrays.sort(result);
        return result;
    }

    /**
     * Alternative two-pointer approach with explicit case handling
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int[] sortTransformedArrayExplicit(int[] nums, int a, int b, int c) {
        int n = nums.length;
        int[] result = new int[n];
        int left = 0, right = n - 1;

        if (a > 0) {
            // Upward parabola: fill from right (largest values first)
            int index = n - 1;
            while (left <= right) {
                int leftVal = transform(nums[left], a, b, c);
                int rightVal = transform(nums[right], a, b, c);

                if (leftVal >= rightVal) {
                    result[index--] = leftVal;
                    left++;
                } else {
                    result[index--] = rightVal;
                    right--;
                }
            }
        } else if (a < 0) {
            // Downward parabola: fill from left (smallest values first)
            int index = 0;
            while (left <= right) {
                int leftVal = transform(nums[left], a, b, c);
                int rightVal = transform(nums[right], a, b, c);

                if (leftVal <= rightVal) {
                    result[index++] = leftVal;
                    left++;
                } else {
                    result[index++] = rightVal;
                    right--;
                }
            }
        } else {
            // Linear function (a = 0)
            if (b >= 0) {
                // Non-decreasing: transform in order
                for (int i = 0; i < n; i++) {
                    result[i] = transform(nums[i], a, b, c);
                }
            } else {
                // Decreasing: transform in reverse order
                for (int i = 0; i < n; i++) {
                    result[i] = transform(nums[n - 1 - i], a, b, c);
                }
            }
        }

        return result;
    }

    /**
     * Using streams for functional approach
     * Time Complexity: O(n log n), Space Complexity: O(n)
     */
    public int[] sortTransformedArrayStream(int[] nums, int a, int b, int c) {
        return Arrays.stream(nums)
                .map(x -> transform(x, a, b, c))
                .sorted()
                .toArray();
    }

    /**
     * Finding vertex of parabola for optimization
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int[] sortTransformedArrayWithVertex(int[] nums, int a, int b, int c) {
        if (a == 0) {
            return sortTransformedArrayExplicit(nums, a, b, c);
        }

        int n = nums.length;
        int[] result = new int[n];

        // Vertex x-coordinate: -b/(2a)
        double vertex = -b / (2.0 * a);

        // Find closest indices to vertex
        int left = 0, right = n - 1;
        while (left < right && nums[left + 1] <= vertex) left++;
        while (right > 0 && nums[right - 1] >= vertex) right--;

        // Use standard two-pointer approach from vertex outward
        return sortTransformedArray(nums, a, b, c);
    }

    /**
     * Helper method to verify parabola properties
     */
    public void analyzeParabola(int a, int b, int c) {
        if (a > 0) {
            System.out.println("Parabola opens upward");
        } else if (a < 0) {
            System.out.println("Parabola opens downward");
        } else {
            System.out.println("Linear function");
        }

        if (a != 0) {
            double vertex = -b / (2.0 * a);
            int vertexValue = (int) transform((int) vertex, a, b, c);
            System.out.printf("Vertex at x=%.2f, y=%d\n", vertex, vertexValue);
        }
    }

    /**
     * Generic transformation function for extensibility
     */
    public interface TransformFunction {
        int apply(int x);
    }

    public int[] sortTransformedGeneric(int[] nums, TransformFunction func) {
        return Arrays.stream(nums)
                .map(func::apply)
                .sorted()
                .toArray();
    }
}
