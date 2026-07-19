package dynamicprogramming.MiscellaneousDP;

import java.util.*;

/**
 * Problem: 24 Game
 *
 * Given four numbers, decide whether you can combine all of them with +, -, *, and
 * / to make exactly 24. Every card must be used once, and parentheses may be used
 * in any order.
 *
 * Leetcode: https://leetcode.com/problems/24-game/
 * Rating:   acceptance 59.6% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | Pair reduction | Floating-point search
 *
 * Example:
 *   Input:  cards = [4,1,8,7]
 *   Output: true
 *   Why:    (8 - 4) * (7 - 1) equals 24, using every card exactly once.
 *
 * Follow-ups:
 *   1. Return the expression that makes 24?
 *      Carry an expression string alongside each numeric value during recursion.
 *   2. Support n cards instead of four?
 *      The same pair-reduction search works, but the branching grows very quickly.
 *   3. Avoid floating-point precision concerns?
 *      Represent values as reduced rational numbers with numerator and denominator.
 *
 * Related: Different Ways to Add Parentheses (241), Expression Add Operators (282).
 */
public class TwentyFourGame {

    private static final double EPS = 1e-6;
    private static final double TARGET = 24.0;

    /**
     * Intuition: any fully parenthesized expression eventually combines two current
     * values into one value. So we can choose every pair, replace it with every
     * possible operation result, and recurse on the smaller list. When only one
     * number remains, it represents one complete expression tree. Because there are
     * always exactly four cards, exhaustive search is small enough and easier to
     * trust than trying to force a greedy rule.
     *
     * Algorithm:
     *   1. Convert the four integers to a list of doubles.
     *   2. Recursively choose every pair of current numbers.
     *   3. Replace the pair with each valid operation result and solve the smaller list.
     *   4. Accept when the final value is within EPS of 24.
     *
     * Time:  O(1) - the input size is fixed at four cards, so the search tree has constant size.
     * Space: O(1) - recursion depth and temporary lists are bounded by four cards.
     *
     * @param nums four card values
     * @return true if the cards can make 24
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

    public static void main(String[] args) {
        TwentyFourGame solver = new TwentyFourGame();
        int[][] inputs = {{1, 2, 1, 2}, {4, 1, 8, 7}, {1, 1, 1, 1}};
        boolean[] expected = {false, true, false};

        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.judgePoint24(inputs[i]);
            System.out.printf("cards=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }
}