package Array;

import java.util.HashMap;

/**
 * Count the number of pairs (arr[i], arr[j]) such that arr[i] - arr[j] = i - j.
 * 
 * GeeksforGeeks Problem: https://www.geeksforgeeks.org/count-the-pairs-in-an-array-such-that-the-difference-between-them-and-their-indices-is-equal/
 * 
 * Approach:
 * - Transform the equation to: arr[i] - i = arr[j] - j.
 * - Use a HashMap to count occurrences of each transformed value.
 * - For each value, compute the number of valid pairs using combinatorial counting.
 * - Runs in **O(N) time complexity**, where N is the array length.
 * - Space complexity is **O(N)** due to HashMap storage.
 */
public class CountPairs {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        System.out.println("Total valid pairs: " + new CountPairs().countPairs(arr));
    }

    /**
     * Counts the number of valid pairs satisfying arr[i] - arr[j] = i - j.
     *
     * @param arr Input array.
     * @return Number of valid pairs.
     */
    int countPairs(int[] arr) {
        HashMap<Integer, Integer> frequencyMap = new HashMap<>();
        int totalPairs = 0;

        for (int i = 0; i < arr.length; i++) {
            int transformedValue = arr[i] - i;
            int count = frequencyMap.getOrDefault(transformedValue, 0);
            totalPairs += count; // Each occurrence forms a pair with previous ones
            frequencyMap.put(transformedValue, count + 1);
        }
        return totalPairs;
    }
}
