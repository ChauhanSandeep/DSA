package arrays;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Rotate Array
 *
 * Problem: Rotate array to the right by k steps in-place.
 *
 * Example: nums = [1,2,3,4,5,6,7], k = 3 -> Output: [5,6,7,1,2,3,4]
 * Rotate right by 3: last 3 elements move to front.
 *
 * LeetCode: https://leetcode.com/problems/rotate-array
 *
 * Follow-up Questions:
 * - How to rotate left instead of right? (Use k = n - k)
 * - What if k > n? (Use k % n to avoid unnecessary rotations)
 * - Can we rotate 2D arrays? (Apply rotation to each dimension)
 */
public class RotateArray {

    /**
     * Rotates array using three-step reversal approach.
     *
     * Algorithm:
     * 1. Reverse entire array: [1,2,3,4,5,6,7] -> [7,6,5,4,3,2,1]
     * 2. Reverse first k elements: [5,6,7,4,3,2,1]
     * 3. Reverse remaining n-k elements: [5,6,7,1,2,3,4]
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) - in-place rotation
     *
     * @param nums array to rotate
     * @param k number of positions to rotate right
     */
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        k = k % n; // Handle k > n

        if (k == 0) return; // No rotation needed

        // Step 1: Reverse entire array
        reverse(nums, 0, n - 1);

        // Step 2: Reverse first k elements
        reverse(nums, 0, k - 1);

        // Step 3: Reverse last n-k elements
        reverse(nums, k, n - 1);
    }

    // Helper method to reverse array segment
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
     * Cyclic replacement approach (alternative O(1) space solution)
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public void rotateCyclic(int[] nums, int k) {
        int n = nums.length;
        k = k % n;

        if (k == 0) return;

        int count = 0;

        for (int start = 0; count < n; start++) {
            int current = start;
            int prev = nums[start];

            do {
                int next = (current + k) % n;
                int temp = nums[next];
                nums[next] = prev;
                prev = temp;
                current = next;
                count++;
            } while (start != current);
        }
    }

    /**
     * Extra space approach (simpler but uses O(n) space)
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public void rotateExtraSpace(int[] nums, int k) {
        int n = nums.length;
        k = k % n;

        if (k == 0) return;

        int[] temp = new int[n];

        // Copy elements to their new positions
        for (int i = 0; i < n; i++) {
            temp[(i + k) % n] = nums[i];
        }

        // Copy back to original array
        System.arraycopy(temp, 0, nums, 0, n);
    }

    /**
     * Using ArrayList for educational purposes
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public void rotateList(int[] nums, int k) {
        int n = nums.length;
        k = k % n;

        if (k == 0) return;

        List<Integer> list = Arrays.stream(nums).boxed().collect(Collectors.toList());

        // Rotate using list operations
        Collections.rotate(list, k);

        // Copy back to array
        for (int i = 0; i < n; i++) {
            nums[i] = list.get(i);
        }
    }

    /**
     * Manual rotation using array slicing concept
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public void rotateSlicing(int[] nums, int k) {
        int n = nums.length;
        k = k % n;

        if (k == 0) return;

        // Split array into two parts and swap
        int[] rightPart = Arrays.copyOfRange(nums, n - k, n);
        int[] leftPart = Arrays.copyOfRange(nums, 0, n - k);

        // Combine: rightPart + leftPart
        System.arraycopy(rightPart, 0, nums, 0, k);
        System.arraycopy(leftPart, 0, nums, k, n - k);
    }

    /**
     * Bubble rotation (inefficient but educational)
     * Time Complexity: O(n*k), Space Complexity: O(1)
     */
    public void rotateBubble(int[] nums, int k) {
        int n = nums.length;
        k = k % n;

        // Rotate one position k times
        for (int i = 0; i < k; i++) {
            int last = nums[n - 1];

            // Shift all elements right by one
            for (int j = n - 1; j > 0; j--) {
                nums[j] = nums[j - 1];
            }

            nums[0] = last;
        }
    }

    /**
     * Recursive approach for educational purposes
     * Time Complexity: O(n), Space Complexity: O(log n) due to recursion
     */
    public void rotateRecursive(int[] nums, int k) {
        int n = nums.length;
        k = k % n;

        if (k == 0) return;

        rotateHelper(nums, 0, n - 1, k);
    }

    private void rotateHelper(int[] nums, int start, int end, int k) {
        int len = end - start + 1;
        if (len <= 1 || k == 0) return;

        k = k % len;
        if (k == 0) return;

        // Reverse entire range
        reverse(nums, start, end);
        // Reverse first k elements
        reverse(nums, start, start + k - 1);
        // Reverse remaining elements
        reverse(nums, start + k, end);
    }

    /**
     * Helper method to print array (for debugging)
     */
    private void printArray(int[] nums, String label) {
        System.out.println(label + ": " + Arrays.toString(nums));
    }
}
