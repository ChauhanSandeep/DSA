package arrays.mergesort;

/**
 * Given an integer array nums, return the number of reverse pairs in the array.
 * A reverse pair is a pair (i, j) where 0 <= i < j < nums.length and nums[i] > 2 * nums[j].
 *
 * Example 1:
 * Input: nums = [1,3,2,3,1]
 * Output: 2
 * Explanation: The reverse pairs are:
 * (1,4) --> nums[1]=3 > 2*nums[4]=2
 * (3,4) --> nums[3]=3 > 2*nums[4]=2
 *
 * Example 2:
 * Input: nums = [2,4,3,5,1]
 * Output: 3
 *
 * LeetCode: https://leetcode.com/problems/reverse-pairs/
 *
 * Follow-up Questions:
 * 1. How would you handle very large arrays (e.g., 10^5 elements)?
 *    - The merge sort based solution has O(n log n) time complexity which is efficient for large inputs.
 * 2. What if we need to return the actual pairs instead of just the count?
 *    - We could modify the solution to collect and return the pairs instead of just counting them.
 * 3. How would you handle floating point numbers in the array?
 *    - The solution would work the same way as it's based on comparison operations.
 *
 * Related Problems:
 * - Count of Range Sum (https://leetcode.com/problems/count-of-range-sum/)
 * - Count of Smaller Numbers After Self (https://leetcode.com/problems/count-of-smaller-numbers-after-self/)
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class ReversePairs {
    /**
     * Merge sort based counting.
     * Intuition and Approach:
     *
     * Intuition:
     * - We need pairs (i, j) such that i < j and nums[i] > 2 * nums[j].
     * - Brute force checks all pairs, which is O(n^2). This becomes too slow for large inputs.
     * - The key idea is: if two halves are already sorted, we can count valid cross-half pairs
     *   in linear time using two pointers.
     *
     * Concept Building:
     * - Merge sort naturally divides the array into left and right halves.
     * - After recursively sorting both halves:
     *   1) Count reverse pairs where left index is in left half and right index is in right half.
     *   2) Merge the two sorted halves so parent level can do the same.
     * - This "count while merging" strategy avoids redundant comparisons and gives O(n log n).
     *
     * Approach Flow:
     * 1) Divide: split array until single element ranges.
     * 2) Conquer: solve left and right ranges recursively.
     * 3) Count cross pairs:
     *    - For each value in left half, move a right pointer in right half while
     *      nums[left] > 2 * nums[right].
     *    - Because both halves are sorted, right pointer never moves backward.
     *    - Number of valid j for current i is right - (mid + 1).
     * 4) Merge both halves into sorted order.
     *
     * Why this works:
     * - Every reverse pair belongs to exactly one merge boundary where the two indices first
     *   lie in different halves, so each pair is counted once.
     * - Sorted order guarantees monotonic pointer movement and linear counting per merge step.
     *
     * Overflow safety:
     * - We use long in comparison ((long) nums[left] > 2L * nums[right]) to avoid integer overflow.
     *
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     */
    public int reversePairs(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }

        int[] mergeBuffer = new int[nums.length];
        long totalReversePairs = mergeSortAndCount(nums, 0, nums.length - 1, mergeBuffer);
        return (int) totalReversePairs;
    }

    /**
     * Recursively sorts the array using merge sort and counts reverse pairs.
     */
    private long mergeSortAndCount(int[] nums, int leftIndex, int rightIndex, int[] mergeBuffer) {
        if (leftIndex >= rightIndex) {
            return 0;
        }

        int middleIndex = leftIndex + (rightIndex - leftIndex) / 2;

        // Sort left half and right half first.
        long leftHalfCount = mergeSortAndCount(nums, leftIndex, middleIndex, mergeBuffer);
        long rightHalfCount = mergeSortAndCount(nums, middleIndex + 1, rightIndex, mergeBuffer);

        // Count valid pairs across the two sorted halves.
        long crossHalfCount = countPairs(nums, leftIndex, middleIndex, rightIndex);

        // Merge the two sorted halves from leftIndex to rightIndex.
        mergeSortedHalves(nums, leftIndex, middleIndex, rightIndex, mergeBuffer);
        return leftHalfCount + rightHalfCount + crossHalfCount;
    }

    /**
     * Counts reverse pairs between the two sorted halves.
     */
    private long countPairs(int[] nums, int leftStart, int middle, int rightEnd) {
        long crossHalfReversePairs = 0;
        int rightPointer = middle + 1;

        for (int leftPointer = leftStart; leftPointer <= middle; leftPointer++) {
            while (rightPointer <= rightEnd && (long) nums[leftPointer] > 2L * nums[rightPointer]) {
                rightPointer++;
            }

            // All elements in right half before rightPointer form valid pairs.
            crossHalfReversePairs += rightPointer - (middle + 1);
        }
        return crossHalfReversePairs;
    }

    /**
     * Merges two sorted subarrays.
     * subarray from (leftStart to middle) and subarray from (middle + 1 to rightEnd) are sorted.
     * We merge them into a single sorted array.
     */
    private void mergeSortedHalves(int[] nums, int leftStart, int middle, int rightEnd, int[] mergeBuffer) {
        int leftPointer = leftStart;
        int rightPointer = middle + 1;
        int mergedIndex = leftStart;

        // Merge into auxiliary array in sorted order.
        while (leftPointer <= middle && rightPointer <= rightEnd) {
            if (nums[leftPointer] <= nums[rightPointer]) {
                mergeBuffer[mergedIndex++] = nums[leftPointer++];
            } else {
                mergeBuffer[mergedIndex++] = nums[rightPointer++];
            }
        }

        while (leftPointer <= middle) {
            mergeBuffer[mergedIndex++] = nums[leftPointer++];
        }

        while (rightPointer <= rightEnd) {
            mergeBuffer[mergedIndex++] = nums[rightPointer++];
        }

        // Copy merged segment back to original array.
        for (int index = leftStart; index <= rightEnd; index++) {
            nums[index] = mergeBuffer[index];
        }
    }

    /**
     * Brute force solution for verification.
     *
     * Time Complexity: O(n^2)
     * Space Complexity: O(1)
     */
    public int reversePairsBruteForce(int[] nums) {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if ((long) nums[i] > 2L * nums[j]) {
                    count++;
                }
            }
        }
        return count;
    }
}
