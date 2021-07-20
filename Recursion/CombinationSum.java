package Recursion;

import java.util.ArrayList;
import java.util.List;

public class CombinationSum {
    public static void main(String[] args) {
        int[] arr = {2, 3, 6, 7};
        int sum = 7;
        List<List<Integer>> ans = combinationSumHelper(arr, sum);
        System.out.println(ans);
    }

    /**
     * Given an array find the subarray which gives the sum provided
     * @param arr
     * @param sum
     * @return
     */
    public static List<List<Integer>> combinationSumHelper(int[] arr, int sum) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        combinationSumRec(arr, 0, sum, 0, temp, ans);
        return ans;
    }

    private static void combinationSumRec(int[] arr, int startIndex, int target, int currSum, List<Integer> list, List<List<Integer>> result){
        if(currSum>target) return;

        if(currSum==target){
            result.add(new ArrayList<>(list));
            return;
        }

        for(int i=startIndex; i<arr.length; i++){
            list.add(arr[i]);
            combinationSumRec(arr, i, target, currSum+arr[i], list, result);
            list.remove(list.size()-1);
        }
    }
}
