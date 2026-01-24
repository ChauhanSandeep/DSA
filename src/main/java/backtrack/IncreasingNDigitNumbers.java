package backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * ✅ Problem: Increasing N-Digit Numbers (Strictly Increasing Digits)
 *
 * Generate all N-digit integers whose digits are strictly increasing from left to right.
 * Digits are chosen from 1..9 (no leading zeros), and each subsequent digit must be greater than the previous.
 * Return the list in ascending numerical order.
 *
 * Leetcode: https://leetcode.com/problems/sequential-digits/ (LC 1291, related), 
 * https://leetcode.com/problems/combinations/ (LC 77, pattern)
 *
 * Example:
 * Input: N = 1
 * Output: [1, 2, 3, 4, 5, 6, 7, 8, 9]
 *
 * Input: N = 2
 * Output: [12, 13, 14, 15, 16, 17, 18, 19, 23, 24, 25, 26, 27, 28, 29, 34, 35, 36, 37, 38, 39, 45, 46, 47, 48, 49, 56, 57, 58, 59, 67, 68, 69, 78, 79, 89]
 *
 * Input: N = 3
 * Output: [123, 124, 125, 126, 127, 128, 129, 134, 135, 136, 137, 138, 139, 145, 146, 147, 148, 149, 156, 157, 158, 159, 167, 168, 169, 178, 179, 189, 234, 235, ..., 789]
 *
 * 🔍 Intuition:
 * Use backtracking (DFS) to build numbers digit by digit. From the last chosen digit d, the next digit can be any value in [d+1..9],
 * until the length reaches N. This is equivalent to generating combinations of size N from digits {1..9} and mapping each combination to a number.
 *
 * Constraints:
 * - 1 ≤ N ≤ 9
 * - Digits strictly increasing; no repeats; no leading zeros
 * - Digits drawn from 1..9
 * - Output should be in ascending numerical order
 *
 * Follow-up Questions:
 * 1) What if we allow non-strictly increasing sequences (i.e., non-decreasing digits with equality allowed)?
 * 2) How to generate N-digit numbers with strictly decreasing digits?
 * 3) Can you generate the k-th number in lexicographic order without generating all (combinatorial indexing)?
 * 4) How to extend this to arbitrary digit ranges (e.g., allow 0..9, or a custom subset/alphabet)?
 * 5) What if we want numbers with at most N digits instead of exactly N digits?
 * LeetCode Contest Rating: 1374
 */
public class IncreasingNDigitNumbers {

    /**
     * Generates all N-digit numbers with digits in strictly increasing order.
     *
     * Steps:
     *   - Use backtracking to generate increasing sequences from digits 1 to 9.
     *   - At each step, ensure that the next digit is greater than the previous.
     *   - Stop when sequence length reaches N.
     *
     * Algorithm: Backtracking (DFS)
     * Time Complexity: O(C(9, N)) => total combinations
     * Space Complexity: O(N) for recursion stack
     *
     * @param n Number of digits
     * @return List of valid N-digit increasing numbers
     */
    public List<Integer> generateIncreasingNDigitNumbers(int n) {
        List<Integer> result = new ArrayList<>();
      if (n <= 0 || n > 9) {
        return result; // Edge case: Invalid N
      }
        backtrack(1, n, new ArrayList<>(), result);
        return result;
    }

    // Helper method for backtracking
    private void backtrack(int startDigit, int targetLength, List<Integer> currentDigits, List<Integer> result) {
        if (currentDigits.size() == targetLength) {
            // Convert digit list to number
            int number = 0;
            for (int digit : currentDigits) {
                number = number * 10 + digit;
            }
            result.add(number);
            return;
        }

        for (int digit = startDigit; digit <= 9; digit++) {
            currentDigits.add(digit);
            backtrack(digit + 1, targetLength, currentDigits, result);
            currentDigits.remove(currentDigits.size() - 1); // backtrack
        }
    }

    /**
     * Optimized version (no backtracking): Uses combination logic
     *
     * Steps:
     *   - Since we want combinations of size N from digits 1 to 9 in increasing order,
     *     we can directly generate combinations using recursion.
     *
     * Time Complexity: O(C(9, N))
     * Space Complexity: O(N) recursion depth
     */
    public List<Integer> generateUsingCombinations(int n) {
        List<Integer> result = new ArrayList<>();
      if (n <= 0 || n > 9) {
        return result;
      }
        generateCombinations(1, 0, n, 0, result);
        return result;
    }

    // Recursive combination generator using number arithmetic directly
    private void generateCombinations(int currentDigit, int digitsChosen, int targetLength, int currentNumber, List<Integer> result) {
        if (digitsChosen == targetLength) {
            result.add(currentNumber);
            return;
        }

        for (int digit = currentDigit; digit <= 9; digit++) {
            generateCombinations(digit + 1, digitsChosen + 1, targetLength, currentNumber * 10 + digit, result);
        }
    }

    // Demo / Test Runner
    public static void main(String[] args) {
        IncreasingNDigitNumbers solver = new IncreasingNDigitNumbers();
        int N = 2;

        System.out.println("Backtracking approach:");
        List<Integer> output1 = solver.generateIncreasingNDigitNumbers(N);
        System.out.println(output1);

        System.out.println("Combinatorial approach:");
        List<Integer> output2 = solver.generateUsingCombinations(N);
        System.out.println(output2);
    }
}