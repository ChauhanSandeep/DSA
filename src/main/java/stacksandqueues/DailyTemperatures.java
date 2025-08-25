package stacksandqueues;

import java.util.Stack;
import java.util.Arrays;

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
        
        int n = temperatures.length;
        int[] result = new int[n];
        Stack<Integer> stack = new Stack<>(); // Stack to store indices
        
        for (int i = 0; i < n; i++) {
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
        int n = temperatures.length;
        int[] result = new int[n];
        
        // Start from the second last element (last element always has result 0)
        for (int i = n - 2; i >= 0; i--) {
            int j = i + 1;
            
            // Find the next warmer temperature
            while (j < n && temperatures[j] <= temperatures[i]) {
                if (result[j] == 0) {
                    // No warmer temperature exists for j, so none exists for i either
                    j = n;
                } else {
                    // Jump to the next warmer temperature for j
                    j = j + result[j];
                }
            }
            
            if (j < n) {
                result[i] = j - i;
            }
        }
        
        return result;
    }
    
    /**
     * Brute force approach for comparison (O(n²) time complexity).
     * Useful for understanding the problem and small inputs.
     * 
     * Time Complexity: O(n²)
     * Space Complexity: O(1) excluding result
     */
    public int[] dailyTemperaturesBruteForce(int[] temperatures) {
        int n = temperatures.length;
        int[] result = new int[n];
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (temperatures[j] > temperatures[i]) {
                    result[i] = j - i;
                    break;
                }
            }
            // If no warmer temperature found, result[i] remains 0
        }
        
        return result;
    }
    
    /**
     * Generic next greater element function.
     * Can be used for various "next greater" problems.
     * 
     * @param arr Input array
     * @return Array where result[i] is index of next greater element, -1 if none
     */
    public int[] nextGreaterElements(int[] arr) {
        int n = arr.length;
        int[] result = new int[n];
        Arrays.fill(result, -1); // Initialize with -1 (no greater element found)
        
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && arr[i] > arr[stack.peek()]) {
                int prevIndex = stack.pop();
                result[prevIndex] = i; // Store index instead of difference
            }
            stack.push(i);
        }
        
        return result;
    }
    
    /**
     * Finds next warmer temperatures (values instead of days).
     * Returns the actual temperature values instead of waiting days.
     * 
     * @param temperatures Array of temperatures
     * @return Array of next warmer temperature values
     */
    public int[] nextWarmerTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] result = new int[n]; // 0 means no warmer temperature
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int prevIndex = stack.pop();
                result[prevIndex] = temperatures[i]; // Store temperature value
            }
            stack.push(i);
        }
        
        return result;
    }
    
    /**
     * Solves the problem for circular array (next warmer temperature wrapping around).
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int[] dailyTemperaturesCircular(int[] temperatures) {
        int n = temperatures.length;
        int[] result = new int[n];
        Stack<Integer> stack = new Stack<>();
        
        // Process array twice to handle circular nature
        for (int i = 0; i < 2 * n; i++) {
            int actualIndex = i % n;
            
            while (!stack.isEmpty() && 
                   temperatures[actualIndex] > temperatures[stack.peek()]) {
                int prevIndex = stack.pop();
                if (result[prevIndex] == 0) { // Only fill if not already filled
                    result[prevIndex] = (actualIndex + n - prevIndex) % n;
                    if (result[prevIndex] == 0) {
                        result[prevIndex] = n; // Full circle needed
                    }
                }
            }
            
            if (i < n) { // Only push indices from first pass
                stack.push(actualIndex);
            }
        }
        
        return result;
    }
    
    /**
     * Validation method to check if result is correct.
     * Useful for testing and debugging.
     * 
     * @param temperatures Original temperatures array
     * @param result Computed result array
     * @return true if result is valid
     */
    public boolean validateResult(int[] temperatures, int[] result) {
        if (temperatures.length != result.length) return false;
        
        for (int i = 0; i < temperatures.length; i++) {
            if (result[i] == 0) {
                // Check that no warmer temperature exists after i
                for (int j = i + 1; j < temperatures.length; j++) {
                    if (temperatures[j] > temperatures[i]) {
                        return false; // Found warmer temperature, result should not be 0
                    }
                }
            } else {
                // Check that temperatures[i + result[i]] > temperatures[i]
                int nextIndex = i + result[i];
                if (nextIndex >= temperatures.length || 
                    temperatures[nextIndex] <= temperatures[i]) {
                    return false;
                }
                
                // Check that no warmer temperature exists between i and nextIndex
                for (int j = i + 1; j < nextIndex; j++) {
                    if (temperatures[j] > temperatures[i]) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
}