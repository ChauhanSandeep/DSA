package arrays;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Problem: Longest Consecutive Sequence
 *
 * Given an unsorted integer array, return the length of the longest run of values
 * that appear consecutively, such as 1, 2, 3, 4. The values do not need to be
 * adjacent in the original array, and duplicates should not extend the run.
 *
 * Leetcode: https://leetcode.com/problems/longest-consecutive-sequence/ (Medium)
 * Rating:   acceptance 47.2% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Hash set | Sequence-start scan
 *
 * Example:
 *   Input:  nums = [100,4,200,1,3,2]
 *   Output: 4
 *   Why:    the values 1, 2, 3, and 4 are all present, forming the longest
 *           consecutive run even though they are scattered in the input.
 *
 * Follow-ups:
 *   1. Return the actual longest sequence?
 *      Remember the best start value and rebuild the run after the scan.
 *   2. Solve with O(1) extra space?
 *      Sort the array in place and scan, accepting O(n log n) time.
 *   3. Find the longest arithmetic run for a chosen difference d?
 *      Use the same set idea but test starts with value - d and extend by d.
 *
 * Related: Longest Increasing Subsequence (300), Longest Harmonious Subsequence (594).
 *
 */
public class LongestConsecutiveSequence {

    public static void main(String[] args) {
        LongestConsecutiveSequence solver = new LongestConsecutiveSequence();

        int[][] inputs = { {100, 4, 200, 1, 3, 2}, {0, 0}, {} };
        int[] expected = { 4, 1, 0 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.findLongestConsecutiveSequence(inputs[i]);
            System.out.printf("nums=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }


    /**
     * Intuition: a consecutive run should be counted exactly once, from its first
     * value. A hash set gives constant-time membership checks, so when num - 1 is
     * absent, num is the start of a run; then walk forward through num + 1, num + 2,
     * and so on to measure that run.
     *
     * Algorithm:
     *   1. Return 0 for null or empty input.
     *   2. Put all numbers into numberSet to deduplicate and support fast lookup.
     *   3. For each num that has no predecessor num - 1, count the run forward.
     *   4. Track and return the maximum run length.
     *
     * Time:  O(n) - each distinct value is advanced through at most once as part of a run.
     * Space: O(n) - the hash set stores the distinct input values.
     *
     * @param nums unsorted input values
     * @return length of the longest consecutive value sequence
     */
    public int findLongestConsecutiveSequence(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        Set<Integer> numberSet = Arrays.stream(nums)
            .boxed()
            .collect(Collectors.toSet());

        int maxSequenceLength = 0;

        for (int num : numberSet) {
            // Only start counting if this is the beginning of a sequence
            if (!numberSet.contains(num - 1)) {
                int currentNum = num;
                int currentLength = 1;

                // Extend sequence as far as possible
                while (numberSet.contains(currentNum + 1)) {
                    currentNum++;
                    currentLength++;
                }

                maxSequenceLength = Math.max(maxSequenceLength, currentLength);
            }
        }

        return maxSequenceLength;
    }

    /**
     * Alternative approach using sorting (O(n log n) time).
     * Steps:
     * 1. Sort the array.
     * 2. Iterate through sorted array, counting consecutive sequences.
     * 3. Handle duplicates by skipping them.
     * 4. Track and return the maximum sequence length.
     * 
     * Time Complexity: O(n log n) due to sorting
     * Space Complexity: O(1) if sorting in-place
     */
    public int longestConsecutiveSorting(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        java.util.Arrays.sort(nums);
        int maxLength = 1;
        int currentLength = 1;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) {
                // Duplicate: skip counting.
                continue;
            }
            if (nums[i] == nums[i - 1] + 1) {
                currentLength++;
            } else {
                currentLength = 1;
            }
            maxLength = Math.max(maxLength, currentLength);
        }

        return maxLength;
    }
}