package array;

import java.util.Arrays;


/**
 * Sort Transformed Array
 *
 * Problem: Transform sorted array using quadratic function f(x) = ax² + bx + c and return result in sorted order.
 *
 * Example: nums = [-4,-2,2,4], a = 1, b = 3, c = 5 -> Output: [3,9,15,33]
 * f(-4) = 16 - 12 + 5 = 9, f(-2) = 4 - 6 + 5 = 3, f(2) = 4 + 6 + 5 = 15, f(4) = 16 + 12 + 5 = 33
 *
 * LeetCode: https://leetcode.com/problems/sort-transformed-array
 *
 * Follow-up Questions:
 * - What if function is cubic or higher degree? (Use similar two-pointer approach)
 * - How to handle floating point coefficients? (Same algorithm applies)
 * - Can we avoid computing all values first? (Yes, current approach does this optimally)
 */
public class SortTransformedArray {

    /**
     * Sorts transformed array using two-pointer technique based on parabola properties.
     *
     * Algorithm:
     * 1. If a > 0: parabola opens upward, extremes have larger values
     * 2. If a < 0: parabola opens downward, extremes have smaller values
     * 3. If a = 0: linear function, order depends on sign of b
     * 4. Use two pointers from ends, compare transformed values, fill result accordingly
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(n) for result array
     *
     * @param nums sorted input array
     * @param a coefficient of x²
     * @param b coefficient of x
     * @param c constant term
     * @return sorted array of transformed values
     */
    public int[] sortTransformedArray(int[] nums, int a, int b, int c) {
        int n = nums.length;
        int[] result = new int[n];
        int left = 0, right = n - 1;

        if (a >= 0) {
            // Parabola opens upward or is linear with positive slope
            // Fill result from right to left (largest to smallest)
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
