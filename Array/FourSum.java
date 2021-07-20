package Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FourSum {

    public static void main(String[] args) {
        int[] nums = {-2,-1,-1,1,1,2,2};
        int target = 0;
        System.out.println(fourSum(nums, target));


//        int[] nums = {2, 2, 2, 2, 2};
//        int target = 8;
//        System.out.println(fourSum(nums, target));
//
//        int[] nums = {-2,-1,-1,1,1,2,2};
//        int target = 0;
//        System.out.println(fourSum(nums, target));

    }

    public static List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        return kSum(nums, target, 0, 4);
    }

    public static List<List<Integer>> kSum(int[] nums, int target, int start, int k) {
        List<List<Integer>> result = new ArrayList<>();
        if(start == nums.length || nums[start] * k > target || nums[nums.length - 1] * k < target) return result;

        if(k == 2) return twoSum(nums, target, start);
        for(int i=start; i<nums.length; i++) {
            if(i == start || nums[i-1] != nums[i]) {
                List<List<Integer>> lists = kSum(nums, target-nums[i], i+1, k-1);

                for(List<Integer> list: new HashSet<>(lists)) { // put in hashset for uniqueness
                    result.add(list);
                    result.get(result.size() - 1).add(nums[i]);
                }
            }
        }
        return result;
    }

    public static List<List<Integer>> twoSum(int[] nums, int target, int start) {
        List<List<Integer>> result = new ArrayList<>();
        int low = start;
        int high = nums.length - 1;
        while(low < high) {
            if(nums[low] + nums[high] < target) {
                low++;
            }else if (nums[low] + nums[high] > target) {
                high--;
            }else {
                result.add(Stream.of(nums[low], nums[high]).collect(Collectors.toList())); // Arrays.asList throws unsupported exception because of immutability
                low++;
                high--;
            }
        }
        return result;
    }

}
