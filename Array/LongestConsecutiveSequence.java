package Array;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Given an unsorted array of integers nums, return the length of the longest consecutive elements sequence.
 * You must write an algorithm that runs in O(n) time.
 * 
 * LeetCode: https://leetcode.com/problems/longest-consecutive-sequence/
 */
public class LongestConsecutiveSequence {
    public static void main(String[] args) {
        int[] nums = {100, 4, 200, 1, 3, 2};
        int result = new LongestConsecutiveSequence().longestConsecutive(nums);
        System.out.println("Longest Consecutive Sequence Length: " + result);
    }

    public int longestConsecutive(int[] nums) {
        if (nums.length == 0) return 0;

        // Convert array to set for O(1) lookups
        Set<Integer> numSet = Arrays.stream(nums).boxed().collect(Collectors.toSet());

        int longestSequence = 0;

        for (int num : numSet) {
            // Only process if it's the start of a sequence
            if (!numSet.contains(num - 1)) {
                int currentNum = num;
                int currentStreak = 1;

                while (numSet.contains(currentNum + 1)) {
                    currentNum++;
                    currentStreak++;
                }

                longestSequence = Math.max(longestSequence, currentStreak);
            }
        }

        return longestSequence;
    }
}
