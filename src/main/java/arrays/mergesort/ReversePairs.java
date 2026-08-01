package arrays.mergesort;

import java.util.Arrays;

/**
 * Problem: Reverse Pairs
 *
 * Given an integer array, count pairs (i, j) such that i < j and nums[i] is
 * greater than twice nums[j]. The pair relation depends on both order and value,
 * so simply sorting the whole array would lose the original left-before-right rule.
 *
 * Leetcode: https://leetcode.com/problems/reverse-pairs/ (Hard)
 * Rating:   acceptance 34.8% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Arrays | Merge sort | Count cross pairs before merging
 *
 * Example:
 *   Input:  nums = [2,4,3,5,1]
 *   Output: 3
 *   Why:    the valid pairs are (4,1), (3,1), and (5,1) - each left value is
 *           greater than twice its right value.
 *
 * Follow-ups:
 *   1. What if the condition is nums[i] > k * nums[j]?
 *      Keep the same merge-sort count and replace 2 with k using long arithmetic.
 *   2. What if values are updated between queries?
 *      Use a segment tree or binary indexed tree with coordinate compression.
 *   3. What if you need to list the pairs, not just count them?
 *      The output can be quadratic, so emit pairs during the cross-count scan.
 *
 * Related: Count of Smaller Numbers After Self (315), Inversion Count.
 */
public class ReversePairs {

    public static void main(String[] args) {
        ReversePairs solver = new ReversePairs();
        int[][] inputs = { {1, 3, 2, 3, 1}, {2, 4, 3, 5, 1}, {1, 2, 3, 4} };
        int[] expected = { 2, 3, 0 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.reversePairs(inputs[i].clone());
            System.out.printf("nums=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Intuition: brute force checks every pair - O(n^2). A reverse pair only
     * cares about relative order, so values can be reordered *within* a half
     * without harm. Merge sort gives exactly that: split the array in two, and
     * every pair is either fully in the left half, fully in the right half, or
     * straddles the split. Recursion handles the two "fully inside" cases; the
     * straddling pairs are counted while both halves are sorted, right before
     * we merge them.
     *
     * Why it is correct: each pair (i, j) with i < j lands in exactly one of
     * those three buckets at one level of the recursion, so it is counted once
     * and never double-counted. Sorting a half is safe because it only shuffles
     * indices that already sit on the same side of the eventual partner.
     *
     * Time:  O(n log n) - merge sort's log n levels, each doing a linear
     *        cross-count plus a linear merge.
     * Space: O(n) for the reused merge buffer, plus O(log n) recursion stack.
     *
     * @param nums input array (may be null); its order is irrelevant to the caller
     * @return number of pairs (i, j) with i < j and nums[i] > 2 * nums[j]
     */
    public int reversePairs(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }

        int[] buffer = new int[nums.length];
        return (int) mergeSortAndCount(nums, 0, nums.length - 1, buffer);
    }

    /**
     * Intuition: standard merge sort, but the cross pairs are counted BEFORE the
     * merge. Ordering matters: once the two sorted halves interleave, the
     * "everything on the left comes before everything on the right" property
     * that makes the count valid is gone, so counting must happen first.
     *
     * Algorithm:
     *   1. Recurse into the left and right halves to count pairs inside each.
     *   2. Count straddling pairs while the halves are still separately sorted.
     *   3. Merge the halves and return the summed count.
     *
     * Time:  O(n log n) - the recurrence T(n) = 2T(n/2) + O(n).
     * Space: O(n) shared buffer, O(log n) recursion stack.
     *
     * @param nums   array being sorted in place
     * @param left   inclusive left bound of the current segment
     * @param right  inclusive right bound of the current segment
     * @param buffer scratch space reused across merges
     * @return reverse-pair count within nums[left..right]
     */
    private long mergeSortAndCount(int[] nums, int left, int right, int[] buffer) {
        if (left >= right) {
            return 0;
        }

        int mid = left + (right - left) / 2;

        long leftCount = mergeSortAndCount(nums, left, mid, buffer);
        long rightCount = mergeSortAndCount(nums, mid + 1, right, buffer);
        // count straddling pairs while the two halves are still separately sorted
        long crossCount = countCrossPairs(nums, left, mid, right);

        merge(nums, left, mid, right, buffer);
        return leftCount + rightCount + crossCount;
    }

    /**
     * Intuition: both halves are sorted ascending, so for a fixed left value the
     * qualifying right values form a prefix of the right half. As the left value
     * grows that prefix only extends, so the right pointer never rewinds - one
     * linear sweep replaces a nested scan.
     *
     * Time:  O(n) - each pointer walks its half at most once.
     * Space: O(1).
     *
     * @param nums  array with sorted halves nums[left..mid] and nums[mid+1..right]
     * @param left  inclusive start of the left half
     * @param mid   inclusive end of the left half
     * @param right inclusive end of the right half
     * @return number of straddling reverse pairs across the two halves
     */
    private long countCrossPairs(int[] nums, int left, int mid, int right) {
        long reversePairs = 0;
        int rightPointer = mid + 1;

        for (int leftPointer = left; leftPointer <= mid; leftPointer++) {
            // long cast guards against 2 * nums[rightPointer] overflowing int range
            while (rightPointer <= right
                && (long) nums[leftPointer] > 2L * nums[rightPointer]) {
                rightPointer++;
            }
            reversePairs += rightPointer - (mid + 1);
        }

        return reversePairs;
    }

    /**
     * Merges the sorted halves nums[left..mid] and nums[mid+1..right] via buffer.
     */
    private void merge(int[] nums, int left, int mid, int right, int[] buffer) {
        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (nums[i] <= nums[j]) {
                buffer[k++] = nums[i++];
            } else {
                buffer[k++] = nums[j++];
            }
        }

        while (i <= mid) {
            buffer[k++] = nums[i++];
        }

        while (j <= right) {
            buffer[k++] = nums[j++];
        }

        for (int index = left; index <= right; index++) {
            nums[index] = buffer[index];
        }
    }
}
