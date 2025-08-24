package backtrack;

import java.util.ArrayList;
import java.util.List;

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