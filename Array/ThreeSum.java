package Array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ThreeSum {
    public static void main(String[] args) {
        int[] arr = {1, 4, 45, 6, 10, 8};
        int[] triplets = findTriplets(arr, 22);
        System.out.println(Arrays.toString(triplets));
    }

    public static int[] findTriplets(int[] arr, int sum) {
        for (int i = 0; i < arr.length - 2; i++) {
            int currSum = sum - arr[i];

            // Apply 2 sum technique from here
            Map<Integer, Integer> map = new HashMap<>();
            for (int j = i + 1; j < arr.length; j++) {
                if (map.containsKey(currSum - arr[j])) {
                    return new int[]{arr[i], arr[j], arr[map.get(currSum - arr[j])]};
                } else {
                    map.put(arr[j], j);
                }
            }
        }
        return new int[] {-1, -1, -1};
    }
}