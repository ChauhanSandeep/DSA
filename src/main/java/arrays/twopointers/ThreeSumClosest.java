package arrays.twopointers;

import java.util.Arrays;

/**
 * Problem: 3Sum Closest
 *
 * Given an integer array and a target, return the sum of three values whose sum
 * is closest to the target. The input is guaranteed to have exactly one answer.
 *
 * Leetcode: https://leetcode.com/problems/3sum-closest/ (Medium)
 * Rating:   acceptance 49.0% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Sorting | Fixed value plus two pointers
 *
 * Example:
 *   Input:  nums = [-1,2,1,-4], target = 1
 *   Output: 2
 *   Why:    -1 + 2 + 1 = 2, which is closer to 1 than any other triplet sum.
 *
 * Follow-ups:
 *   1. Return the triplet values too?
 *      Store the three values whenever closestSum improves.
 *   2. Find the closest 4Sum?
 *      Add another fixed loop before the two-pointer scan.
 *   3. Return all triplets tied for closest?
 *      Track the best difference and collect triplets whose difference equals it.
 *
 * Related: 3Sum (15), 4Sum (18), Two Sum Less Than K (1099).
 */
public class ThreeSumClosest {

    public static void main(String[] args) {
    int[][] inputs = { {-1, 2, 1, -4}, {0, 0, 0} };
    int[] targets = { 1, 1 };
    int[] expected = { 2, 0 };

    for (int i = 0; i < inputs.length; i++) {
        int got = threeSumClosest(inputs[i].clone(), targets[i]);
        System.out.printf("nums=%s target=%d -> %d  expected=%d%n",
            Arrays.toString(inputs[i]), targets[i], got, expected[i]);
    }
}

    /**
 * Intuition: sorting makes each fixed first value pair with a controllable
 * two-pointer search. If currentSum is too small, moving left right is the
 * only way to increase it; if too large, moving right left is the only way to
 * decrease it. Track the smallest absolute difference seen.
 *
 * Algorithm:
 *   1. Sort nums.
 *   2. Fix each fixedIndex as the first triplet value.
 *   3. Scan the suffix with left and right pointers.
 *   4. Return on exact target, otherwise update closestSum and move the pointer that improves direction.
 *
 * Time:  O(n^2) - each fixed value runs a linear two-pointer scan.
 * Space: O(1) - excluding sorting implementation storage.
 *
 * @param nums input values
 * @param target sum to approach
 * @return triplet sum closest to target
 */
    public static int threeSumClosest(int[] nums, int target) {
        // Sort the array to use the two-pointer approach.
        Arrays.sort(nums);

        int len = nums.length;
        int minDiff = Integer.MAX_VALUE; // Track the smallest difference
        int closestSum = 0; // Result to hold the closest sum

        // Iterate through each number as the first element of the triplet
        for (int fixedIndex = 0; fixedIndex < len - 2; fixedIndex++) {
            int left = fixedIndex + 1;
            int right = len - 1;

            // Two-pointer approach to find the best pair
            while (left < right) {
                int currentSum = nums[fixedIndex] + nums[left] + nums[right];

                // If exact match found, return immediately
                if (currentSum == target) {
                    return currentSum;
                }

                // Update the closest sum if the current one is closer
                int currentDiff = Math.abs(target - currentSum);
                if (currentDiff < minDiff) {
                    minDiff = currentDiff;
                    closestSum = currentSum;
                }

                // Adjust pointers to try to get closer to target
                if (currentSum < target) {
                    left++; // Increase sum
                } else {
                    right--; // Decrease sum
                }
            }
        }

        return closestSum;
    }
}