package arrays.matrix;

import java.util.Arrays;
/**
 * Problem: Rotate Array
 *
 * Given an integer array, rotate it to the right by k steps in place. A value
 * leaving the end wraps around to the front, and k may be larger than the array
 * length.
 *
 * Leetcode: https://leetcode.com/problems/rotate-array/ (Medium)
 * Rating:   no contest rating (pre-contest problem)
 * Pattern:  Array | Reversal | Cyclic replacement
 *
 * Example:
 *   Input:  nums = [1,2,3,4,5,6,7], k = 3
 *   Output: [5,6,7,1,2,3,4]
 *   Why:    the last three values wrap to the front in their original order.
 *
 * Follow-ups:
 *   1. Rotate left by k?
 *      Rotate right by n - (k % n), or mirror the reversal boundaries.
 *   2. Prove O(1) extra space?
 *      Use the three-reversal method so all movement happens inside nums.
 *   3. Rotate a linked list?
 *      Find length, connect tail to head, then break at the new tail.
 *
 * Related: Rotate Image (48), Rotate List (61), Reverse Words in a String (151).
 */
public class RotateArray {

    public static void main(String[] args) {
        RotateArray solver = new RotateArray();

        int[][] inputs = { {1, 2, 3, 4, 5, 6, 7}, {-1, -100, 3, 99}, {1} };
        int[] steps = { 3, 2, 10 };
        int[][] expected = { {5, 6, 7, 1, 2, 3, 4}, {3, 99, -1, -100}, {1} };

        for (int i = 0; i < inputs.length; i++) {
            int[] input = inputs[i].clone();
            solver.rotate(input, steps[i]);
            System.out.printf("nums=%s k=%d -> output=%s  expected=%s%n",
                Arrays.toString(inputs[i]), steps[i], Arrays.toString(input), Arrays.toString(expected[i]));
        }
    }

/**
 * Intuition: a right rotation keeps the final k elements together, but moves
 * them before the prefix. Reversing the whole array brings those values to the
 * front, and reversing each side restores the internal order of both groups.
 *
 * Algorithm:
 *   1. Return for null or length-one input, then reduce k modulo length.
 *   2. Reverse the entire array.
 *   3. Reverse the first k positions.
 *   4. Reverse the remaining suffix.
 *
 * Time:  O(n) - each reversal touches a disjoint or full linear slice.
 * Space: O(1) - the array is modified in place.
 *
 * @param nums array to rotate in place
 * @param k number of right-rotation steps
 */
    public void rotate(int[] nums, int k) {
        if (nums == null || nums.length <= 1) {
            return;
        }

        int length = nums.length;

        // Normalize k to handle cases where k > length
        k = k % length;

        // If k is 0 after modulo, no rotation needed
        if (k == 0) {
            return;
        }

        // Step 1: Reverse entire array
        reverse(nums, 0, length - 1);

        // Step 2: Reverse first k elements
        reverse(nums, 0, k - 1);

        // Step 3: Reverse remaining length-k elements
        reverse(nums, k, length - 1);
    }

    // Helper method to reverse elements between start and end indices (inclusive)
    private void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }

    /**
     * Alternative approach using cyclic replacements for educational purposes.
     * More complex but demonstrates understanding of array indexing patterns.
     *
     * Algorithm:
     * 1. Start from index 0 and place element at its final position
     * 2. Take displaced element and place it at its final position
     * 3. Continue until cycle completes (returns to starting index)
     * 4. Handle multiple cycles by starting from next unvisited index
     * 5. Use GCD to determine number of cycles needed
     *
     * Time Complexity: O(n) where n is length of array
     * Space Complexity: O(1) using constant extra space
     *
     * @param nums array to rotate
     * @param k number of steps to rotate right
     */
    public void rotateCyclicReplacements(int[] nums, int k) {
        if (nums == null || nums.length <= 1) {
            return;
        }

        int length = nums.length;
        k = k % length;

        if (k == 0) {
            return;
        }

        int jumpsCompleted = 0;
        int startIndex = 0;

        // Continue until all elements are moved
        while (jumpsCompleted < length) {
            int currentIndex = startIndex;
            int currentValue = nums[startIndex];

            // Complete one cycle
          do {
            // Calculate next index using modulo for wrap-around
            int nextIndex = (currentIndex + k) % length;
            int nextValue = nums[nextIndex];

            // Place current value in its correct position
            nums[nextIndex] = currentValue;

            // Move to next position
            currentValue = nextValue;
            currentIndex = nextIndex;

            // Increment count of moved elements
            jumpsCompleted++;

            // If we have returned to the start of this cycle, break
          } while (currentIndex != startIndex);

            // Move to next starting point for next cycle
            startIndex++;
        }
    }

    /**
     * Brute force approach using extra space for comparison and understanding.
     * Not optimal for space but easy to understand and implement.
     *
     * Algorithm:
     * 1. Create temporary array to store rotated elements
     * 2. Calculate final position of each element: (i + k) % n
     * 3. Copy elements from original to final positions in temp array
     * 4. Copy temp array back to original array
     *
     * Time Complexity: O(n) where n is length of array
     * Space Complexity: O(n) for temporary array
     *
     * @param nums array to rotate
     * @param k number of steps to rotate right
     */
    public void rotateExtraSpace(int[] nums, int k) {
        if (nums == null || nums.length <= 1) {
            return;
        }

        int length = nums.length;
        k = k % length;

        if (k == 0) {
            return;
        }

        // Create temporary array to store rotated result
        int[] resultArray = new int[length];

        // Place each element at its final position
        for (int i = 0; i < length; i++) {
            int finalPosition = (i + k) % length;
            resultArray[finalPosition] = nums[i];
        }

        // Copy back to original array
        System.arraycopy(resultArray, 0, nums, 0, length);
    }
}
