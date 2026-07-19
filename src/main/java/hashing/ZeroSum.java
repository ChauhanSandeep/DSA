package hashing;

import java.util.*;

/**
 * Problem: Largest Continuous Sequence Zero Sum
 *
 * Given a list of integers, return the longest contiguous subarray whose sum is
 * zero. If no such subarray exists, return an empty list.
 *
 * InterviewBit: https://www.interviewbit.com/problems/largest-continuous-sequence-zero-sum/
 * Pattern:      Hashing | Prefix sum | First occurrence index
 *
 * Example:
 *   Input:  nums = [1,2,-2,4,-4]
 *   Output: [2,-2,4,-4]
 *   Why:    the running sum before 2 and after -4 is the same, so the values
 *           between those indices sum to zero.
 *
 * Follow-ups:
 *   1. How would you return all maximum-length zero-sum subarrays?
 *      Keep every range that matches the best length instead of only one start/end pair.
 *   2. How would you count all zero-sum subarrays?
 *      Store prefix-sum frequencies and add the previous frequency each time a sum repeats.
 *   3. How would you target sum k instead of zero?
 *      Look for current prefix sum minus k in the map.
 *   4. How would integer overflow be avoided?
 *      Use long for the running prefix sum and map keys.
 *
 * Related: Subarray Sum Equals K (560), Continuous Subarray Sum (523).
 */
public class ZeroSum {
    public static void main(String[] args) {
        ZeroSum solver = new ZeroSum();
        List<ArrayList<Integer>> inputs = Arrays.asList(
            new ArrayList<>(Arrays.asList(1, 2, -2, 4, -4)),
            new ArrayList<>(Arrays.asList(1, 2, 3))
        );
        String[] expected = { "[2, -2, 4, -4]", "[]" };

        for (int i = 0; i < inputs.size(); i++) {
            List<Integer> got = solver.findLargestZeroSumSubarray(inputs.get(i));
            System.out.printf("nums=%s -> %s  expected=%s%n", inputs.get(i), got, expected[i]);
        }
    }

    /**
     * Intuition: equal prefix sums mean the values between those two indices sum
     * to zero. Store the first index where each prefix sum appears so a repeated
     * sum gives the longest zero-sum range ending at the current index.
     *
     * Algorithm:
     *   1. Seed prefix sum 0 at index -1 to allow ranges starting at index 0.
     *   2. Scan the list while updating the running sum.
     *   3. If the sum was seen before, update the best range; otherwise store its first index.
     *   4. Return the best range or an empty list if none was found.
     *
     * Time:  O(n) - each element is processed once.
     * Space: O(n) - the map may store one prefix sum per index.
     *
     * @param nums input list of integers
     * @return longest contiguous zero-sum subarray, or an empty list
     */
    public List<Integer> findLargestZeroSumSubarray(ArrayList<Integer> nums) {
        Map<Integer, Integer> prefixSumIndices = new HashMap<>(); // (sum -> first occurrence index)
        prefixSumIndices.put(0, -1); // Handles case where subarray starts from index 0

        int runningSum = 0;
        int maxLength = 0;
        int start = -1, end = -1;

        for (int i = 0; i < nums.size(); i++) {
            runningSum += nums.get(i);

            // If runningSum is seen before, update max-length subarray
            if (prefixSumIndices.containsKey(runningSum)) {
                int previousIndex = prefixSumIndices.get(runningSum);
                int currentLength = i - previousIndex;

                if (currentLength > maxLength) {
                    maxLength = currentLength;
                    start = previousIndex + 1;
                    end = i;
                }
            } else {
                // Store first occurrence of the sum
                prefixSumIndices.put(runningSum, i);
            }
        }

        // Return the subarray if found, otherwise return empty list
        return (maxLength > 0) ? nums.subList(start, end + 1) : Collections.emptyList();
    }
}
