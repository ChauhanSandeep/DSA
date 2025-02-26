package Array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    public static void main(String[] args) {
        int[] arr = {12, 3, 4, 1, 6, 9};
        int[] result = findTwoSum(arr, 10);

        if (result != null) {
            System.out.println("Indices: " + result[0] + ", " + result[1]);
            System.out.println("Numbers: " + arr[result[0]] + ", " + arr[result[1]]);
        } else {
            System.out.println("No valid pair found.");
        }
    }

    public static int[] findTwoSum(int[] arr, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < arr.length; i++) {
            if (map.containsKey(target - arr[i])) {
                return new int[]{map.get(target - arr[i]), i}; // Return indices
            }
            map.put(arr[i], i);
        }
        
        return null; // More explicit failure indicator
    }
}
