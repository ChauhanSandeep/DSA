package stacksandqueues.monotonicstack;

import java.util.Stack;

/**
 * Problem: Daily Temperatures
 *
 * Given an array of integers temperatures represents the daily temperatures, return an array answer
 * such that answer[i] is the number of days you have to wait after the ith day to get a warmer temperature.
 * If there is no future day for which this is possible, keep answer[i] == 0.
 *
 * Example:
 * Input: temperatures = [73,74,75,71,69,72,76,73]
 * Output: [1,1,4,2,1,1,0,0]
 * Explanation: For the first day (73°), the next warmer day is tomorrow (74°), so wait 1 day.
 *
 * LeetCode: https://leetcode.com/problems/daily-temperatures
 *
 * Follow-up Questions:
 * 1. What if we need to find the actual temperature of the next warmer day instead of days to wait?
 *    Answer: Modify result to store temperatures[stack.peek()] instead of index differences.
 *
 * 2. How would you solve for "next cooler temperature" instead?
 *    Answer: Change comparison from temperatures[i] > temperatures[stack.peek()] to the opposite.
 *
 * 3. What if we need to find the next warmer temperature in a circular array?
 *    Answer: Process the array twice or use modular arithmetic to simulate circular behavior.
 *    Related: https://leetcode.com/problems/next-greater-element-ii/
 *
 * @author Sandeep
 */
public class DailyTemperatures {

    /**
     * Finds days to wait for warmer temperature using monotonic stack.
     *
     * Algorithm:
     * 1. Use stack to maintain indices of temperatures in decreasing order
     * 2. For each temperature, pop stack while current temperature is warmer
     * 3. For each popped index, calculate days difference and store in result
     * 4. Push current index to stack
     * 5. Remaining indices in stack have no warmer future temperature
     *
     * Time Complexity: O(n) where n is length of temperatures array
     * Space Complexity: O(n) for the stack in worst case (decreasing sequence)
     *
     * @param temperatures Array of daily temperatures
     * @return Array indicating days to wait for each temperature
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
}