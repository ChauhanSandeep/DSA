package Graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Fibonacci Sum - Greedy Algorithm
 *
 * Problem:
 * Given an integer `num`, find the minimum number of Fibonacci numbers
 * whose sum is exactly `num`. A Fibonacci number can be used multiple times.
 *
 * Approach:
 * 1. **Generate Fibonacci numbers** up to `num`.
 * 2. **Greedy selection**: Pick the largest Fibonacci number <= `num`.
 * 3. **Subtract & repeat** until `num` reaches 0.
 *
 * Time Complexity: **O(log num)** (Fibonacci growth is exponential)
 * Space Complexity: **O(log num)** (Fibonacci list storage)
 */
public class FibonacciSum {
    public static void main(String[] args) {
        FibonacciSum solver = new FibonacciSum();
        System.out.println(solver.fibsum(11)); // Output: 2 (8 + 3)
        System.out.println(solver.fibsum(17)); // Output: 3 (13 + 3 + 1)
        System.out.println(solver.fibsum(19)); // Output: 3 (13 + 5 + 1)
        System.out.println(solver.fibsum(1));  // Output: 1 (1)
        System.out.println(solver.fibsum(4));  // Output: 2 (3 + 1)
    }

    /**
     * Finds the minimum number of Fibonacci numbers that sum to `num`.
     *
     * @param num The target sum.
     * @return Minimum count of Fibonacci numbers needed to reach `num`.
     */
    public int fibsum(int num) {
        if (num == 1) return 1; // Special case for num = 1

        // Step 1: Generate Fibonacci numbers up to `num`
        List<Integer> fibonacci = new ArrayList<>();
        fibonacci.add(1);
        fibonacci.add(1);

        while (fibonacci.get(fibonacci.size() - 1) + fibonacci.get(fibonacci.size() - 2) <= num) {
            fibonacci.add(fibonacci.get(fibonacci.size() - 1) + fibonacci.get(fibonacci.size() - 2));
        }

        // Step 2: Find minimum Fibonacci numbers using a greedy approach
        int count = 0;
        int index = fibonacci.size() - 1;

        while (num > 0) {
            if (fibonacci.get(index) <= num) {
                num -= fibonacci.get(index);
                count++;
            }
            index--; // Move to the next largest Fibonacci number
        }

        return count;
    }
}
