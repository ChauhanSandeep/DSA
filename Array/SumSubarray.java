package Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SumSubarray {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] indices = findSubarrayWithSum2(arr, 12);
        System.out.println(Arrays.toString(indices));
    }

    // Only if all the elements in the array are positive
    public static int[] findSubarrayWithSum(int[] arr, int sum) {
        if(arr == null || arr.length == 0) {
            throw new RuntimeException("The array is invalid");
        }
        if(arr.length == 1) {
            if(arr[0] == sum) return new int[] {1, 1};
            else return new int[] {-1, -1};
         }
        int start=0, end=0;
        int currSum = 0;
        while(end < arr.length && start<=end) {
            if(currSum == sum) {
                return new int[] {start, end};
            }else if(currSum < sum) {
                currSum += arr[end];
                end++;
            }else {
                currSum -= arr[start];
                start++;
            }
        }
        return new int[] {-1, -1};
    }

    // If elements present in array are postitive/negative
    public static int[] findSubarrayWithSum2(int[] arr, int sum) {
        Map<Integer, Integer> sumIndexMap = new HashMap<Integer, Integer> ();
        int currSum = 0;
        sumIndexMap.put(0, -1);
        for(int i=0; i<arr.length; i++) {
            if(sumIndexMap.containsKey(sum - arr[i])) {
                return new int[] {sumIndexMap.get(sum - arr[i]), i};
            }
            currSum += arr[i];
            sumIndexMap.put(currSum, i);
        }
        return new int[] {-1, -1};
    }
}
