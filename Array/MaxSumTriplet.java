package Array;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * You need to find the maximum sum of triplet ( Ai + Aj + Ak ) such that 0 <= i < j < k < N and Ai < Aj < Ak.
 * If no such triplet exist return 0.
 *
 * https://www.interviewbit.com/problems/maximum-sum-triplet/
 */
public class MaxSumTriplet {
    public static void main(String[] args) {
        ArrayList<Integer> list = (ArrayList<Integer>) Stream.of(2, 5, 3, 1, 4, 9).collect(Collectors.toList());
        System.out.println(new MaxSumTriplet().solve(list));
    }

    /**
     * this is O(nLog(n)) time complexity
     */
    public int solve(ArrayList<Integer> list) {
        int size = list.size();
        int[] rightMaxArr = new int[size + 1];
        rightMaxArr[size] = 0;
        for(int i=size-1; i>=0; i--) {
            rightMaxArr[i] = Math.max(rightMaxArr[i+1], list.get(i));
        }

        int result = 0;
        TreeSet<Integer> set = new TreeSet<>();
        set.add(Integer.MIN_VALUE);

        for(int i=0; i<list.size()-1; i++) {
            if(rightMaxArr[i+1] > list.get(i)) {
                result = Math.max(result, list.get(i) + rightMaxArr[i+1] + set.lower(list.get(i)));
                set.add(list.get(i));
            }
        }
        return result;
    }
}
