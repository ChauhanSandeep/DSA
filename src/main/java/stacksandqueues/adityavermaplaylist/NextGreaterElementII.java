package stacksandqueues.adityavermaplaylist;

import java.util.*;

/**
 * Next Greater Element II
 *
 * Given a circular integer array nums, return the next greater number for every element
 * in nums. The next greater number of an element x is the first greater number to its
 * traversing order next in the array, which means you could search circularly to find
 * its next greater number. If it doesn't exist, return -1 for this element.
 *
 * In a circular array, the element after nums[nums.length - 1] is nums[0], allowing
 * wraparound searching for next greater elements.
 *
 * Example:
 * Input: nums = [1,2,1]
 * Output: [2,-1,2]
 * Explanation:
 * - For first 1: next greater is 2 (at index 1)
 * - For 2: no element greater than 2 exists, so -1
 * - For second 1: wrapping around, next greater is 2 (at index 1)
 *
 * Example:
 * Input: nums = [1,2,3,4,3]
 * Output: [2,3,4,-1,4]
 *
 * LeetCode: https://leetcode.com/problems/next-greater-element-ii/description/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. What if we need to find next smaller element instead of next greater?
 *    Answer: Use same monotonic stack approach but maintain increasing order instead of decreasing.
 * 2. How to handle k-circular arrays (wrapping k times instead of just once)?
 *    Answer: Modify loop to iterate k*n times with same modulo logic.
 * 3. What if we need both next greater and previous greater elements?
 *    Answer: Run algorithm twice - once left-to-right for next, once right-to-left for previous.
 * 4. How to optimize space when array is very large?
 *    Answer: Process in chunks if memory-constrained, or use index-based stack instead of value-based.
 *
 * Related Problems:
 * - LeetCode 496: Next Greater Element I
 * - LeetCode 739: Daily Temperatures (Similar monotonic stack pattern)
 * - LeetCode 84: Largest Rectangle in Histogram (Advanced monotonic stack)
 */
public class NextGreaterElementII {

    /**
     * Implementation using index-based stack for better memory usage.
     * Stores indices instead of values to reduce memory footprint when values are large.
     *
     * Algorithm: Monotonic Decreasing Stack + Double Traversal
     * Core insight: Use stack to maintain potential candidates in decreasing order.
     * Process array twice (2n iterations) to handle circular nature - elements at
     * beginning can be next greater for elements at end.
     *
     * Key mechanics:
     * 1. Traverse from right to left twice (total 2n-1 to 0 iterations)
     * 2. For each position, pop stack elements ≤ current element (they can't help future elements)
     * 3. After popping, stack top (if exists) is next greater element for current position
     * 4. Push current element as potential candidate for positions to its left
     * 5. Use modulo (i % n) to handle circular indexing
     *
     * Why right-to-left: When processing position i, stack contains all valid candidates
     * from positions to the right of i, maintaining decreasing order for efficient lookup.
     *
     * Time Complexity: O(n) - each element pushed/popped from stack at most once
     * Space Complexity: O(n) - stack can contain up to n elements
     *
     * @param nums circular integer array
     * @return array containing next greater element for each position, or -1 if none exists
     */
    public int[] nextGreaterElementsIndexBased(int[] nums) {
        int size = nums.length;
        int[] result = new int[size];
        Arrays.fill(result, -1);

        Deque<Integer> stack = new ArrayDeque<>(); // Stack to store indices in decreasing order of values

        // Double traversal with index-based stack
        for (int i = 2 * size - 1; i >= 0; i--) {
            int currentIndex = i % size;

            // Pop indices whose values are ≤ current element
            while (!stack.isEmpty() && nums[stack.peek()] <= nums[currentIndex]) {
                stack.pop();
            }

            // Update result with value at top index
            if (!stack.isEmpty() && i < size) {
                result[currentIndex] = nums[stack.peek()];
            }

            stack.push(currentIndex);
        }

        return result;
    }

    /**
     * Single-pass implementation using array doubling technique.
     * Creates virtual circular array by conceptually doubling the input array.
     *
     * @param nums circular integer array
     * @return array containing next greater element for each position
     */
    public int[] nextGreaterElementsDoubleArray(int[] nums) {
        int size = nums.length;
        int[] result = new int[size];
        Arrays.fill(result, -1);

        Deque<Integer> stack = new ArrayDeque<>();

        // Process doubled array: each element appears twice conceptually
        for (int i = 0; i < 2 * size; i++) {
            int value = nums[i % size];

            // While stack not empty and top element < current element
            while (!stack.isEmpty() && nums[stack.peek()] < value) {
                int index = stack.pop();
                result[index] = value; // Found next greater element
            }

            // Only push indices from first traversal to avoid duplicates
            if (i < size) {
                stack.push(i);
            }
        }

        return result;
    }

    /**
     * Space-optimized implementation for memory-constrained environments.
     * Uses single pass with careful index management to minimize space usage.
     *
     * @param nums circular integer array
     * @return array containing next greater element for each position
     */
    public int[] nextGreaterElementsSpaceOptimized(int[] nums) {
        int size = nums.length;
        int[] result = new int[size];
        Arrays.fill(result, -1);

        // Use ArrayList for dynamic resizing instead of fixed-size stack
        List<Integer> candidates = new ArrayList<>();

        for (int round = 0; round < 2; round++) {
            for (int i = size - 1; i >= 0; i--) {
                int currentValue = nums[i];

                // Remove candidates that can't help
                int writePos = 0;
                for (int readPos = 0; readPos < candidates.size(); readPos++) {
                    if (candidates.get(readPos) > currentValue) {
                        candidates.set(writePos++, candidates.get(readPos));
                    }
                }

                // Resize list to remove eliminated candidates
                while (candidates.size() > writePos) {
                    candidates.remove(candidates.size() - 1);
                }

                // Set result if candidate exists and this is first round
                if (!candidates.isEmpty() && round == 0) {
                    result[i] = candidates.get(0);
                }

                // Add current element as candidate
                candidates.add(0, currentValue);
            }
        }

        return result;
    }

}
