package stacksandqueues.monotonicstack;

import java.util.Arrays;
import java.util.Stack;

/**
 * Problem: Daily Temperatures
 *
 * Given daily temperatures, return how many days each day must wait until a
 * warmer temperature appears. If no warmer future day exists, keep 0 at that
 * position.
 *
 * Leetcode: https://leetcode.com/problems/daily-temperatures/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Stack | Monotonic decreasing stack | Next greater index
 *
 * Example:
 *   Input:  temperatures = [73,74,75,71,69,72,76,73]
 *   Output: [1,1,4,2,1,1,0,0]
 *   Why:    day 2 with 75 waits until day 6 with 76, so its answer is 4.
 *
 * Follow-ups:
 *   1. Return the warmer temperature instead of the wait?
 *      Store temperatures[i] for each popped index instead of i - prevIndex.
 *   2. Find next cooler days?
 *      Reverse the comparison so cooler current temperatures resolve waiting indices.
 *   3. Make the array circular?
 *      Traverse 2 * n positions and push indices only during the first pass.
 *   4. Answer online as temperatures stream in?
 *      Keep unresolved indices on the same stack and emit answers when future days arrive.
 *
 * Related: Next Greater Element I (496), Next Greater Element II (503), Online Stock Span (901).
 */

public class DailyTemperatures {

        /**
     * Intuition: each unresolved day waits on the stack for the first warmer
     * future day. When temperatures[i] is warmer than the day at stack.peek(),
     * it is exactly that first warmer day because all days between them were not
     * warm enough to pop it earlier.
     *
     * Algorithm:
     *   1. Return an empty array for null or empty input.
     *   2. Scan temperatures from left to right.
     *   3. While current temperature is warmer than stack top's temperature, pop and record the day difference.
     *   4. Push the current index as unresolved.
     *   5. Leave remaining unresolved indices as 0.
     *
     * Time:  O(n) - each index is pushed once and popped at most once.
     * Space: O(n) - result and stack can each store n entries.
     *
     * @param temperatures daily temperatures
     * @return days to wait for a warmer temperature at each index
     */

    public int[] dailyTemperatures(int[] temperatures) {
        if (temperatures == null || temperatures.length == 0) {
            return new int[0];
        }

        int size = temperatures.length;
        int[] result = new int[size];
        Stack<Integer> stack = new Stack<>(); // Stack to store indices

        for (int i = 0; i < size; i++) {
            // While stack not empty and current temperature is warmer than stack top
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int prevIndex = stack.pop();
                result[prevIndex] = i - prevIndex; // Days to wait
            }

            // Push current index to stack
            stack.push(i);
        }

        // Remaining indices in stack have no warmer future temperature (result[i] = 0 by default)
        return result;
    }

    /**
     * Optimized approach working backwards from the end.
     * Uses the previously computed results to skip unnecessary iterations.
     *
     * Time Complexity: O(n) amortized
     * Space Complexity: O(1) excluding result array
     */
    public int[] dailyTemperaturesBackward(int[] temperatures) {
        int size = temperatures.length;
        int[] result = new int[size];

        // Start from the second last element (last element always has result 0)
        for (int i = size - 2; i >= 0; i--) {
            int j = i + 1;

            // Find the next warmer temperature
            while (j < size && temperatures[j] <= temperatures[i]) {
                if (result[j] == 0) {
                    // No warmer temperature exists for j, so none exists for i either
                    j = size;
                } else {
                    // Jump to the next warmer temperature for j
                    j = j + result[j];
                }
            }

            if (j < size) {
                result[i] = j - i;
            }
        }

        return result;
    }

    /**
     * Solves the problem for circular array (next warmer temperature wrapping around).
     * Uses next greater element approch with double traversal.
     *
     * Algorithm:
     * 1. Traverse the array twice to simulate circular behavior.
     * 2. Use a stack to keep track of indices of temperatures.
     * 3. For each temperature, pop from stack while current temperature is warmer.
     * 4. Calculate days difference using modular arithmetic to handle wrapping.
     * 5. Push current index to stack only during the first pass.
     * 6. Remaining indices in stack have no warmer future temperature.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int[] dailyTemperaturesCircular(int[] temperatures) {
        int size = temperatures.length;
        int[] result = new int[size];
        Stack<Integer> stack = new Stack<>();

        // Process array twice to handle circular nature
        for (int i = 0; i < 2 * size; i++) {
            int actualIndex = i % size;

            while (!stack.isEmpty() && temperatures[actualIndex] > temperatures[stack.peek()]) {
                int prevIndex = stack.pop();
                if (result[prevIndex] == 0) { // Only fill if not already filled

                    // (actualIndex - prevIndex) % size can result in negative value
                    // So, (actualIndex + size - prevIndex) % size is used to avoid overflow
                    result[prevIndex] = (actualIndex + size - prevIndex) % size;

                    if (result[prevIndex] == 0) {
                        result[prevIndex] = size; // Full circle needed
                    }
                }
            }

            if (i < size) { // Only push indices from first pass
                stack.push(actualIndex);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        DailyTemperatures solver = new DailyTemperatures();
        int[][] inputs = { {73, 74, 75, 71, 69, 72, 76, 73}, {30, 40, 50, 60}, {} };
        int[][] expected = { {1, 1, 4, 2, 1, 1, 0, 0}, {1, 1, 1, 0}, {} };

        for (int i = 0; i < inputs.length; i++) {
            int[] got = solver.dailyTemperatures(inputs[i]);
            System.out.printf("temperatures=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }
}