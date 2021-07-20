package Backtracking;

import java.util.ArrayList;
import java.util.List;

public class Subset {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        List<List<Integer>> subsets = subsets(nums);
        System.out.println(subsets);
    }

    /**
     * Given an integer array nums of unique elements, return all possible subsets (the power set).
     */
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> resultList = new ArrayList<>();
        subsetsHelper(resultList, new ArrayList<>(), nums, 0);
        return resultList;
    }

    private static void subsetsHelper(List<List<Integer>> resultList , List<Integer> list, int [] nums, int start){
        resultList.add(new ArrayList<>(list));

        for(int i = start; i < nums.length; i++){
            // add element
            list.add(nums[i]);
            // Explore
            subsetsHelper(resultList, list, nums, i + 1);
            // remove
            list.remove(list.size() - 1);
        }
    }
}
