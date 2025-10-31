package arrays.heap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Given an array of integers arr and an integer k. Find the least number of unique integers after removing exactly k elements.
 * For example :
 * Input: arr = [4,3,1,1,3,3,2], k = 3
 * Output: 2
 * Explanation: Remove 4, 2 and either one of the two 1s or three 3s. 1 and 3 will be left.
 *
 * LeetCode: https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/
 */
public class LeastUniqueNumbers {

    public static void main(String[] args) {
        int[] arr = {1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3};
        int k = 5;
        int result = new LeastUniqueNumbers().findLeastNumOfUniqueInts(arr, k);
        System.out.println("Least unique numbers after removing " + k + " elements: " + result);
    }

    /**
     * Approach:
     * 1. Count the frequency of each number in the array.
     * 2. Sort the unique numbers based on their frequency in ascending order.
     * 3. Iterate through the sorted list, removing numbers starting from the least frequent until k elements are removed.
     *
     * Time Complexity: O(N log N) where N is the number of unique integers in the array.
     * Space Complexity: O(N) for storing the frequency map and the sorted list of unique integers.
     */
    public int findLeastNumOfUniqueInts(int[] arr, int k) {
        // Count frequency of each number
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : arr) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        // Sort unique numbers by their frequency in ascending order
        List<Integer> uniqueNumbers = frequencyMap.keySet()
                .stream()
                .sorted(Comparator.comparingInt(frequencyMap::get))
                .collect(Collectors.toList());

        int uniqueCount = uniqueNumbers.size();

        for (int num : uniqueNumbers) {
            int freq = frequencyMap.get(num);
            if (k >= freq) {
                k -= freq;
                uniqueCount--;  // Remove this number entirely
            } else {
                break;  // Can't remove this number completely, stop here
            }
        }

        return uniqueCount;
    }
}
