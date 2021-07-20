package Backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Permutation2 {

    public static void main(String[] args) {
        int[] arr = {3, 3, 0, 3};
        List<List<Integer>> lists = permuteUnique(arr);
        System.out.println(lists);
    }


    /**
     * Find all permutations of numbers in array. There can be duplicates in array.
     * @return
     */
    public static List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        Arrays.sort(nums);
        permuteUniqueRec(list, new ArrayList<>(), nums, new boolean[nums.length]);
        return list;
    }

    private static void permuteUniqueRec(List<List<Integer>> result, List<Integer> cur, int[] nums, boolean[] used) {
        if (cur.size() == nums.length) {
            result.add(new ArrayList<>(cur));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i] || (i > 0 && nums[i] == nums[i - 1] && !used[i - 1])) {
                continue;
            }
            used[i] = true;
            cur.add(nums[i]);
            permuteUniqueRec(result, cur, nums, used);
            used[i] = false;
            cur.remove(cur.size() - 1);
        }

    }


}
