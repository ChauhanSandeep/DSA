package greedy;

import java.util.HashMap;
import java.util.Map;

public class MajorityElement {

    public static void main(String[] args) {
        int[] arr = {1, 2, 2, 1, 1};
        System.out.println(new MajorityElement().majorityElement(arr)); // Output: 1
    }

    /**
     * Boyer-Moore Voting Algorithm
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int majorityElement(final int[] arr) {
        int candidateIndex = 0;
        int count = 1;

        // Find candidate for majority element
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == arr[candidateIndex]) count++;
            else count--;

            if (count == 0) {
                candidateIndex = i;
                count = 1;
            }
        }

        // Verify candidate is actually a majority element (> N/2 occurrences)
        int candidate = arr[candidateIndex];
        count = 0;
        for (int num : arr) {
            if (num == candidate) count++;
        }

        return count > arr.length / 2 ? candidate : -1; // Return -1 if no majority element
    }

    /**
     * HashMap Approach (Alternative)
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int majorityElementWithMap(final int[] arr) {
        int target = arr.length / 2;
        Map<Integer, Integer> map = new HashMap<>();

        for (int num : arr) {
            map.put(num, map.getOrDefault(num, 0) + 1);
            if (map.get(num) > target) return num;
        }

        return -1; // This case shouldn't occur as problem assumes majority exists
    }
}
