package Array;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Find the maximum sum of triplet ( Ai + Aj + Ak ) such that 0 <= i < j < k < N and Ai < Aj < Ak.
 * If no such triplet exists, return 0.
 *
 * Time Complexity: O(n log n)
 * 
 * https://www.interviewbit.com/problems/maximum-sum-triplet/
 */
public class MaxSumTriplet {
    public static void main(String[] args) {
        List<Integer> list = Stream.of(2, 5, 3, 1, 4, 9).collect(Collectors.toList());
        System.out.println("Maximum Sum Triplet: " + new MaxSumTriplet().findMaxTripletSum(list));
    }

    public int findMaxTripletSum(List<Integer> list) {
        int n = list.size();
        if (n < 3) return 0; // At least 3 elements needed for a valid triplet

        // Step 1: Construct maxRight array (stores max element to the right of each index)
        int[] maxRight = new int[n + 1];
        maxRight[n] = 0;
        for (int i = n - 1; i >= 0; i--) {
            maxRight[i] = Math.max(maxRight[i + 1], list.get(i));
        }

        // Step 2: Use a TreeSet to maintain sorted prefix elements
        TreeSet<Integer> sortedPrefixSet = new TreeSet<>();
        sortedPrefixSet.add(Integer.MIN_VALUE);

        int maxSum = 0;

        // Step 3: Iterate and calculate maximum triplet sum
        for (int i = 0; i < n - 1; i++) {
            int current = list.get(i);
            
            // Ensure there's a valid triplet condition Ai < Aj < Ak
            if (maxRight[i + 1] > current) {
                int lower = sortedPrefixSet.lower(current); // Largest Ai < Aj
                maxSum = Math.max(maxSum, current + maxRight[i + 1] + lower);
            }
            
            sortedPrefixSet.add(current); // Insert current element for future lookups
        }

        return maxSum;
    }
}
