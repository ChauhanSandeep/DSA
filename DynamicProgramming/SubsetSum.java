package DynamicProgramming;

import java.util.HashMap;
import java.util.Map;

/**
 * Count number of subarrays with given sum in provided array.
 * subarrays are continous
 */
public class SubsetSum {
    public static void main(String[] args) {
        int[] nums = {1, 1, 1};
        int sum = 2;
        System.out.println(subarraySum(nums, sum));
    }

    public static int subarraySum(int[] nums, int k) {
        int sum = 0;
        int count = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1); // used when first match is found
        for(int i=0; i<nums.length; i++) {
            sum += nums[i];
            if(map.containsKey(sum-k)) {
                count = count + map.get(sum-k);
            }
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return count;
    }
}
