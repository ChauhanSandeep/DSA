package arrays;

/**
 * Minimum Swaps To Group All 1s Together
 *
 * Problem: Given binary array, find minimum swaps to group all 1s together.
 * A swap exchanges two elements in the array.
 *
 * Example: data = [1,0,1,0,1] -> Output: 1
 * Can group all 1s together: [1,1,1,0,0] with 1 swap.
 *
 * LeetCode: https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together
 *
 * Follow-up Questions:
 * - What if we want to group all 0s together? (Apply same logic with 0s and 1s swapped)
 * - How to handle case with no 1s? (Return 0, no swaps needed)
 * - What if swaps have different costs? (Use dynamic programming)
 */
public class MinimumSwapsToGroupAll1sTogether {

    /**
     * Finds minimum swaps to group all 1s together using sliding window.
     *
     * Algorithm:
     * 1. Count total number of 1s (this determines window size)
     * 2. Use sliding window of size equal to count of 1s
     * 3. For each window position, count 0s inside (these need to be swapped)
     * 4. Return minimum count of 0s across all window positions
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param data binary array
     * @return minimum swaps needed to group all 1s
     */
    public int minSwaps(int[] data) {
        int onesCount = 0;

        // Count total 1s in array
        for (int num : data) {
            if (num == 1) onesCount++;
        }

        // Edge case: no 1s or all are 1s
        if (onesCount <= 1) return 0;

        int n = data.length;
        int windowSize = onesCount;

        // Count 0s in first window
        int zerosInWindow = 0;
        for (int i = 0; i < windowSize; i++) {
            if (data[i] == 0) zerosInWindow++;
        }

        int minSwaps = zerosInWindow;

        // Slide the window and update count
        for (int i = windowSize; i < n; i++) {
            // Remove element going out of window
            if (data[i - windowSize] == 0) zerosInWindow--;

            // Add new element coming into window
            if (data[i] == 0) zerosInWindow++;

            minSwaps = Math.min(minSwaps, zerosInWindow);
        }

        return minSwaps;
    }

    /**
     * Alternative implementation with explicit window tracking
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int minSwapsExplicitWindow(int[] data) {
        int totalOnes = 0;
        for (int val : data) {
            totalOnes += val;
        }

        if (totalOnes <= 1) return 0;

        int n = data.length;
        int left = 0, right = 0;
        int onesInWindow = 0;
        int maxOnesInWindow = 0;

        // Expand window to desired size
        while (right < totalOnes) {
            onesInWindow += data[right];
            right++;
        }

        maxOnesInWindow = onesInWindow;

        // Slide window through rest of array
        while (right < n) {
            // Remove leftmost element
            onesInWindow -= data[left];
            left++;

            // Add rightmost element
            onesInWindow += data[right];
            right++;

            maxOnesInWindow = Math.max(maxOnesInWindow, onesInWindow);
        }

        // Minimum swaps = total ones - maximum ones we can keep in any window
        return totalOnes - maxOnesInWindow;
    }

    /**
     * Circular array approach (handles wraparound)
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int minSwapsCircular(int[] data) {
        int onesCount = 0;
        for (int num : data) {
            if (num == 1) onesCount++;
        }

        if (onesCount <= 1) return 0;

        int n = data.length;
        int maxOnesInWindow = 0;

        // Try all possible windows (including circular)
        for (int start = 0; start < n; start++) {
            int onesInCurrentWindow = 0;

            // Count 1s in current window
            for (int i = 0; i < onesCount; i++) {
                int index = (start + i) % n;
                if (data[index] == 1) {
                    onesInCurrentWindow++;
                }
            }

            maxOnesInWindow = Math.max(maxOnesInWindow, onesInCurrentWindow);
        }

        return onesCount - maxOnesInWindow;
    }

    /**
     * Prefix sum approach for different perspective
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int minSwapsPrefixSum(int[] data) {
        int n = data.length;
        int totalOnes = 0;

        for (int num : data) {
            totalOnes += num;
        }

        if (totalOnes <= 1) return 0;

        // Build prefix sum array
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + data[i];
        }

        int maxOnesInWindow = 0;
        int windowSize = totalOnes;

        // Check all windows using prefix sum
        for (int i = 0; i <= n - windowSize; i++) {
            int onesInWindow = prefixSum[i + windowSize] - prefixSum[i];
            maxOnesInWindow = Math.max(maxOnesInWindow, onesInWindow);
        }

        return totalOnes - maxOnesInWindow;
    }
}
