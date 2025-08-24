package dynamicprogramming;

import java.util.*;

/**
 * 679. 24 Game
 *
 * Problem: Given 4 cards with numbers on them, determine if we can use +, -, *, /
 * operations to get 24. Each card must be used exactly once.
 *
 * Example:
 * Input: [4, 1, 8, 7]
 * Output: true
 * Explanation: (8-4) * (7-1) = 24
 *
 * LeetCode: https://leetcode.com/problems/24-game
 *
 * Follow-up questions:
 * Q: What if we need to find the actual expression?
 * A: Modify the backtracking to store the expression string alongside calculations.
 *
 * Q: What if we have n cards instead of 4?
 * A: Same algorithm works, complexity becomes factorial in n.
 *
 * Q: What about integer overflow?
 * A: Use double precision and check bounds, or use BigDecimal for exact arithmetic.
 */
public class TwentyFourGame {

    private static final double EPS = 1e-6;
    private static final double TARGET = 24.0;

    /**
     * Determines if 24 can be made from the given cards using basic arithmetic operations.
     *
     * Algorithm: Backtracking with all possible combinations of numbers and operations
     * - For each pair of numbers, try all 4 operations (+, -, *, /)
     * - Recursively solve with the remaining numbers plus the result
     * - Base case: when one number remains, check if it equals 24
     *
     * Time Complexity: O(1) - constant since we always have exactly 4 numbers
     * Space Complexity: O(1) - constant recursion depth and space
     */
    public boolean judgePoint24(int[] nums) {
        List<Double> numbers = new ArrayList<>();
        for (int num : nums) {
            numbers.add((double) num);
        }
        return solve(numbers);
    }

    // Backtracking helper to try all combinations
    private boolean solve(List<Double> numbers) {
        if (numbers.size() == 1) {
            return Math.abs(numbers.get(0) - TARGET) < EPS;
        }

        // Try all pairs of numbers
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = i + 1; j < numbers.size(); j++) {
                double a = numbers.get(i);
                double b = numbers.get(j);

                // Create new list without the two chosen numbers
                List<Double> nextNumbers = new ArrayList<>();
                for (int k = 0; k < numbers.size(); k++) {
                    if (k != i && k != j) {
                        nextNumbers.add(numbers.get(k));
                    }
                }

                // Try all operations
                List<Double> candidates = Arrays.asList(
                    a + b,  // addition
                    a - b,  // subtraction
                    b - a,  // subtraction (commutative)
                    a * b,  // multiplication
                    Math.abs(b) > EPS ? a / b : Double.NaN,  // division
                    Math.abs(a) > EPS ? b / a : Double.NaN   // division (commutative)
                );

                for (Double candidate : candidates) {
                    if (!candidate.isNaN()) {
                        nextNumbers.add(candidate);
                        if (solve(nextNumbers)) {
                            return true;
                        }
                        nextNumbers.remove(nextNumbers.size() - 1);
                    }
                }
            }
        }

        return false;
    }

    /**
     * Alternative approach using explicit operation enumeration.
     * More verbose but clearer about which operations are being tried.
     */
    public boolean judgePoint24Alternative(int[] nums) {
        return solveWithExplicitOps(Arrays.asList(
            (double) nums[0], (double) nums[1], (double) nums[2], (double) nums[3]
        ));
    }

    // Helper with explicit operations
    private boolean solveWithExplicitOps(List<Double> nums) {
        if (nums.size() == 1) {
            return Math.abs(nums.get(0) - TARGET) < EPS;
        }

        for (int i = 0; i < nums.size(); i++) {
            for (int j = i + 1; j < nums.size(); j++) {
                double a = nums.get(i);
                double b = nums.get(j);

                List<Double> remaining = new ArrayList<>();
                for (int k = 0; k < nums.size(); k++) {
                    if (k != i && k != j) {
                        remaining.add(nums.get(k));
                    }
                }

                // Try addition
                remaining.add(a + b);
                if (solveWithExplicitOps(remaining)) return true;
                remaining.remove(remaining.size() - 1);

                // Try subtraction (both ways)
                remaining.add(a - b);
                if (solveWithExplicitOps(remaining)) return true;
                remaining.remove(remaining.size() - 1);

                remaining.add(b - a);
                if (solveWithExplicitOps(remaining)) return true;
                remaining.remove(remaining.size() - 1);

                // Try multiplication
                remaining.add(a * b);
                if (solveWithExplicitOps(remaining)) return true;
                remaining.remove(remaining.size() - 1);

                // Try division (both ways, check for zero)
                if (Math.abs(b) > EPS) {
                    remaining.add(a / b);
                    if (solveWithExplicitOps(remaining)) return true;
                    remaining.remove(remaining.size() - 1);
                }

                if (Math.abs(a) > EPS) {
                    remaining.add(b / a);
                    if (solveWithExplicitOps(remaining)) return true;
                    remaining.remove(remaining.size() - 1);
                }
            }
        }

        return false;
    }
}