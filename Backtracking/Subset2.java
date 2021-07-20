package Backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Subset2 {

    public static void main(String[] args) {
        int[] arr = {2, 1, 2};
        List<List<Integer>> lists = subsetsWithDup(arr);
        System.out.println(lists);
    }

    /**
     * Find powerset of array. Duplicates can exist
     * @param nums
     * @return
     */
    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if(nums == null || nums.length == 0) return result;

        Arrays.sort(nums);
        List<Integer> list = new ArrayList<>();
        subsetsWithDupRec(result, list, nums, 0);
        return result;
    }

    public static void subsetsWithDupRec(List<List<Integer>> resultList, List<Integer> list, int[] nums, int start) {
        resultList.add(new ArrayList<>(list));
        for(int i=start; i<nums.length; i++) {
            if(i > start && nums[i] == nums[i-1]) {
                continue;
            } else if(i > 0 && nums[i] == nums[i-1]){
                System.out.println("aaaa");
            }
            list.add(nums[i]);
            subsetsWithDupRec(resultList, list, nums, i+1);
            list.remove(list.size() - 1);
        }

    }
}
