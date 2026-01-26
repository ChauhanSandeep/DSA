package arrays.matrix;

/**
 * Problem: Rotate Array
 *
 * Given an array, rotate the array to the right by k steps, where k is non-negative.
 *
 * You must do this in-place with O(1) extra space.
 *
 * Example:
 * Input: nums = [1,2,3,4,5,6,7], k = 3
 * Output: [5,6,7,1,2,3,4]
 * Explanation:
 * - Rotate 1 step: [7,1,2,3,4,5,6]
 * - Rotate 2 steps: [6,7,1,2,3,4,5]
 * - Rotate 3 steps: [5,6,7,1,2,3,4]
 *
 * Input: nums = [-1,-100,3,99], k = 2
 * Output: [3,99,-1,-100]
 * Explanation:
 * - Rotate 1 step: [99,-1,-100,3]
 * - Rotate 2 steps: [3,99,-1,-100]
 *
 * LeetCode: https://leetcode.com/problems/rotate-array
 *
 * Follow-up Questions:
 * 1. Q: What if k is much larger than array length?
 *    A: Use k %= n to get effective rotation since rotating by array length gives same array.
 *
 * 2. Q: How would you rotate left instead of right?
 *    A: For left rotation by k, use right rotation by (n - k) steps.
 *
 * 3. Q: What if you could use extra space?
 *    A: Could create new array and copy elements directly to final positions: newArr[(i + k) % n] = arr[i].
 *
 * 4. Q: How would you handle very large arrays efficiently?
 *    A: Current O(1) space solution is already optimal. Could optimize cache performance with blocking.
 *
 * Related Problems:
 * - Rotate Image: https://leetcode.com/problems/rotate-image/
 * - Reverse Words in a String: https://leetcode.com/problems/reverse-words-in-a-string/
 * - Rotate List: https://leetcode.com/problems/rotate-list/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class RotateArray {

    /**
     * Rotates array using three-step reversal algorithm for optimal space efficiency.
     *
     * Algorithm:
     * 1. Handle edge cases and normalize k using modulo operation
     * 2. Reverse entire array: [1,2,3,4,5,6,7] → [7,6,5,4,3,2,1]
     * 3. Reverse first k elements: [7,6,5,4,3,2,1] → [5,6,7,4,3,2,1] (k=3)
     * 4. Reverse remaining n-k elements: [5,6,7,4,3,2,1] → [5,6,7,1,2,3,4]
     *
     * Time Complexity: O(n) where n is length of array
     * Space Complexity: O(1) using constant extra space
     *
     * @param nums array to rotate
     * @param k number of steps to rotate right
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
