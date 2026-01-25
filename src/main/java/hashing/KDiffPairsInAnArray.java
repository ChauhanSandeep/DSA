package hashing;

import java.util.*;

/**
 * 532. K-diff Pairs in an Array
 *
 * Problem: Given an array of integers nums and an integer k, return the number
 * of unique k-diff pairs in the array. A k-diff pair is (nums[i], nums[j])
 * where i != j and |nums[i] - nums[j]| = k.
 *
 * Example:
 * Input: nums = [3,1,4,1,5], k = 2
 * Output: 2
 * Explanation: There are two 2-diff pairs: (1, 3) and (3, 5)
 *
 * LeetCode: https://leetcode.com/problems/k-diff-pairs-in-an-array
 *
 * Follow-up questions:
 * Q: What if k can be negative?
 * A: Since we use absolute difference, negative k is same as positive k.
 *
 * Q: How to find all pairs instead of just counting?
 * A: Store pairs in set instead of counting, return list of pairs.
 *
 * Q: Can we optimize for very large arrays?
 * A: Use HashMap approach which is already O(n), can't improve asymptotically.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class KDiffPairsInAnArray {

    /**
     * HashMap approach for O(n) solution.
     *
     * Algorithm:
     * - Count frequency of each number
     * - For k = 0: count numbers that appear more than once
     * - For k > 0: for each unique number, check if number + k exists
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int findPairs(int[] nums, int k) {
        if (k < 0) return 0;

        Map<Integer, Integer> count = new HashMap<>();

        // Count frequencies
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }

        int result = 0;

        for (int num : count.keySet()) {
            if (k == 0) {
                // For k=0, we need pairs of identical numbers
                if (count.get(num) > 1) {
                    result++;
                }
            } else {
                // For k>0, check if num + k exists
                if (count.containsKey(num + k)) {
                    result++;
                }
            }
        }

        return result;
    }

    /**
     * Two-pointer approach using sorted array.
     * More space efficient if input can be modified.
     */
    public int findPairsTwoPointer(int[] nums, int k) {
        if (k < 0) return 0;

        Arrays.sort(nums);
        int left = 0, right = 1;
        int result = 0;

        while (right < nums.length) {
            int diff = nums[right] - nums[left];

            if (diff == k && left != right) {
                result++;

                // Skip duplicates for left pointer
                int leftVal = nums[left];
                while (left < nums.length && nums[left] == leftVal) {
                    left++;
                }

                // Skip duplicates for right pointer
                int rightVal = nums[right];
                while (right < nums.length && nums[right] == rightVal) {
                    right++;
                }

                // Ensure right > left
                if (right <= left) {
                    right = left + 1;
                }
            } else if (diff < k) {
                right++;
            } else {
                left++;
                if (right <= left) {
                    right = left + 1;
                }
            }
        }

        return result;
    }

    /**
     * HashSet approach for clarity and uniqueness handling.
     * Uses set to naturally handle unique pairs.
     */
    public int findPairsHashSet(int[] nums, int k) {
        if (k < 0) return 0;

        Set<Integer> numSet = new HashSet<>();
        Set<Integer> pairs = new HashSet<>();

        for (int num : nums) {
            // Check if we can form a pair with previous numbers
            if (numSet.contains(num - k)) {
                pairs.add(num - k);
            }
            if (numSet.contains(num + k)) {
                pairs.add(num);
            }

            numSet.add(num);
        }

        return pairs.size();
    }

    /**
     * Brute force approach for small arrays or verification.
     * Checks all possible pairs explicitly.
     */
    public int findPairsBruteForce(int[] nums, int k) {
        if (k < 0) return 0;

        Set<String> uniquePairs = new HashSet<>();

        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (Math.abs(nums[i] - nums[j]) == k) {
                    int smaller = Math.min(nums[i], nums[j]);
                    int larger = Math.max(nums[i], nums[j]);
                    uniquePairs.add(smaller + "," + larger);
                }
            }
        }

        return uniquePairs.size();
    }

    /**
     * Stream-based functional approach.
     * Demonstrates functional programming style solution.
     */
    public int findPairsStream(int[] nums, int k) {
        if (k < 0) return 0;

        Map<Integer, Long> count = Arrays.stream(nums)
            .boxed()
            .collect(java.util.stream.Collectors.groupingBy(
                java.util.function.Function.identity(),
                java.util.stream.Collectors.counting()
            ));

        return (int) count.keySet().stream()
            .mapToLong(num -> {
                if (k == 0) {
                    return count.get(num) > 1 ? 1 : 0;
                } else {
                    return count.containsKey(num + k) ? 1 : 0;
                }
            })
            .sum();
    }

    /**
     * Returns all unique k-diff pairs instead of just counting.
     * Extension that provides the actual pairs.
     */
    public List<int[]> findAllPairs(int[] nums, int k) {
        if (k < 0) return new ArrayList<>();

        Map<Integer, Integer> count = new HashMap<>();
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }

        List<int[]> result = new ArrayList<>();

        for (int num : count.keySet()) {
            if (k == 0) {
                if (count.get(num) > 1) {
                    result.add(new int[]{num, num});
                }
            } else {
                if (count.containsKey(num + k)) {
                    result.add(new int[]{num, num + k});
                }
            }
        }

        return result;
    }

    /**
     * Binary search approach for each element.
     * Alternative method using binary search for target finding.
     */
    public int findPairsBinarySearch(int[] nums, int k) {
        if (k < 0) return 0;

        Set<Integer> unique = new HashSet<>();
        for (int num : nums) {
            unique.add(num);
        }

        List<Integer> sortedNums = new ArrayList<>(unique);
        Collections.sort(sortedNums);

        int result = 0;

        for (int num : sortedNums) {
            if (k == 0) {
                // Count frequency in original array
                long freq = Arrays.stream(nums).filter(x -> x == num).count();
                if (freq > 1) {
                    result++;
                }
            } else {
                // Binary search for num + k
                if (Collections.binarySearch(sortedNums, num + k) >= 0) {
                    result++;
                }
            }
        }

        return result;
    }

    /**
     * Sliding window approach on sorted array.
     * Uses sliding window technique after sorting.
     */
    public int findPairsSlidingWindow(int[] nums, int k) {
        if (k < 0) return 0;

        Arrays.sort(nums);
        Set<Integer> seen = new HashSet<>();
        int result = 0;

        for (int i = 0; i < nums.length - 1; i++) {
            // Skip duplicates
            if (seen.contains(nums[i])) {
                continue;
            }

            seen.add(nums[i]);

            // Look for nums[i] + k in remaining array
            int target = nums[i] + k;

            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] == target) {
                    result++;
                    break;
                } else if (nums[j] > target) {
                    break; // Array is sorted, no more matches
                }
            }
        }

        return result;
    }

    /**
     * Optimized two-pass approach.
     * First pass builds frequency map, second pass counts pairs.
     */
    public int findPairsOptimized(int[] nums, int k) {
        if (k < 0) return 0;

        Map<Integer, Integer> freq = new HashMap<>();
        Set<Integer> processed = new HashSet<>();

        // Build frequency map
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }

        int result = 0;

        // Count pairs
        for (int num : freq.keySet()) {
            if (processed.contains(num)) continue;

            if (k == 0) {
                if (freq.get(num) >= 2) {
                    result++;
                }
            } else {
                if (freq.containsKey(num + k)) {
                    result++;
                    processed.add(num + k); // Avoid counting reverse pair
                }
            }

            processed.add(num);
        }

        return result;
    }
}