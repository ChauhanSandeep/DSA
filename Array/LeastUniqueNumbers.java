package Array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Given an array of integers arr and an integer k. Find the least number of unique integers after removing exactly k elements.
 *
 * https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/
 */
public class LeastUniqueNumbers {

    public static void main(String[] args) {
        int[] arr = {1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3};
        int result = new LeastUniqueNumbers().findLeastNumOfUniqueInts(arr, 5);
        System.out.println(result);
    }

    public int findLeastNumOfUniqueInts(int[] arr, int k) {
        Map<Integer, Integer> numFreqMap = new HashMap<>();
        for (int num : arr) {
            numFreqMap.put(num, numFreqMap.getOrDefault(num, 0) + 1);
        }
        // get all the unique numbers
        List<Integer> nums = new ArrayList<>(numFreqMap.keySet());
        // sort unique numbers by their frequency
        Collections.sort(nums, (a, b) -> numFreqMap.get(a) - numFreqMap.get(b));

        int size = numFreqMap.size();
        int removedNums = 0;
        int index = 0;
        while (k > 0 && index < size) {
            int freq = numFreqMap.get(nums.get(index++));
            // remove all numbers
            k = k - freq;
            if (k >= 0) {
                removedNums++;
            }
        }
        // return total uniqueNumber - Numbers removed
        return size - removedNums;
    }
}
