package graphs;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Fibonacci Sum
 *
 * Given a positive integer, return the minimum number of Fibonacci numbers whose
 * sum is exactly that integer. The same Fibonacci number may be used more than
 * once.
 *
 * Source: InterviewBit - https://www.interviewbit.com/problems/fibonacci-sum/
 * Pattern:  Greedy | Fibonacci numbers | Canonical coin system
 *
 * Example:
 *   Input:  num = 19
 *   Output: 3
 *   Why:    19 can be written as 13 + 5 + 1, and no two Fibonacci numbers can
 *           make 19 exactly.
 *
 * Follow-ups:
 *   1. Why does greedy work for Fibonacci numbers?
 *      Zeckendorf-style structure ensures taking the largest possible Fibonacci number is safe.
 *   2. What if the coin sequence is arbitrary?
 *      Use coin-change DP because greedy can fail for non-canonical coin systems.
 *   3. Count how many minimum representations exist?
 *      Extend DP to track both best count and number of ways for each sum.
 */
public class FibonacciSum {


    public static void main(String[] args) {
        FibonacciSum solver = new FibonacciSum();
        int[] inputs = {1, 19, 4};
        int[] expected = {1, 3, 2};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.fibSumGreedy(inputs[i]);
            System.out.printf("num=%d  ->  %d  expected=%d%n", inputs[i], output, expected[i]);
        }
    }
    /**
     * Intuition: Fibonacci numbers behave like coin denominations where taking the
     * largest possible value never blocks an optimal answer. Zeckendorf's theorem
     * guarantees every positive number can be represented as a sum of nonconsecutive
     * Fibonacci numbers, so repeatedly subtracting the largest available Fibonacci
     * number minimizes the count.
     *
     * Algorithm:
     *   1. Generate all Fibonacci numbers up to num.
     *   2. Repeatedly choose the largest generated Fibonacci number not exceeding the remaining value.
     *   3. Subtract it and increment the answer count.
     *   4. Continue until the remaining value becomes zero.
     *
     * Time:  O(log n) - only logarithmically many Fibonacci numbers are generated and chosen.
     * Space: O(log n) - the list stores Fibonacci numbers up to num.
     *
     * @param num positive number to represent as a Fibonacci sum
     * @return minimum number of Fibonacci numbers whose sum is num
     */
    public int fibSumGreedy(int num) {
        if (num <= 0) return 0;

        // Step 1: Generate Fibonacci numbers up to num
        List<Integer> fibonacci = new ArrayList<>();
        fibonacci.add(1);
        fibonacci.add(1);

        while (fibonacci.get(fibonacci.size() - 1) + fibonacci.get(fibonacci.size() - 2) <= num) {
            int nextFib = fibonacci.get(fibonacci.size() - 1) + fibonacci.get(fibonacci.size() - 2);
            fibonacci.add(nextFib);
        }

        // Step 2: Use Greedy to subtract largest possible Fibonacci numbers
        int count = 0;
        int index = fibonacci.size() - 1;

        while (num > 0) {
            if (fibonacci.get(index) <= num) {
                num -= fibonacci.get(index);
                count++;
            }
            index--; // move to next largest Fibonacci number
        }

        return count;
    }

    /**
     * Dynamic Programming Approach (alternative to greedy).
     * Not optimal for large numbers but good for interview discussion.
     *
     * Time Complexity: O(num * F) where F = number of Fibonacci numbers <= num
     * Space Complexity: O(num)
     */
    public int fibSumDP(int num) {
        if (num <= 0) return 0;

        // Generate Fibonacci numbers up to num
        List<Integer> fibonacci = new ArrayList<>();
        fibonacci.add(1);
        fibonacci.add(1);
        while (fibonacci.get(fibonacci.size() - 1) + fibonacci.get(fibonacci.size() - 2) <= num) {
            int nextFib = fibonacci.get(fibonacci.size() - 1) + fibonacci.get(fibonacci.size() - 2);
            fibonacci.add(nextFib);
        }

        // Standard coin change DP approach
        int[] dp = new int[num + 1];
        for (int i = 1; i <= num; i++) {
            dp[i] = Integer.MAX_VALUE;
            for (int fib : fibonacci) {
                if (fib <= i && dp[i - fib] != Integer.MAX_VALUE) {
                    dp[i] = Math.min(dp[i], dp[i - fib] + 1);
                }
            }
        }
        return dp[num];
    }
}
