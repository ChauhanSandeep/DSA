package Graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Fibonacci Sum
 * InterviewBit: https://www.interviewbit.com/problems/fibonacci-sum/
 *
 * Statement:
 * Given an integer `num`, find the minimum number of Fibonacci numbers whose sum is exactly `num`.
 * A Fibonacci number can be used multiple times.
 *
 * Example:
 * Input: 19
 * Output: 3
 * Explanation: 19 = 13 + 5 + 1
 *
 * Follow-up Questions (FAANG-style):
 * 1. Why does the greedy algorithm always work here?
 *    - Because Fibonacci numbers form a canonical coin system (Zeckendorf’s theorem guarantees a unique representation).
 * 2. Can this be extended to other sequences like Lucas numbers?
 *    - Not necessarily; greedy may fail if the sequence is not canonical.
 * 3. What if negative numbers were allowed?
 *    - Problem constraints break; additional handling or different strategy required.
 */
public class FibonacciSum {
    public static void main(String[] args) {
        FibonacciSum solver = new FibonacciSum();
        System.out.println(solver.fibSumGreedy(11)); // Output: 2 (8 + 3)
        System.out.println(solver.fibSumGreedy(17)); // Output: 3 (13 + 3 + 1)
        System.out.println(solver.fibSumGreedy(19)); // Output: 3 (13 + 5 + 1)
        System.out.println(solver.fibSumGreedy(1));  // Output: 1 (1)
        System.out.println(solver.fibSumGreedy(4));  // Output: 2 (3 + 1)
    }

    /**
     * Greedy Approach to find minimum number of Fibonacci numbers that sum to num.
     *
     * Approach:
     * - Generate all Fibonacci numbers up to `num`.
     * - Greedy strategy: repeatedly subtract the largest Fibonacci number less than or equal to `num`.
     * - Continue until `num` becomes 0, counting how many Fibonacci numbers were used.
     *
     * Time Complexity: O(log num) → Fibonacci grows exponentially, so list size is O(log num).
     * Space Complexity: O(log num) → For storing Fibonacci sequence.
     *
     * @param num target integer
     * @return minimum count of Fibonacci numbers summing to num
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
