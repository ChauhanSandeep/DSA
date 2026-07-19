package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: Kth Missing Positive Number
 *
 * Given a strictly increasing positive array, return the k-th positive integer missing from it. The binary-search method uses the missing count before each index.
 *
 * Leetcode: https://leetcode.com/problems/kth-missing-positive-number/ (Easy)
 * Rating:   zerotrac 1295 (Q1, biweekly-32)
 * Pattern:  Binary search | Missing count formula | Lower bound
 *
 * Example:
 *   Input:  arr = [2,3,4,7,11], k = 5
 *   Output: 9
 *   Why:    the missing positives are [1,5,6,8,9,...].
 *
 * Follow-ups:
 *   1. Unsorted input? Sort first or scan with a set.
 *   2. Duplicates allowed? Deduplicate or adjust the count formula.
 *   3. Many k queries? Precompute missing counts and binary search them.
 *   4. Restrict to [L, R]? Shift the formula by L and cap at R.
 *
 * Related: Missing Number (268), First Missing Positive (41).
 */
public class KthMissingPositiveNumber {

    public static void main(String[] args) {
        KthMissingPositiveNumber solver = new KthMissingPositiveNumber();
        int[][] inputs = { {2,3,4,7,11}, {1,2,3,4}, {5,6,7} };
        int[] kth = { 5, 2, 1 };
        int[] expected = { 9, 6, 1 };
        for (int i = 0; i < inputs.length; i++) {
            int got = solver.findKthPositiveBinarySearch(inputs[i], kth[i]);
            System.out.printf("arr=%s k=%d -> %d  expected=%d%n", Arrays.toString(inputs[i]), kth[i], got, expected[i]);
        }
    }


    /**
     * Linear scan approach.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public int findKthPositiveLinear(int[] arr, int k) {
        int current = 1;
        int index = 0;

        while (k > 0) {
            if (index < arr.length && arr[index] == current) {
                index++;
            } else {
                k--;
                if (k == 0) {
                    return current;
                }
            }
            current++;
        }

        return current - 1;
    }

        /**
     * Intuition: At index i, arr[i] - (i + 1) counts positives missing before arr[i]. Binary search the last index whose missing count is still below k.
     *
     * Algorithm:
     *   1. Search the index range with inclusive bounds.
     *   2. Compute missingCount at mid.
     *   3. Move left rightward while missingCount < k; otherwise move right leftward.
     *   4. Return right + k + 1.
     *
     * Time:  O(log n) - the missing-count predicate is searched.
     * Space: O(1) - only indexes are stored.
     *
     * @param arr strictly increasing positive integers
     * @param k missing positive position
     * @return k-th missing positive
     */
    public int findKthPositiveBinarySearch(int[] arr, int k) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Missing positives before arr[mid].
            int missingCount = arr[mid] - (mid + 1);

            if (missingCount < k) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        /*
         * After loop:
         * - right = last index where missingCount(arr[right]) < k
         * - left = first index where missingCount(arr[left]) >= k
         * 
         * Numbers present at right: arr[right]
         * Missing count before it: arr[right] - right - 1
         * We need: k total missing
         * 
         * We still need: k - (arr[right] - right - 1) more missing numbers
         *
         * So the kth missing is:
         * arr[right] + (k - (arr[right] - right - 1))
         * = arr[right] + k - arr[right] + right + 1
         * = right + k + 1
         */
        return right + k + 1;
    }
}
