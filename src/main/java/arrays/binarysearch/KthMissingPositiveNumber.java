package arrays.binarysearch;

/**
 * 1539. Kth Missing Positive Number
 *
 * Problem Statement:
 * Given a sorted array arr of positive integers (strictly increasing),
 * and an integer k, return the kth positive integer that is missing from this
 * array.
 *
 * LeetCode Link: https://leetcode.com/problems/kth-missing-positive-number/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class KthMissingPositiveNumber {

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
     * Binary search approach.
     *
     * Intuition:
     * For any index i, the count of missing positive numbers before arr[i] is:
     * missing(i) = arr[i] - (i + 1)
     * because in a perfect array without gaps, value at index i would be i + 1.
     * Any extra value beyond (i + 1) represents missing numbers.
     *
     * We need the first position where missing(i) >= k.
     * - If missing(mid) < k, kth missing is to the right.
     * - Else, kth missing is at mid or to the left.
     *
     * Steps:
     * 1. Binary search on index range [0, n - 1].
     * 2. Compute missingCount at mid using arr[mid] - (mid + 1).
     * 3. Move left/right bounds based on whether missingCount is < k.
     * 4. After loop, use right (last index with missingCount < k) to compute
     * answer.
     *
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
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
