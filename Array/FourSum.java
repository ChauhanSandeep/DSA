package Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FourSum {

    public static void main(String[] args) {
        int[] nums = {-2,-1,-1,1,1,2,2};
        int target = 0;
        System.out.println(fourSum(nums, target));
    }

    public static List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        return kSum(nums, target, 0, 4);
    }

    public static List<List<Integer>> kSum(int[] nums, int target, int start, int k) {
        List<List<Integer>> result = new ArrayList<>();
        if (start == nums.length || nums[start] * k > target || nums[nums.length - 1] * k < target) return result;

        if (k == 2) return twoSum(nums, target, start);
        for (int i = start; i < nums.length; i++) {
            if (i == start || nums[i - 1] != nums[i]) {
                List<List<Integer>> lists = kSum(nums, target - nums[i], i + 1, k - 1);

                for (List<Integer> list : lists) {
                    list.add(nums[i]);
                    result.add(list);
                }
            }
        }
        return result;
    }

    public static List<List<Integer>> twoSum(int[] nums, int target, int start) {
        List<List<Integer>> result = new ArrayList<>();
        int low = start;
        int high = nums.length - 1;
        while (low < high) {
            int sum = nums[low] + nums[high];
            if (sum < target) {
                low++;
            } else if (sum > target) {
                high--;
            } else {
                result.add(Stream.of(nums[low], nums[high]).collect(Collectors.toList()));
                low++;
                high--;
            }
        }
        return result;
    }
}
