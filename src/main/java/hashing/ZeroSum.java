package hashing;

import java.util.*;

/**
 * Problem: Find the Largest Continuous Subarray with Zero Sum
 * https://www.interviewbit.com/problems/largest-continuous-sequence-zero-sum/
 *
 * Approach:
 * - Maintain a running prefix sum while iterating through the list.
 * - Use a HashMap to store (prefix sum -> first occurrence index).
 * - If the same sum is encountered again, a zero-sum subarray exists between those indices.
 * - Keep track of the longest such subarray.
 *
 * Time Complexity: O(N) - Single pass through the array.
 * Space Complexity: O(N) - Storing prefix sums in a hashmap.
 */
public class ZeroSum {
    public static void main(String[] args) {
        ArrayList<Integer> inputList = new ArrayList<>(Arrays.asList(1, 2, -2, 4, -4));
        List<Integer> result = new ZeroSum().findLargestZeroSumSubarray(inputList);
        System.out.println(result); // Expected output: [2, -2, 4, -4]
    }

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
