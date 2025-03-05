package Hashing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://www.interviewbit.com/problems/largest-continuous-sequence-zero-sum/
 *
 * Given an array of integers, find the largest contiguous subarray that sums to zero.
 *
 * Approach:
 * - Maintain a running sum while iterating through the list.
 * - Use a hashmap to store previously seen sums and their corresponding indices.
 * - If the same sum is encountered again, a subarray with zero sum exists between those indices.
 * - Keep track of the longest such subarray.
 *
 * Time Complexity: O(N) - Single pass through the array.
 * Space Complexity: O(N) - Storing prefix sums in a hashmap.
 */
public class ZeroSum {
    public static void main(String[] args) {
        ArrayList<Integer> inputList = new ArrayList<>(Stream.of(1, 2, -2, 4, -4).collect(Collectors.toList()));
        List<Integer> result = new ZeroSum().findLargestZeroSumSubarray(inputList);
        System.out.println(result);
    }

    public List<Integer> findLargestZeroSumSubarray(ArrayList<Integer> nums) {
        Map<Integer, Integer> prefixSumIndices = new HashMap<>(); // Stores sum -> first occurrence index
        prefixSumIndices.put(0, -1); // Handles cases where subarray starts from index 0

        int sum = 0;
        int maxLen = 0;
        int start = -1, end = -1;

        for (int i = 0; i < nums.size(); i++) {
            sum += nums.get(i);

            if (prefixSumIndices.containsKey(sum)) {
                int previousIndex = prefixSumIndices.get(sum);
                int currentLength = i - previousIndex;
                if (currentLength > maxLen) {
                    maxLen = currentLength;
                    start = previousIndex + 1;
                    end = i;
                }
            } else {
                prefixSumIndices.put(sum, i);
            }
        }

        return (maxLen > 0) ? nums.subList(start, end + 1) : new ArrayList<>();
    }
}
