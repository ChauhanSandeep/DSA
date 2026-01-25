package arrays.slidingwindow;

/**
 * Problem: Minimum Swaps to Group All 1's Together
 *
 * Given a binary array data, return the minimum number of swaps required to group all 1's
 * present in the array together in any place in the array. A swap means exchanging the
 * positions of two elements in the array.
 *
 * Example:
 * Input: data = [1,0,1,0,1]
 * Output: 1
 * Explanation:
 * There are 3 ways to group all 1's together:
 * - [1,1,1,0,0] using 1 swap
 * - [0,1,1,1,0] using 2 swaps
 * - [0,0,1,1,1] using 1 swap
 * The minimum is 1.
 *
 * Input: data = [0,0,0,1,0]
 * Output: 0
 * Explanation: Since there is only one 1, no swaps needed.
 *
 * LeetCode: https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together
 *
 * Follow-up Questions:
 * 1. Q: What if the array is circular (like problem 2134)?
 *    A: Extend the array by duplicating it and apply the same sliding window technique.
 *
 * 2. Q: What if we want to group all 0's together instead of 1's?
 *    A: Same approach but count 0's and find window with maximum 0's.
 *
 * 3. Q: How would you handle very large arrays with memory constraints?
 *    A: Current O(1) space solution is already optimal for memory usage.
 *
 * 4. Q: What if swaps have different costs based on distance?
 *    A: Would require dynamic programming approach to minimize total cost.
 *
 * Related Problems:
 * - Minimum Swaps to Group All 1's Together II: https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together-ii/
 * - Minimum Adjacent Swaps for K Consecutive Ones: https://leetcode.com/problems/minimum-adjacent-swaps-for-k-consecutive-ones/
 * - Sliding Window Maximum: https://leetcode.com/problems/sliding-window-maximum/
 * LeetCode Contest Rating: 1508
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

        int length = data.length;
        int windowSize = onesCount;

        // Count 0s in first window
        int zerosInWindow = 0;
        for (int i = 0; i < windowSize; i++) {
            if (data[i] == 0) zerosInWindow++;
        }

        int minSwaps = zerosInWindow; // these 0s need to be swapped

        // Slide the window using start and end indices
        for (int start = 1, end = windowSize; end < length; start++, end++) {
            if (data[start - 1] == 0) zerosInWindow--;
            if (data[end] == 0) zerosInWindow++;
            minSwaps = Math.min(minSwaps, zerosInWindow);
        }

        return minSwaps;
    }


}
