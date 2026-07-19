package arrays.twopointers;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

/**
 * Problem: Two Sum
 *
 * Given an array and a target, return indices of two distinct numbers whose sum
 * equals the target. The hash map solution remembers previously seen values so
 * each new number can check its complement immediately.
 *
 * Leetcode: https://leetcode.com/problems/two-sum/ (Easy)
 * Rating:   acceptance 57.8% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Hash map | Complement lookup
 *
 * Example:
 *   Input:  nums = [2,7,11,15], target = 9
 *   Output: [0,1]
 *   Why:    nums[0] + nums[1] = 2 + 7 = 9.
 *
 * Follow-ups:
 *   1. What if the array is sorted?
 *      Use two pointers for O(1) extra space.
 *   2. Return all pairs instead of one?
 *      Store lists of indices per value and continue scanning after matches.
 *   3. Find three numbers for the target?
 *      Sort and fix one value, then use two pointers on the suffix.
 *
 * Related: Two Sum II (167), 3Sum (15), 4Sum (18).
 */
public class TwoSum {
public static void main(String[] args) {
    int[][] inputs = { {2, 7, 11, 15}, {3, 2, 4} };
    int[] targets = { 9, 6 };
    int[][] expected = { {0, 1}, {1, 2} };

    for (int i = 0; i < inputs.length; i++) {
        int[] got = getTwoSumIndices(inputs[i], targets[i]);
        System.out.printf("nums=%s target=%d -> %s  expected=%s%n",
            Arrays.toString(inputs[i]), targets[i], Arrays.toString(got), Arrays.toString(expected[i]));
    }
}

    /**
 * Intuition: when scanning left to right, any valid partner for currentNum
 * must be target - currentNum. If that complement was seen earlier, its index
 * is already in the map; otherwise store currentNum for a future element.
 *
 * Algorithm:
 *   1. Create valueToIndexMap for values already scanned.
 *   2. For each currentIndex, compute complement = target - currentNum.
 *   3. Return the stored complement index and currentIndex if found.
 *   4. Otherwise store currentNum -> currentIndex and continue.
 *
 * Time:  O(n) - each value is looked up and inserted once.
 * Space: O(n) - the map may store every value before a pair is found.
 *
 * @param nums input values
 * @param target desired pair sum
 * @return two indices, or {-1, -1} when no pair exists
 */
    public static int[] getTwoSumIndices(int[] nums, int target) {
        Map<Integer, Integer> valueToIndexMap = new HashMap<>();

        for (int currentIndex = 0; currentIndex < nums.length; currentIndex++) {
            int currentNum = nums[currentIndex];
            int complement = target - currentNum;

            if (valueToIndexMap.containsKey(complement)) {
                return new int[]{valueToIndexMap.get(complement), currentIndex};
            }

            valueToIndexMap.put(currentNum, currentIndex);
        }

        return new int[]{-1, -1};
    }
}
