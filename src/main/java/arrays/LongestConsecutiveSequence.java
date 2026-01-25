package arrays;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Longest Consecutive Sequence
 *
 * Problem:
 * Given an unsorted array of integers, find the length of the longest consecutive elements sequence.
 * You must write an algorithm that runs in O(n) time.
 *
 * Example 1:
 * Input: nums = [100,4,200,1,3,2]
 * Output: 4
 * Explanation: The longest consecutive sequence is [1, 2, 3, 4], which has length 4.
 *
 * Example 2:
 * Input: nums = [0,3,7,2,5,8,4,6,0,1]
 * Output: 9
 * Explanation: The longest consecutive sequence is [0,1,2,3,4,5,6,7,8], which has length 9.
 *
 * Constraints:
 * - 0 <= nums.length <= 10^5
 * - -10^9 <= nums[i] <= 10^9
 *
 * LeetCode: https://leetcode.com/problems/longest-consecutive-sequence/
 *
 * Follow-up Questions:
 * Q1: What if we're allowed to use sorting? Would it be simpler?
 * A1: Yes, sort in O(n log n) then scan for consecutive sequences in O(n). Simpler but slower.
 *
 * Q2: How would you find all consecutive sequences, not just the longest one?
 * A2: Same approach but store all sequences in a list instead of just tracking maximum length.
 *
 * Q3: What if numbers can repeat and we want consecutive unique numbers?
 * A3: The HashSet approach already handles this by deduplicating the input automatically.
 *
 * Q4: Can we solve this in O(1) space (excluding output)?
 * A4: Not in O(n) time. We'd need O(n log n) time if we sort in-place, compromising time for space.
 *
 * Q5: How would you modify this to find the longest arithmetic sequence with any common difference?
 * A5: Use HashMap with (value, length) pairs and for each number, check if (num - diff) exists for various diffs.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class LongestConsecutiveSequence {
    public static void main(String[] args) {
        int[] nums = {100, 4, 200, 1, 3, 2};
        int result = new LongestConsecutiveSequence().findLongestConsecutiveSequence(nums);
        System.out.println("Longest Consecutive Sequence Length: " + result);
    }

    /**
     * Finds the length of the longest consecutive sequence in an unsorted array.
     *
     * Algorithm:
     * 1. Convert array to HashSet for O(1) lookups
     * 2. For each number, check if it's the start of a sequence (num-1 doesn't exist)
     * 3. If it's a sequence start, count consecutive numbers (num+1, num+2, ...)
     * 4. Track the maximum sequence length found
     * 5. Return the maximum length
     *
     * Key Insight: Only start counting from sequence beginnings (when num-1 doesn't exist).
     * This ensures each number is visited at most once, achieving O(n) time.
     *
     * Time Complexity: O(n) - each number processed at most twice (once in outer loop, once in inner)
     * Space Complexity: O(n) - HashSet stores all unique numbers
     *
     * @param nums Input array of integers
     * @return Length of the longest consecutive sequence
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
}