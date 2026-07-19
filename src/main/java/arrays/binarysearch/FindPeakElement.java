package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: Find Peak Element
 *
 * A peak is greater than its existing neighbors, with virtual negative infinity outside the array. Return any peak index in logarithmic time.
 *
 * Leetcode: https://leetcode.com/problems/find-peak-element/ (Medium)
 * Rating:   acceptance 47.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Binary search | Slope direction | Local optimum
 *
 * Example:
 *   Input:  nums = [1,2,3,1]
 *   Output: 2
 *   Why:    nums[2] = 3 is greater than both neighbors.
 *
 * Follow-ups:
 *   1. Return all peaks? Scan linearly because every index may matter.
 *   2. Find a 2D peak? Binary search rows or columns and move toward a larger neighbor.
 *   3. Allow equal adjacent values? Define plateau handling explicitly.
 *   4. Find the global maximum? Use a linear scan unless extra structure exists.
 *
 * Related: Find a Peak Element II (1901).
 */
public class FindPeakElement {

    public static void main(String[] args) {
        FindPeakElement solver = new FindPeakElement();
        int[][] inputs = { {1,2,3,1}, {1}, {1,2,1,3,5,6,4} };
        int[] expected = { 2, 0, 5 };
        for (int i = 0; i < inputs.length; i++) {
            int got = solver.findPeakElement(inputs[i]);
            System.out.printf("nums=%s -> %d  expected=%d%n", Arrays.toString(inputs[i]), got, expected[i]);
        }
    }


        /**
     * Intuition: A rising slope to the right must eventually reach a peak; a falling slope means a peak exists at mid or left. Keep the side guaranteed to contain a peak.
     *
     * Algorithm:
     *   1. Handle null, empty, and single-element inputs as restored.
     *   2. Binary search while left < right.
     *   3. Compare nums[mid] with nums[mid + 1].
     *   4. Move toward the side that must contain a peak and return left.
     *
     * Time:  O(log n) - each comparison discards half.
     * Space: O(1) - only indexes are stored.
     *
     * @param nums input array
     * @return index of any peak, or -1 for empty input
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

    /** Recursively searches a peak inside inclusive bounds. */
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