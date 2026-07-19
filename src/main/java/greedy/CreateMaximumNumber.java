package greedy;

import java.util.Arrays;

/**
 * Problem: Create Maximum Number
 *
 * Given two arrays of digits and an integer k, pick exactly k digits total while
 * preserving the relative order of digits chosen from the same array. Return the
 * lexicographically largest possible k-digit sequence.
 *
 * Leetcode: https://leetcode.com/problems/create-maximum-number/ (Hard)
 * Rating:   acceptance 36.0% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Greedy | Monotonic stack | Try every split and lexicographic merge
 *
 * Example:
 *   Input:  nums1 = [3,4,6,5], nums2 = [9,1,2,5,8,3], k = 5
 *   Output: [9,8,6,5,3]
 *   Why:    choosing [6,5] from nums1 and [9,8,3] from nums2 lets the merge take
 *           the larger remaining suffix at every step.
 *
 * Follow-ups:
 *   1. Return the smallest number instead?
 *      Use the same split loop with an increasing stack and a smaller-suffix merge.
 *   2. What if there are more than two arrays?
 *      Distribute k across arrays, then repeatedly merge the best remaining suffix.
 *   3. What if k is close to m + n?
 *      Think in terms of dropping only m + n - k digits; the same stack rule decides safe drops.
 *   4. Stream the arrays instead of storing them?
 *      Exact suffix comparisons require buffering, so the merge is not purely streaming.
 *
 * Related: Remove K Digits (402), Maximum Swap (670), Largest Number (179).
 */
public class CreateMaximumNumber {

    public static void main(String[] args) {
        CreateMaximumNumber solver = new CreateMaximumNumber();
        int[][] nums1 = { {3, 4, 6, 5}, {6, 7}, {3, 9} };
        int[][] nums2 = { {9, 1, 2, 5, 8, 3}, {6, 0, 4}, {8, 9} };
        int[] kValues = {5, 5, 3};
        String[] expected = {"[9, 8, 6, 5, 3]", "[6, 7, 6, 0, 4]", "[9, 8, 9]"};

        for (int i = 0; i < nums1.length; i++) {
            int[] got = solver.maxNumber(nums1[i], nums2[i], kValues[i]);
            System.out.printf("nums1=%s nums2=%s k=%d -> %s  expected=%s%n",
                Arrays.toString(nums1[i]), Arrays.toString(nums2[i]), kValues[i],
                Arrays.toString(got), expected[i]);
        }
    }

    /**
     * Intuition: the hard part is that digits must keep their original order
     * inside each source array. First remove one uncertainty by trying every
     * valid split: take i digits from nums1 and k - i from nums2. For one array,
     * the best subsequence is built by a decreasing stack: drop a smaller kept
     * digit when a larger later digit can replace it and enough input remains.
     * Then merge the two subsequences by always taking the lexicographically
     * larger remaining suffix.
     *
     * Algorithm:
     *   1. Try every valid count i taken from nums1.
     *   2. Build the maximum length-i subsequence from nums1 and length-(k - i) subsequence from nums2.
     *   3. Merge those two subsequences by comparing their remaining suffixes.
     *   4. Keep the best merged candidate seen so far.
     *
     * Time:  O(k * (m + n)^2) - up to k splits are tried, and suffix comparisons can rescan candidates.
     * Space: O(k) - each candidate and the best answer store k digits.
     *
     * @param nums1 first array of digits
     * @param nums2 second array of digits
     * @param k length of the final sequence
     * @return lexicographically largest sequence of exactly k digits
     */
    public int[] maxNumber(int[] nums1, int[] nums2, int k) {
        int m = nums1.length, n = nums2.length;
        int[] result = new int[k];
        
        // Try all possible ways to split k between nums1 and nums2
        for (int i = Math.max(0, k - n); i <= Math.min(k, m); i++) {
            int[] candidate = merge(
                maxArray(nums1, i),
                maxArray(nums2, k - i)
            );
            if (greater(candidate, 0, result, 0)) {
                result = candidate;
            }
        }
        return result;
    }
    
    /** Builds the largest order-preserving subsequence of length k from one array. */
    private int[] maxArray(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[k];
        int len = 0; // current length of result
        
        for (int i = 0; i < n; i++) {
            // While we can remove elements from result to make a larger number
            while (len > 0 && len + n - i > k && result[len - 1] < nums[i]) {
                len--;
            }
            if (len < k) {
                result[len++] = nums[i];
            }
        }
        return result;
    }
    
    /** Merges two subsequences into the largest possible combined sequence. */
    private int[] merge(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int[] result = new int[m + n];
        int i = 0, j = 0, k = 0;
        
        while (i < m && j < n) {
            // Compare which array has the larger number
            result[k++] = greater(nums1, i, nums2, j) ? nums1[i++] : nums2[j++];
        }
        
        // Add remaining elements
        while (i < m) result[k++] = nums1[i++];
        while (j < n) result[k++] = nums2[j++];
        
        return result;
    }
    
    /** Returns whether nums1[i..] is lexicographically greater than nums2[j..]. */
    private boolean greater(int[] nums1, int i, int[] nums2, int j) {
        while (i < nums1.length && j < nums2.length && nums1[i] == nums2[j]) {
            i++;
            j++;
        }
        return j == nums2.length || (i < nums1.length && nums1[i] > nums2[j]);
    }
}
