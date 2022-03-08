package Backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Find all the subsets of the array having duplicates in array.
 *
 * https://leetcode.com/problems/subsets-ii/
 */
public class Subset2 {

    public static void main(String[] args) {
        int[] arr = {2, 1, 2};
        List<List<Integer>> lists = new Subset2().subsetsWithDup(arr);
        System.out.println(lists);
    }

    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if(nums== null || nums.length == 0) return result;

        Arrays.sort(nums);
        subsetRec(nums, result, new ArrayList<>(), 0);
        return result;
    }

    public void subsetRec(int[] nums, List<List<Integer>> result, List<Integer> list, int start) {
        result.add(new ArrayList<>(list));

        for(int i=start; i<nums.length; i++) {
            if(i > start && nums[i-1] == nums[i]) continue;

            list.add(nums[i]);
            subsetRec(nums, result, list, i+1);
            list.remove(list.size() - 1);
        }
    }
}
