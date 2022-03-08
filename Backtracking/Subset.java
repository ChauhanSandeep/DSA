package Backtracking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Given an integer array nums of unique elements, return all possible subsets (the power set).
 *
 * https://leetcode.com/problems/subsets/
 */
public class Subset {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        List<List<Integer>> subsets = new Subset().subsets(nums);
        System.out.println(subsets);
    }

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if(nums == null || nums.length ==0) return result;

        List<Integer> curr = new ArrayList<>();
        subsetRec(nums, curr, result, 0);
        return result;

    }

    public void subsetRec(int[] nums, List<Integer> curr, List<List<Integer>> result, int start) {
        result.add(new ArrayList<>(curr));

        for(int i= start; i<nums.length; i++) {
            curr.add(nums[i]);
            subsetRec(nums, curr, result, i+1);
            curr.remove(curr.size() - 1);
        }
    }
}
