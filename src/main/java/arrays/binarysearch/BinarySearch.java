package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: Binary Search
 *
 * Given a sorted array and a target, return the target index or -1 if it is absent. Each comparison uses sorted order to discard the half that cannot contain the answer.
 *
 * Leetcode: https://leetcode.com/problems/binary-search/ (Easy)
 * Rating:   acceptance 61.2% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Binary search | Sorted arrays | Inclusive bounds
 *
 * Example:
 *   Input:  arr = [1,2,3,4,5], target = 4
 *   Output: 3
 *   Why:    value 4 is stored at zero-based index 3.
 *
 * Follow-ups:
 *   1. Return the first or last duplicate? Keep searching left or right after a match.
 *   2. Search a rotated sorted array? First identify the sorted half, then discard the impossible half.
 *   3. Search an unbounded array? Expand the right bound exponentially, then binary search.
 *   4. Write it recursively? Carry left and right bounds through a helper.
 *
 * Related: Search Insert Position (35), Search in Rotated Sorted Array (33).
 */
public class BinarySearch {

    public static void main(String[] args) {
        int[][] inputs = { {1, 2, 3, 4, 5}, {1, 3, 5, 7}, {} };
        int[] targets = { 4, 2, 1 };
        int[] expected = { 3, -1, -1 };
        for (int i = 0; i < inputs.length; i++) {
            int iterative = binarySearch(inputs[i], targets[i]);
            int recursive = binarySearchRecursive(inputs[i], targets[i]);
            System.out.printf("arr=%s target=%d -> iterative=%d recursive=%d  expected=%d%n", Arrays.toString(inputs[i]), targets[i], iterative, recursive, expected[i]);
        }
    }




        /**
     * Intuition: Sorted order turns one comparison into proof that half the window is impossible. Inclusive left/right bounds keep all remaining candidates inside the window until it crosses.
     *
     * Algorithm:
     *   1. Start left at 0 and right at arr.length - 1.
     *   2. Compare target with arr[mid].
     *   3. Return mid on equality; otherwise move the appropriate bound past mid.
     *   4. Return -1 once left > right.
     *
     * Time:  O(log n) - each iteration halves the window.
     * Space: O(1) - only indexes are stored.
     *
     * @param arr sorted input array
     * @param target value to search for
     * @return index of target, or -1 if absent
     */
    public static int binarySearch(int[] arr, int target) {
        if (arr == null || arr.length == 0) return -1;

        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1; // Move to right half
            } else {
                right = mid - 1; // Move to left half
            }
        }

        return -1; // Not found
    }

    /**
     * Performs recursive binary search.
     *
     * Time Complexity: O(log N)
     * Space Complexity: O(log N) due to recursive call stack
     */
    public static int binarySearchRecursive(int[] arr, int target) {
        return searchHelper(arr, target, 0, arr.length - 1);
    }

        /** Recursively searches arr inside inclusive bounds. */
    private static int searchHelper(int[] arr, int target, int left, int right) {
        if (left > right) return -1;

        int mid = left + (right - left) / 2;

        if (arr[mid] == target) return mid;
        if (arr[mid] < target) return searchHelper(arr, target, mid + 1, right);
        return searchHelper(arr, target, left, mid - 1);
    }
}