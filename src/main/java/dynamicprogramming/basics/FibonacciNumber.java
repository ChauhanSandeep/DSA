package dynamicprogramming.basics;

/**
 * Problem: Fibonacci Number
 *
 * Return F(n), where F(0) = 0, F(1) = 1, and every later number is the sum of
 * the previous two. The sequence models any process where the next state depends
 * only on the two immediately preceding states.
 *
 * Leetcode: https://leetcode.com/problems/fibonacci-number/ (Easy)
 * Rating:   acceptance 74.4% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Rolling variables | Linear recurrence
 *
 * Example:
 *   Input:  position = 4
 *   Output: 3
 *   Why:    F(4) = F(3) + F(2) = 2 + 1 = 3.
 *
 * Follow-ups:
 *   1. Can this be solved in O(log n) time?
 *      Use matrix exponentiation on [[1,1],[1,0]].
 *   2. How do you avoid overflow for huge n?
 *      Compute modulo a chosen value or use BigInteger.
 *   3. What if many Fibonacci queries arrive?
 *      Precompute up to the maximum requested n or memoize results lazily.
 *
 * Related: Climbing Stairs (70), Tribonacci Number (1137).
 */
public class FibonacciNumber {

    public static void main(String[] args) {
        FibonacciNumber solver = new FibonacciNumber();
        int[] inputs = { 0, 4, 7 };
        int[] expected = { 0, 3, 13 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.fib(inputs[i]);
            System.out.printf("position=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }

        /**
     * Intuition: the value at each position depends only on the previous two
     * Fibonacci values. previousNumber holds F(i - 2) and currentNumber holds
     * F(i - 1) before each loop step. Adding them gives F(i), then the two
     * variables slide forward one position.
     *
     * Algorithm:
     *   1. Return the position itself for the base cases 0 and 1.
     *   2. Keep the two previous Fibonacci numbers in rolling variables.
     *   3. Iterate from 2 through position, computing and shifting the next value.
     *
     * Time:  O(n) - one loop iteration per Fibonacci position after the base cases.
     * Space: O(1) - only two rolling values and one temporary are stored.
     *
     * @param position zero-based Fibonacci index
     * @return Fibonacci number at that position
     */
    public int fib(int position) {
        // Base cases
        if (position <= 1) {
            return position;
        }

        // Initialize first two Fibonacci numbers
        int previousNumber = 0;
        int currentNumber = 1;

        // Compute Fibonacci numbers up to the desired position
        for (int i = 2; i <= position; i++) {
            int nextNumber = previousNumber + currentNumber;
            previousNumber = currentNumber;
            currentNumber = nextNumber;
        }

        return currentNumber;
    }

    /**
     * Matrix exponentiation solution for O(log n) time complexity
     *
     * This method uses the property that raising the matrix [[1,1],[1,0]] to the power of n
     * gives us the (n+1)th Fibonacci number at position [0][0].
     *
     * @param position The position in the Fibonacci sequence
     * @return The Fibonacci number at the given position
     */
    public int fibMatrixExponentiation(int position) {
        if (position <= 1) {
            return position;
        }

        int[][] matrix = {{1, 1}, {1, 0}};
        matrixPower(matrix, position - 1);

        return matrix[0][0];
    }

    /**
     * Helper method to raise a 2x2 matrix to a given power using exponentiation by squaring
     *
     * @param matrix The matrix to raise to a power
     * @param power The power to raise the matrix to
     */
    private void matrixPower(int[][] matrix, int power) {
        if (power <= 1) {
            return;
        }

        // Initialize result as identity matrix
        int[][] result = {{1, 0}, {0, 1}};

        // Use exponentiation by squaring for efficient power calculation
        while (power > 0) {
            // If power is odd, multiply result by matrix
            if (power % 2 == 1) {
                multiplyMatrices(result, matrix);
            }
            // Square the matrix
            multiplyMatrices(matrix, matrix);
            // Divide power by 2
            power /= 2;
        }

        // Copy result back to original matrix
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                matrix[i][j] = result[i][j];
            }
        }
    }

    /**
     * Helper method to multiply two 2x2 matrices
     *
     * @param a First matrix (result will be stored here)
     * @param b Second matrix
     */
    private void multiplyMatrices(int[][] a, int[][] b) {
        int x = a[0][0] * b[0][0] + a[0][1] * b[1][0];
        int y = a[0][0] * b[0][1] + a[0][1] * b[1][1];
        int z = a[1][0] * b[0][0] + a[1][1] * b[1][0];
        int w = a[1][0] * b[0][1] + a[1][1] * b[1][1];

        a[0][0] = x;
        a[0][1] = y;
        a[1][0] = z;
        a[1][1] = w;
    }
}
