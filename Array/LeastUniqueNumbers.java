package Array;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Given an array of integers arr and an integer k. Find the least number of unique integers after removing exactly k elements.
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
