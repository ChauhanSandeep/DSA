package Array;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Given an unsorted array of integers, return the length of the longest consecutive sequence.
 * Example
 * Input: nums = [100,4,200,1,3,2]
 * Output: 4
 * Explanation: The longest consecutive elements sequence is [1, 2, 3, 4]. Therefore its length is 4.
 *
 * Approach:
 * - Convert the array into a HashSet for O(1) lookups.
 * - Iterate through the set and only start counting if the current number is the **start** of a sequence.
 * - Expand the sequence by checking consecutive numbers.
 * - Track and update the **maximum sequence length**.
 *
 * Time Complexity: O(n) → Each number is processed once.
 * Space Complexity: O(n) → Extra space used for the HashSet.
 *
 * LeetCode: https://leetcode.com/problems/longest-consecutive-sequence/
 */
public class LongestConsecutiveSequence {
    public static void main(String[] args) {
        int[] nums = {100, 4, 200, 1, 3, 2};
        int result = new LongestConsecutiveSequence().findLongestConsecutiveSequence(nums);
        System.out.println("Longest Consecutive Sequence Length: " + result);
    }

    public int findLongestConsecutiveSequence(int[] nums) {
        // Edge case: If array is empty, return 0
        if (nums == null || nums.length == 0) return 0;

        // Convert array to a HashSet for O(1) lookups
        Set<Integer> numSet = Arrays.stream(nums).boxed().collect(Collectors.toSet());

        int maxSequenceLength = 0;

        // Iterate through the set to find the longest sequence
        for (int num : numSet) {
            // Only start counting if 'num' is the first element of a sequence
            if (!numSet.contains(num - 1)) {
                int currentNumber = num;
                int currentSequenceLength = 1;

                // Expand sequence while consecutive elements exist
                while (numSet.contains(currentNumber + 1)) {
                    currentNumber++;
                    currentSequenceLength++;
                }

                // Update max sequence length found
                maxSequenceLength = Math.max(maxSequenceLength, currentSequenceLength);
            }
        }

        return maxSequenceLength;
    }
}