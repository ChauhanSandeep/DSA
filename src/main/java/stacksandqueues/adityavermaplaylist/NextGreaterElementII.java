package stacksandqueues.adityavermaplaylist;

import java.util.*;

/**
 * Problem: Next Greater Element II
 *
 * Given a circular integer array, return the next greater value for every
 * position. Searching may wrap from the end of the array back to the start; if
 * no greater value exists, the answer for that position is -1.
 *
 * Leetcode: https://leetcode.com/problems/next-greater-element-ii/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Stack | Circular array | Monotonic decreasing stack
 *
 * Example:
 *   Input:  nums = [1,2,1]
 *   Output: [2,-1,2]
 *   Why:    the last 1 wraps around and finds 2, while 2 is the maximum value.
 *
 * Follow-ups:
 *   1. What if the array wraps k times?
 *      Iterate k * n positions with modulo indexing, but answers still settle on the first greater value.
 *   2. Return next greater indices instead of values?
 *      Store stack.peek() directly in result before converting to values if needed.
 *   3. Find next smaller in a circular array?
 *      Reverse the comparison and maintain candidates that are smaller than current.
 *   4. Can you avoid physically duplicating the array?
 *      Yes, the original code uses modulo indices to simulate the second pass.
 *
 * Related: Next Greater Element I (496), Daily Temperatures (739), Online Stock Span (901).
 */

public class NextGreaterElementII {

        /**
     * Intuition: in a circular array, each element can look through the suffix
     * after it and then the prefix before it. Scanning 2 * size positions from
     * right to left lets the stack hold exactly those future circular candidates;
     * after removing candidates no larger than nums[adjustedIndex], the stack top
     * is the next greater value.
     *
     * Algorithm:
     *   1. Fill result with -1 and scan i from 2 * size - 1 down to 0.
     *   2. Convert i to adjustedIndex with i % size.
     *   3. Pop indices whose values are <= nums[adjustedIndex].
     *   4. During the real pass (i < size), record nums[stack.peek()] when present.
     *   5. Push adjustedIndex as a candidate for earlier circular positions.
     *
     * Time:  O(n) - each circular index is pushed and popped a constant number of times.
     * Space: O(n) - result and stack store up to n indices.
     *
     * @param nums circular integer array
     * @return next greater value for each position, or -1 if none exists
     */

    public int[] nextGreaterElementsIndexBased(int[] nums) {
        int size = nums.length;
        int[] result = new int[size];
        Arrays.fill(result, -1);

        Deque<Integer> stack = new ArrayDeque<>(); // Stack to store indices in decreasing order of values

        // Double traversal with index-based stack
        for (int i = 2 * size - 1; i >= 0; i--) {
            int adjustedIndex = i % size;

            // Pop indices whose values are less than or equal to current element
            while (!stack.isEmpty() &&  nums[stack.peek()] <= nums[adjustedIndex]) {
                stack.pop();
            }

            // Update result with value at top index
            if (!stack.isEmpty() && i < size) {
                result[adjustedIndex] = nums[stack.peek()];
            }

            stack.push(adjustedIndex);
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


    public static void main(String[] args) {
        NextGreaterElementII solver = new NextGreaterElementII();
        int[][] inputs = { {1, 2, 1}, {1, 2, 3, 4, 3}, {5, 5, 5} };
        int[][] expected = { {2, -1, 2}, {2, 3, 4, -1, 4}, {-1, -1, -1} };

        for (int i = 0; i < inputs.length; i++) {
            int[] got = solver.nextGreaterElementsIndexBased(inputs[i]);
            System.out.printf("nums=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }
}
