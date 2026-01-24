package arrays.heap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Problem: Least Number of Unique Integers after K Removals
 * 
 * Given an array of integers arr and an integer k, find the least number of unique
 * integers after removing exactly k elements.
 * 
 * Example:
 * Input: arr = [4,3,1,1,3,3,2], k = 3
 * Output: 2
 * Explanation: Remove 4, 2 and either one of the two 1s or three 3s. We can remove
 * 4 (freq=1), 2 (freq=1), and 1 (freq=2)
 * 
 * Constraints:
 * - 1 <= arr.length <= 10^5
 * - 1 <= arr[i] <= 10^9
 * - 0 <= k <= arr.length
 * 
 * LeetCode Problem: https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals
 * 
 * Follow-up Questions:
 * 
 * 1. What if you need to maximize unique integers instead of minimize?
 *    Answer: Reverse the strategy - remove elements with highest frequencies first.
 *    Sort frequencies in descending order and greedily remove from most frequent,
 *    preserving elements that appear rarely.
 * 
 * 2. How would you handle if k can be larger than array length?
 *    Answer: Add validation at the start. If k >= arr.length, return 0 since we can
 *    remove all elements. Otherwise proceed with normal algorithm.
 * 
 * 3. What if you need to return which elements to remove, not just the count?
 *    Answer: Store element values along with their frequencies. When removing, track
 *    which elements were selected and return them as part of the result.
 * 
 * 4. Can you optimize space for cases where frequencies are small?
 *    Answer: Use counting sort instead of general sorting if frequency range is small.
 *    Create frequency-of-frequency array and process in O(n + max_freq) time.
 * 
 * 5. How would you extend this to remove at most k elements (not exactly k)?
 *    Answer: Same greedy approach but stop early if we've eliminated all unique integers
 *    before using all k removals. Return 0 as soon as unique count reaches 0.
 * LeetCode Contest Rating: 1284
 **/
public class LeastUniqueNumbers {

    public static void main(String[] args) {
        int[] arr = {1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3};
        int k = 5;
        int result = new LeastUniqueNumbers().findLeastNumOfUniqueInts(arr, k);
        System.out.println("Least unique numbers after removing " + k + " elements: " + result);
    }

    /**
     * Finds minimum unique integers after removing exactly k elements using greedy approach.
     * 
     * Algorithm:
     * 1. Count frequency of each unique integer using HashMap
     * 2. Extract frequencies and sort them in ascending order
     * 3. Greedily remove integers with lowest frequencies first
     * 4. Subtract each frequency from k until k is exhausted
     * 5. Return count of remaining unique integers
     * 
     * Key insight: To minimize unique integers, prioritize removing complete groups
     * (all occurrences) of integers that appear rarely. Removing one occurrence of
     * a frequent integer doesn't reduce unique count, but removing all occurrences
     * of a rare integer does.
     * 
     * Time Complexity: O(N log N) where N is array length. Counting frequencies is O(N),
     * sorting frequencies is O(U log U) where U is unique integers (U <= N), and
     * processing sorted frequencies is O(U). Overall dominated by sorting.
     * 
     * Space Complexity: O(N) for HashMap storing frequencies and list of frequency values.
     * In worst case where all elements are unique, we store N frequencies.
     * 
     * @param arr array of integers
     * @param k number of elements to remove
     * @return minimum number of unique integers after k removals
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
