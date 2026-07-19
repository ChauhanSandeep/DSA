package dynamicprogramming.sequence;

import java.util.*;

/**
 * Problem: Maximum Length of Repeated Subarray
 *
 * Return the length of the longest contiguous subarray that appears in both arrays. Mismatches reset the current contiguous match.
 *
 * Leetcode: https://leetcode.com/problems/maximum-length-of-repeated-subarray/ (Medium)
 * Rating:   not available (not a contest problem)
 * Pattern:  Dynamic programming | String matching | Longest common suffix
 *
 * Example:
 *   Input:  nums1 = [1,2,3,2,1], nums2 = [3,2,1,4,7]
 *   Output: 3
 *   Why:    [3,2,1] appears contiguously in both arrays.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Longest Common Subsequence (1143).
 */
public class MaximumLengthOfRepeatedSubarray {

    public static void main(String[] args) {
        MaximumLengthOfRepeatedSubarray solution = new MaximumLengthOfRepeatedSubarray();
        int[][] nums1Cases = { {1, 2, 3, 2, 1}, {0, 0, 0}, {1, 2, 3} };
        int[][] nums2Cases = { {3, 2, 1, 4, 7}, {0, 0}, {4, 5, 6} };
        int[] expected = {3, 2, 0};
        for (int i = 0; i < nums1Cases.length; i++) {
            int got = solution.findLength(nums1Cases[i], nums2Cases[i]);
            System.out.printf("nums1=%s nums2=%s -> %d  expected=%d%n", Arrays.toString(nums1Cases[i]), Arrays.toString(nums2Cases[i]), got, expected[i]);
        }
    }


        /**
     * Intuition: dp[i][j] is the length of the common contiguous suffix ending at nums1[i - 1] and nums2[j - 1]. Equal values extend the diagonal suffix; unequal values break contiguity and reset the cell to zero.
     *
     * Algorithm:
     *   1. Create dp[m + 1][n + 1].
     *   2. Iterate i from 1..m and j from 1..n.
     *   3. On equal values, set dp[i][j] = dp[i - 1][j - 1] + 1.
     *   4. Otherwise set dp[i][j] = 0.
     *   5. Track the maximum cell.
     *
     * Time:  O(m * n) - every pair is compared once.
     * Space: O(m * n) - stores the table.
     *
     * @param nums1 first array
     * @param nums2 second array
     * @return longest repeated subarray length
     */
public int findLength(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int[][] dp = new int[m + 1][n + 1];
        int maxLength = 0;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (nums1[i - 1] == nums2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    maxLength = Math.max(maxLength, dp[i][j]);
                } else {
                    dp[i][j] = 0;
                }
            }
        }

        return maxLength;
    }

    /**
     * Space-optimized DP using 1D array.
     * Since we only need the previous row, we can optimize space.
     */
    public int findLengthOptimized(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int[] dp = new int[n + 1];
        int maxLength = 0;

        for (int i = 1; i <= m; i++) {
            // Process from right to left to avoid overwriting needed values
            for (int j = n; j >= 1; j--) {
                if (nums1[i - 1] == nums2[j - 1]) {
                    dp[j] = dp[j - 1] + 1;
                    maxLength = Math.max(maxLength, dp[j]);
                } else {
                    dp[j] = 0;
                }
            }
        }

        return maxLength;
    }

    /**
     * Rolling hash approach with binary search.
     * More complex but better for very large arrays.
     */
    public int findLengthRollingHash(int[] nums1, int[] nums2) {
        int left = 0, right = Math.min(nums1.length, nums2.length);
        int result = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (hasCommonSubarray(nums1, nums2, mid)) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    // Check if there exists common subarray of given length using rolling hash
    /** Checks whether a fixed-length common subarray exists. */
    private boolean hasCommonSubarray(int[] nums1, int[] nums2, int length) {
        if (length == 0) return true;

        int BASE = 113;
        int MOD = 1000000007;

        // Calculate base^length for rolling hash
        long basePower = 1;
        for (int i = 0; i < length; i++) {
            basePower = (basePower * BASE) % MOD;
        }

        // Get all hashes of subarrays of given length from nums1
        Set<Long> hashes1 = getRollingHashes(nums1, length, BASE, MOD, basePower);

        // Check if any subarray from nums2 matches
        Set<Long> hashes2 = getRollingHashes(nums2, length, BASE, MOD, basePower);

        for (long hash : hashes1) {
            if (hashes2.contains(hash)) {
                return true;
            }
        }

        return false;
    }

    // Get all rolling hashes of subarrays of given length
    /** Computes rolling hashes for fixed-length windows. */
    private Set<Long> getRollingHashes(int[] nums, int length, int BASE, int MOD, long basePower) {
        Set<Long> hashes = new HashSet<>();
        if (length > nums.length) return hashes;

        long hash = 0;

        // Calculate hash of first window
        for (int i = 0; i < length; i++) {
            hash = (hash * BASE + nums[i]) % MOD;
        }
        hashes.add(hash);

        // Rolling hash for remaining windows
        for (int i = length; i < nums.length; i++) {
            hash = (hash - (nums[i - length] * basePower) % MOD + MOD) % MOD;
            hash = (hash * BASE + nums[i]) % MOD;
            hashes.add(hash);
        }

        return hashes;
    }

    /**
     * Brute force approach checking all possible subarrays.
     * Simple but inefficient - for verification and small inputs.
     */
    public int findLengthBruteForce(int[] nums1, int[] nums2) {
        int maxLength = 0;

        for (int i = 0; i < nums1.length; i++) {
            for (int j = 0; j < nums2.length; j++) {
                int length = 0;

                // Extend subarray as long as elements match
                while (i + length < nums1.length &&
                       j + length < nums2.length &&
                       nums1[i + length] == nums2[j + length]) {
                    length++;
                }

                maxLength = Math.max(maxLength, length);
            }
        }

        return maxLength;
    }

    /**
     * Suffix array approach for advanced string matching.
     * Efficient for multiple queries on same arrays.
     */
    public int findLengthSuffixArray(int[] nums1, int[] nums2) {
        // Convert arrays to combined array with separator
        List<Integer> combined = new ArrayList<>();
        for (int num : nums1) {
            combined.add(num);
        }
        combined.add(-1); // Separator (assuming all elements are non-negative)
        for (int num : nums2) {
            combined.add(num);
        }

        // Build suffix array and LCP array
        int[] suffixArray = buildSuffixArray(combined);
        int[] lcp = buildLCPArray(combined, suffixArray);

        // Find maximum LCP between suffixes from different arrays
        int maxLength = 0;
        int separator = nums1.length;

        for (int i = 0; i < lcp.length; i++) {
            int pos1 = suffixArray[i];
            int pos2 = suffixArray[i + 1];

            // Check if suffixes are from different original arrays
            if ((pos1 <= separator) != (pos2 <= separator)) {
                maxLength = Math.max(maxLength, lcp[i]);
            }
        }

        return maxLength;
    }

    // Build suffix array using simple O(n^2 log n) algorithm
    /** Builds a simple suffix array. */
    private int[] buildSuffixArray(List<Integer> arr) {
        int n = arr.size();
        Integer[] suffixes = new Integer[n];
        for (int i = 0; i < n; i++) {
            suffixes[i] = i;
        }

        Arrays.sort(suffixes, (a, b) -> {
            for (int i = 0; i < n; i++) {
                if (a + i >= n && b + i >= n) return 0;
                if (a + i >= n) return -1;
                if (b + i >= n) return 1;

                int cmp = Integer.compare(arr.get(a + i), arr.get(b + i));
                if (cmp != 0) return cmp;
            }
            return 0;
        });

        return Arrays.stream(suffixes).mapToInt(Integer::intValue).toArray();
    }

    // Build Longest Common Prefix array
    /** Builds adjacent longest-common-prefix lengths. */
    private int[] buildLCPArray(List<Integer> arr, int[] suffixArray) {
        int n = suffixArray.length;
        int[] lcp = new int[n - 1];

        for (int i = 0; i < n - 1; i++) {
            int pos1 = suffixArray[i];
            int pos2 = suffixArray[i + 1];

            int commonLen = 0;
            while (pos1 + commonLen < arr.size() &&
                   pos2 + commonLen < arr.size() &&
                   arr.get(pos1 + commonLen).equals(arr.get(pos2 + commonLen))) {
                commonLen++;
            }

            lcp[i] = commonLen;
        }

        return lcp;
    }

    /**
     * Returns the actual longest common subarray instead of just length.
     * Extension that provides the subarray elements.
     */
    public int[] findLongestSubarray(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int[][] dp = new int[m + 1][n + 1];
        int maxLength = 0;
        int endPosI = 0, endPosJ = 0;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (nums1[i - 1] == nums2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    if (dp[i][j] > maxLength) {
                        maxLength = dp[i][j];
                        endPosI = i - 1;
                        endPosJ = j - 1;
                    }
                } else {
                    dp[i][j] = 0;
                }
            }
        }

        // Extract the actual subarray
        int[] result = new int[maxLength];
        for (int i = 0; i < maxLength; i++) {
            result[i] = nums1[endPosI - maxLength + 1 + i];
        }

        return result;
    }

    /**
     * Multi-hash rolling hash for better collision resistance.
     * Uses multiple hash functions to reduce false positives.
     */
    public int findLengthMultiHash(int[] nums1, int[] nums2) {
        int left = 0, right = Math.min(nums1.length, nums2.length);
        int result = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (hasCommonSubarrayMultiHash(nums1, nums2, mid)) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    // Check using multiple hash functions
    /** Checks fixed-length common subarrays with multiple hashes. */
    private boolean hasCommonSubarrayMultiHash(int[] nums1, int[] nums2, int length) {
        if (length == 0) return true;

        int[] BASES = {31, 37, 41};
        int MOD = 1000000007;

        // Get multi-hashes from nums1
        Set<List<Long>> hashes1 = getMultiRollingHashes(nums1, length, BASES, MOD);
        Set<List<Long>> hashes2 = getMultiRollingHashes(nums2, length, BASES, MOD);

        // Check for intersection
        for (List<Long> hash : hashes1) {
            if (hashes2.contains(hash)) {
                return true;
            }
        }

        return false;
    }

    // Get rolling hashes using multiple hash functions
    /** Computes multiple rolling hashes for fixed-length windows. */
    private Set<List<Long>> getMultiRollingHashes(int[] nums, int length, int[] BASES, int MOD) {
        Set<List<Long>> result = new HashSet<>();
        if (length > nums.length) return result;

        int numHashes = BASES.length;
        long[] hashes = new long[numHashes];
        long[] basePowers = new long[numHashes];

        // Calculate base powers
        for (int h = 0; h < numHashes; h++) {
            basePowers[h] = 1;
            for (int i = 0; i < length; i++) {
                basePowers[h] = (basePowers[h] * BASES[h]) % MOD;
            }
        }

        // Calculate initial hashes
        for (int i = 0; i < length; i++) {
            for (int h = 0; h < numHashes; h++) {
                hashes[h] = (hashes[h] * BASES[h] + nums[i]) % MOD;
            }
        }
        result.add(Arrays.stream(hashes).boxed().collect(java.util.stream.Collectors.toList()));

        // Rolling hash
        for (int i = length; i < nums.length; i++) {
            for (int h = 0; h < numHashes; h++) {
                hashes[h] = (hashes[h] - (nums[i - length] * basePowers[h]) % MOD + MOD) % MOD;
                hashes[h] = (hashes[h] * BASES[h] + nums[i]) % MOD;
            }
            result.add(Arrays.stream(hashes).boxed().collect(java.util.stream.Collectors.toList()));
        }

        return result;
    }

    /**
     * Parallel processing approach for very large arrays.
     * Divides the search space across multiple threads.
     */
    public int findLengthParallel(int[] nums1, int[] nums2) {
        if (nums1.length * nums2.length < 10000) {
            return findLength(nums1, nums2); // Use sequential for small inputs
        }

        // Split nums1 into chunks and process in parallel
        int numThreads = Runtime.getRuntime().availableProcessors();
        int chunkSize = nums1.length / numThreads;

        return java.util.stream.IntStream.range(0, numThreads)
            .parallel()
            .map(threadId -> {
                int start = threadId * chunkSize;
                int end = (threadId == numThreads - 1) ? nums1.length : (threadId + 1) * chunkSize;

                int maxLen = 0;
                for (int i = start; i < end; i++) {
                    for (int j = 0; j < nums2.length; j++) {
                        int length = 0;
                        while (i + length < nums1.length &&
                               j + length < nums2.length &&
                               nums1[i + length] == nums2[j + length]) {
                            length++;
                        }
                        maxLen = Math.max(maxLen, length);
                    }
                }
                return maxLen;
            })
            .max()
            .orElse(0);
    }
}