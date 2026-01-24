package arrays.twopointers;

import java.util.HashMap;
import java.util.Map;

/**
 * Two Sum
 *
 * Problem:
 * Given an array of integers nums and an integer target, return indices of the two numbers
 * such that they add up to target. You may assume that each input would have exactly one solution,
 * and you may not use the same element twice.
 *
 * Example:
 * Input: nums = [2,7,11,15], target = 9
 * Output: [0,1]
 * Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].
 *
 * Example:
 * Input: nums = [3,2,4], target = 6
 * Output: [1,2]
 *
 * Constraints:
 * - 2 <= nums.length <= 10^4
 * - -10^9 <= nums[i] <= 10^9
 * - -10^9 <= target <= 10^9
 * - Only one valid answer exists
 *
 * LeetCode: https://leetcode.com/problems/two-sum/
 *
 * Follow-up Questions:
 * Q1: What if the array is sorted? Can we optimize space?
 * A1: Yes, use two-pointer approach (one at start, one at end) with O(1) space instead of O(n).
 *
 * Q2: What if we need to find all pairs that sum to target, not just indices?
 * A2: Continue iterating after finding pairs, and use a Set to avoid duplicates.
 *
 * Q3: What if we need to find three numbers that sum to target (3Sum)?
 * A3: Fix one number and apply two-pointer or hash map approach on remaining array.
 *
 * Q4: How would you handle the case where input can have duplicate solutions?
 * A4: Store all indices for each value in a list within the HashMap, then return all valid pairs.
 *
 * Q5: Can you solve this with O(1) space but allowing modification of input array?
 * A5: Not efficiently in O(n) time. Sorting would take O(n log n), then two-pointers in O(n).
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class TwoSum {
    public static void main(String[] args) {
        int[] nums = {12, 3, 4, 1, 6, 9};
        int target = 10;

        int[] result = getTwoSumIndices(nums, target);

        if (result[0] != -1) {
            System.out.println("Two Sum indices for target " + target + ": [" + result[0] + ", " + result[1] + "]");
            System.out.println("Numbers: " + nums[result[0]] + " + " + nums[result[1]] + " = " + target);
        } else {
            System.out.println("No valid pair found.");
        }
    }

    /**
     * Finds indices of two numbers that add up to the target using hash map approach.
     *
     * Algorithm:
     * 1. Create a hash map to store seen numbers and their indices
     * 2. For each number, calculate its complement (target - number)
     * 3. Check if complement exists in map - if yes, we found the pair
     * 4. If no, add current number and index to map
     * 5. Continue until pair is found
     *
     * Key Insight: Instead of checking all pairs (O(n²)), we check each number once
     * by remembering previously seen numbers in a hash map.
     *
     * Time Complexity: O(n) - single pass through array
     * Space Complexity: O(n) - hash map stores at most n elements
     *
     * @param nums Input array of integers
     * @param target Target sum to find
     * @return Array containing indices of the two numbers, or {-1, -1} if no pair exists
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
