package arrays.heap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Problem: Least Number of Unique Integers after K Removals
 *
 * Remove exactly k elements from an array so the number of distinct integers left
 * is as small as possible. Removing only some copies of a value does not reduce
 * the distinct count; a value disappears only when all of its copies are removed.
 *
 * Leetcode: https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/
 * Rating:   1284 (zerotrac Elo, Q2, weekly-contest-193)
 * Pattern:  Arrays | Greedy | Remove lowest frequencies first
 *
 * Example:
 *   Input:  arr = [4,3,1,1,3,3,2], k = 3
 *   Output: 2
 *   Why:    remove 4 and 2 first because each costs one deletion, then remove
 *           both 1s; only the value 3 remains.
 *
 * Follow-ups:
 *   1. What if you need to maximize the number of unique integers left?
 *      Spend removals on duplicates first so each distinct value keeps one copy.
 *   2. What if k may be larger than the array length?
 *      Clamp it or return 0 when all elements can be removed.
 *   3. What if you must return which values were fully removed?
 *      Sort value-frequency pairs and collect each value whose full count is paid.
 */
public class LeastUniqueNumbers {
    public static void main(String[] args) {
        LeastUniqueNumbers solver = new LeastUniqueNumbers();
        int[][] inputs = {{4, 3, 1, 1, 3, 3, 2}, {5, 5, 4}, {1, 2, 3}};
        int[] removals = {3, 1, 3};
        int[] expected = {2, 1, 0};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.findLeastNumOfUniqueInts(inputs[i], removals[i]);
            System.out.printf("arr=%s k=%d -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), removals[i], got, expected[i]);
        }
    }

    /**
     * Intuition (interview default): a distinct value disappears only if every copy
     * of that value is removed. To minimize the number of distinct values left, buy
     * the cheapest disappearances first: values with frequency 1, then frequency 2,
     * and so on. Sorting the frequencies gives that exact order. Once the next
     * full frequency costs more removals than we have left, no later frequency can
     * be removed either because the list is sorted ascending.
     *
     * Time:  O(n log n) - sorting the frequency list dominates the linear counting pass.
     * Space: O(n) - the map and frequency list can store one entry per input value.
     *
     * @param arr input values
     * @param k number of elements to remove
     * @return minimum possible number of unique integers after exactly k removals
     */
    public int findLeastNumOfUniqueInts(int[] arr, int k) {
        // Count frequency of each unique integer
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : arr) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        
        // Extract and sort frequencies in ascending order
        List<Integer> frequencies = new ArrayList<>(freqMap.values());
        Collections.sort(frequencies);
        
        int uniqueCount = frequencies.size();
        
        // Greedily remove integers with lowest frequencies
        for (int freq : frequencies) {
            if (k >= freq) {
                k -= freq;
                uniqueCount--;
            } else {
                break;  // Can't remove complete group, stop
            }
        }
        
        return uniqueCount;
    }


    /**
     * Alternative approach using counting sort for better performance when max frequency is small.
     * 
     * Algorithm:
     * 1. Count frequency of each unique integer
     * 2. Count frequency-of-frequencies (how many integers have each frequency)
     * 3. Process from frequency 1 upward, removing complete groups greedily
     * 4. Return remaining unique count
     * 
     * Time Complexity: O(N + F) where N is array length and F is maximum frequency.
     * Better than O(N log N) when F << N.
     * 
     * Space Complexity: O(N + F) for frequency map and frequency count array.
     * 
     * @param arr array of integers
     * @param k number of elements to remove
     * @return minimum number of unique integers after k removals
     */
    public int findLeastNumOfUniqueIntsCountingSort(int[] arr, int k) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : arr) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        
        // Count how many integers have each frequency
        int maxFreq = Collections.max(freqMap.values());
        int[] freqCount = new int[maxFreq + 1];
        
        for (int freq : freqMap.values()) {
            freqCount[freq]++;
        }
        
        int uniqueCount = freqMap.size();
        
        // Process frequencies from smallest to largest
        for (int freq = 1; freq <= maxFreq; freq++) {
            int integersWithThisFreq = freqCount[freq];
            
            // How many complete integers of this frequency can we remove?
            int canRemove = Math.min(k / freq, integersWithThisFreq);
            
            k -= canRemove * freq;
            uniqueCount -= canRemove;
            
            if (k < freq) {
                break;  // Not enough removals left for any more complete integers
            }
        }
        
        return uniqueCount;
    }
}
