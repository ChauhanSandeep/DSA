package heaps;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/**
 * Problem: Top K Frequent Elements
 *
 * Given an integer array nums, return k values with the highest frequencies. The
 * answer may be returned in any order, and the original problem guarantees the
 * selected set is unique.
 *
 * Leetcode: https://leetcode.com/problems/top-k-frequent-elements/ (Medium)
 * Rating:   acceptance 66.9% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Heap | Frequency map | Bucket sort alternative
 *
 * Example:
 *   Input:  nums = [1,1,1,2,2,3], k = 2
 *   Output: [1,2]
 *   Why:    1 appears 3 times and 2 appears 2 times, more than every other value.
 *
 * Follow-ups:
 *   1. Can you do better than O(n log k)?
 *      Bucket sort by frequency gives O(n) time when values can be grouped by counts.
 *   2. How should ties be ordered?
 *      Add a tie-breaker to the heap comparator or sort the final result.
 *   3. What if numbers arrive as a stream?
 *      Maintain counts plus a heap with lazy updates or use approximate heavy hitters.
 *   4. What if k is close to the number of unique values?
 *      A max heap or sorting all entries can be simpler with similar cost.
 *
 * Related: Top K Frequent Words (692), Sort Characters By Frequency (451).
 */

public class TopKFrequentElements {

    public static void main(String[] args) {
        TopKFrequentElements solver = new TopKFrequentElements();
        int[][] inputs = { {1, 1, 1, 2, 2, 3}, {5} };
        int[] kValues = {1, 1};
        int[][] expected = { {1}, {5} };

        for (int i = 0; i < inputs.length; i++) {
            int[] got = solver.topKFrequent(inputs[i], kValues[i]);
            System.out.printf("nums=%s k=%d -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), kValues[i],
                Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }

        /**
     * Intuition: after counting frequencies, the top k elements are the k largest
     * map entries by count. A min heap of size k keeps the current winners, and
     * its root is the least frequent winner to evict when a better entry arrives.
     *
     * Algorithm:
     *   1. Return an empty array for null, empty, or non-positive k input.
     *   2. Count each value's frequency in a HashMap.
     *   3. Offer every map entry into a min heap ordered by frequency.
     *   4. Poll whenever the heap grows past k, then extract the remaining keys.
     *
     * Time:  O(n log k) - counting is linear and each unique value updates the heap.
     * Space: O(n + k) - the map stores frequencies and the heap stores k entries.
     *
     * @param nums input values
     * @param k number of frequent values to return
     * @return k most frequent values in heap extraction order
     */

    public int[] topKFrequent(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }

        // Count frequency of each element
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        // Min heap based on frequency - keeps k most frequent elements
        PriorityQueue<Map.Entry<Integer, Integer>> minHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(a.getValue(), b.getValue())
        );

        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            minHeap.offer(entry);

            // Keep only k most frequent elements
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        // Extract elements from heap
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll().getKey();
        }

        return result;
    }

    /**
     * Bucket sort approach for O(n) time complexity.
     * Uses the fact that maximum frequency is at most n.
     *
     * Steps:
     * 1. Count frequency of each element
     * 2. Create buckets for each possible frequency (0 to n)
     * 3. Add elements to buckets based on their frequency
     * 4. Collect k most frequent elements from highest frequency buckets
     *
     * Time Complexity: O(n) because we are using bucket sort
     * Space Complexity: O(n)
     */
    public int[] topKFrequentBucketSort(int[] nums, int k) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        // Create buckets for each possible frequency (0 to n)
        List<Integer>[] freqBucket = new ArrayList[nums.length + 1];

        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            int key = entry.getKey();
            int frequency = entry.getValue();
            if (freqBucket[frequency] == null) {
                freqBucket[frequency] = new ArrayList<>();
            }
            freqBucket[frequency].add(key);
        }

        // Collect k most frequent elements from highest frequency buckets
        int[] result = new int[k];
        int index = 0;

        for (int freq = freqBucket.length - 1; freq >= 0 && index < k; freq--) {
            if (freqBucket[freq] != null) {
                for (int num : freqBucket[freq]) {
                    if (index < k) {
                        result[index++] = num;
                    }
                }
            }
        }

        return result;
    }
}