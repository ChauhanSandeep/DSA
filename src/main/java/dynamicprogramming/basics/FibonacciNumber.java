package dynamicprogramming.basics;

/**
 * Problem: Fibonacci Number (LeetCode #509)
 *
 * Problem Statement:
 * The Fibonacci numbers, commonly denoted F(n) form a sequence, called the Fibonacci sequence,
 * such that each number is the sum of the two preceding ones, starting from 0 and 1.
 *
 * Example 1:
 * Input: n = 2
 * Output: 1
 * Explanation: F(2) = F(1) + F(0) = 1 + 0 = 1
 *
 * Example 2:
 * Input: n = 4
 * Output: 3
 * Explanation: F(4) = F(3) + F(2) = 2 + 1 = 3
 *
 * Approach:
 * We can solve this problem using multiple approaches:
 * 1. Iterative approach (O(n) time, O(1) space)
 * 2. Matrix exponentiation (O(log n) time, O(1) space)
 * 3. Recursive with memoization (O(n) time, O(n) space)
 *
 * The iterative approach is the most efficient in terms of both time and space for this problem.
 *
 * Time Complexity: O(n) for iterative approach, O(log n) for matrix exponentiation
 * Space Complexity: O(1) for both approaches
 *
 * Follow-up Questions:
 * 1. Can you solve it using matrix exponentiation for O(log n) time?
 *    Answer: Yes, by raising the matrix [[1,1],[1,0]] to the power of n, we can get F(n) in O(log n) time.
 *
 * 2. What if we want to find the last k digits of the nth Fibonacci number?
 *    Answer: We can use the iterative approach with modulo 10^k at each step to prevent integer overflow
 *    and get the last k digits efficiently.
 *
 * 3. Can you find the nth Fibonacci number modulo m efficiently?
 *    Answer: Yes, we can use the Pisano period property to find F(n) mod m in O(m^2) time, which is
 *    efficient when m is not too large.
 *
 * LeetCode: https://leetcode.com/problems/fibonacci-number/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class FibonacciNumber {

    /**
     * Iterative solution to find the nth Fibonacci number
     *
     * @param position The position in the Fibonacci sequence (0-based)
     * @return The Fibonacci number at the given position
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
