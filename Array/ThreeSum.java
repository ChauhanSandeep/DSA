package Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {
    public static void main(String[] args) {
        int[] arr = {1, 4, 45, 6, 10, 8, 8};
        int sum = 22;
        List<List<Integer>> triplets = findTriplets(arr, sum);
        System.out.println(triplets);
    }

    public static List<List<Integer>> findTriplets(int[] arr, int target) {
        Arrays.sort(arr); // Sorting helps with two-pointer approach
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < arr.length - 2; i++) {
            if (i > 0 && arr[i] == arr[i - 1]) continue; // Skip duplicates

            int left = i + 1, right = arr.length - 1;
            while (left < right) {
                int sum = arr[i] + arr[left] + arr[right];

                if (sum == target) {
                    result.add(Arrays.asList(arr[i], arr[left], arr[right]));

                    // Move pointers and skip duplicates
                    while (left < right && arr[left] == arr[left + 1]) left++;
                    while (left < right && arr[right] == arr[right - 1]) right--;

                    left++;
                    right--;
                } else if (sum < target) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }
}
