package Backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Permutation {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        List<List<Integer>> permute = permute(nums);
        System.out.println(permute);
    }

    /**
     * Find all the permutations of the numbers in array provided that all elements in array are unique
     * @return
     */
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        permuteRec(nums, 0, new LinkedList<>(), result);
        return result;
    }

    private static void permuteRec(int[] nums, int index, List<Integer> cur, List<List<Integer>> result) {
        System.out.println(cur);
        if (cur.size() == nums.length) {
            System.out.println("Adding "+ cur);
            result.add(new ArrayList<>(cur));
            return;
        }

        for (int pos = 0; pos <= cur.size(); pos++) {
            System.out.println("Adding "+ nums[index] + " to index " + pos);
            cur.add(pos, nums[index]);
            permuteRec(nums, index + 1, cur, result);
            System.out.println("Removing from pos "+ pos);
            cur.remove(pos);
        }

    }
}
