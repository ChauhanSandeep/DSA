package Recursion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given an array find the subarray which gives the sum provided.
 * Same number can not be used multiple times. Subarray need not continous
 */
public class CombinationSum2 {

    public static void main(String[] args) {
        ArrayList<Integer> input = (ArrayList<Integer>) Stream.of(10, 1, 2, 7, 6, 1, 5).collect(Collectors.toList());
        ArrayList<ArrayList<Integer>> arrayLists = new CombinationSum2().combinationSum(input, 8);
        System.out.println(arrayLists);
    }

    ArrayList<ArrayList<Integer>> result;
    public ArrayList<ArrayList<Integer>> combinationSum(ArrayList<Integer> input, int target) {
        result = new ArrayList<>();
        if(input == null || input.size() == 0) return result;
        Collections.sort(input);
        boolean[] visited = new boolean[input.size()];

        combinationSum(input, new ArrayList<>(), 0, 0, target, visited);
        return result;
    }

    public void combinationSum(ArrayList<Integer> input, ArrayList<Integer> currList, int index, int curr, int target, boolean[] visited){
        if(index > input.size() || curr > target) return;

        if(curr == target) {
            result.add(new ArrayList<>(currList));
            return;
        }

        for(int i=index; i<input.size(); i++) {
            if(i == 0 || (input.get(i).intValue() != input.get(i-1).intValue()) || (input.get(i).intValue() == input.get(i-1).intValue() && visited[i-1])){
                visited[i] = true;
                currList.add(input.get(i));
                combinationSum(input, currList, i+1, curr + input.get(i), target, visited);
                visited[i] = false;
                currList.remove(currList.size() - 1);
            }
        }
    }

}
