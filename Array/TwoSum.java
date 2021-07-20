package Array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    public static void main(String[] args) {
        int[] arr = {12, 3, 4, 1, 6, 9};
        int[] result = findTwoSum(arr, 10);
        System.out.println(Arrays.toString(result));
    }

    public static int[] findTwoSum(int[] arr, int sum) {
        Map<Integer, Integer> map = new HashMap<>();
        for(int i=0; i<arr.length; i++) {
            if(map.containsKey(sum - arr[i])) {
                return new int[] {sum-arr[i], arr[i]};
            }
            map.put(arr[i], i);
        }
        return new int[] {-1, -1};
    }
}
