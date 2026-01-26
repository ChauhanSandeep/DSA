package arrays.binarysearch;

/**
 * Problem: Find Peak Element
 *
 * A peak element is an element that is strictly greater than its neighbors.
 * Given a 0-indexed integer array nums, find a peak element, and return its index.
 * If the array contains multiple peaks, return the index to any of the peaks.
 * You may imagine that nums[-1] = nums[n] = -∞. In other words, an element is always
 * considered to be strictly greater than a neighbor that is outside the array.
 * You must write an algorithm that runs in O(log n) time.
 *
 * Example:
 * Input: nums = [1,2,3,1]
 * Output: 2
 * Explanation: 3 is a peak element and your function should return the index number 2.
 *
 * LeetCode: https://leetcode.com/problems/find-peak-element
 *
 * Follow-up Questions:
 * 1. What if we need to find all peak elements?
 *    Answer: Cannot use binary search, need O(n) linear scan to find all peaks.
 *
 * 2. How would you find the global maximum instead of any peak?
 *    Answer: Still O(n) linear scan needed, binary search only works for local peaks.
 *
 * 3. What about finding peaks in a 2D matrix?
 *    Answer: Use divide and conquer, find peak in middle column, then recurse on side with larger neighbor.
 *    Related: https://leetcode.com/problems/find-a-peak-element-ii/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class FindPeakElement {

    /**
     * Finds a peak element using binary search approach.
     * Intuition:
     * - If the array is monotonically increasing, the peak is at the end.
     * - If it's monotonically decreasing, the peak is at the start.
     * - While moving towards increasing elements, if you find a dip then the peak is just before dip
     * - By always moving toward the higher neighbor, you're guaranteed to eventually land on a peak. Either
     * you find a dip or you reach the end of the array
     *
     * Algorithm:
     * 1. Use binary search with left and right pointers
     * 2. Compare middle element with its right neighbor
     * 3. If nums[mid] > nums[mid+1], peak exists in left half (including mid)
     * 4. Otherwise, peak exists in right half
     * 5. Continue until left == right
     *
     * Time Complexity: O(log n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param nums Input array of integers
     * @return Index of any peak element
     */
    public int findPeakElement(int[] nums) {
        if (nums == null || nums.length == 0) return -1;
        if (nums.length == 1) return 0;

        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            // Compare mid with right neighbor
            if (nums[mid] > nums[mid + 1]) {
                // Peak exists in left half (including mid)
                right = mid;
            } else {
                // Peak exists in right half
                left = mid + 1;
            }
        }

        return left; // left == right, both point to a peak
    }

    /**
     * Recursive binary search implementation.
     * Same logic but uses recursion instead of iteration.
     *
     * Time Complexity: O(log n)
     * Space Complexity: O(log n) for recursion stack
     */
    public int findPeakElementRecursive(int[] nums) {
        return findPeakHelper(nums, 0, nums.length - 1);
    }

    // Helper method for recursive approach
    private int findPeakHelper(int[] nums, int left, int right) {
        if (left == right) {
            return left;
        }

        int mid = left + (right - left) / 2;

        if (nums[mid] > nums[mid + 1]) {
            return findPeakHelper(nums, left, mid);
        } else {
            return findPeakHelper(nums, mid + 1, right);
        }
    }

    /**
     * Find all peak elements in the array (O(n) solution).
     * Cannot be optimized to O(log n) since we need to examine all elements.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(k) where k is number of peaks
     */
    public java.util.List<Integer> findAllPeaks(int[] nums) {
        java.util.List<Integer> peaks = new java.util.ArrayList<>();
        int size = nums.length;

        if (size == 0) return peaks;
        if (size == 1) {
            peaks.add(0);
            return peaks;
        }

        // Check first element
        if (nums[0] > nums[1]) {
            peaks.add(0);
        }

        // Check middle elements
        for (int i = 1; i < size - 1; i++) {
            if (nums[i] > nums[i-1] && nums[i] > nums[i+1]) {
                peaks.add(i);
            }
        }

        // Check last element
        if (nums[size-1] > nums[size-2]) {
            peaks.add(size-1);
        }

        return peaks;
    }
}