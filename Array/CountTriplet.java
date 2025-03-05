package Array;

import java.util.HashSet;
import java.util.Set;

/**
 * Given an array, find all the triplets such that the sum of two elements equals the third element.
 * 
 * Approach:
 * - Store all elements in a HashSet for quick lookup.
 * - Iterate through all pairs and check if their sum exists in the set.
 * - Runs in **O(N^2) time complexity** due to nested loops.
 * - Space complexity is **O(N)** for the HashSet storage.
 */
public class CountTriplet {
    public static void main(String args[]) {
        int arr[] = {5, 32, 1, 7, 10, 50, 19, 21, 2};
        System.out.println("Total triplets: " + countTriplet(arr));
    }

    /**
     * Counts the number of triplets where the sum of two elements equals a third element.
     *
     * @param arr Input array.
     * @return Number of valid triplets.
     */
    static int countTriplet(int arr[]) {
        Set<Integer> set = new HashSet<>();
        int result = 0;
        
        // Store all elements in a set for quick lookup
        for (int num : arr) {
            set.add(num);
        }

        // Check for all pairs if their sum exists in the set
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (set.contains(arr[i] + arr[j])) {
                    result++;
                }
            }
        }
        return result;
    }
}
