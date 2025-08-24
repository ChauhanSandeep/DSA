package frazsheet;

/**
 * Given an integer array nums, return the number of reverse pairs in the array.
 * A reverse pair is a pair (i, j) where 0 <= i < j < nums.length and nums[i] > 2 * nums[j].
 * 
 * Example 1:
 * Input: nums = [1,3,2,3,1]
 * Output: 2
 * Explanation: The reverse pairs are:
 * (1,4) --> nums[1]=3 > 2*nums[4]=2
 * (3,4) --> nums[3]=3 > 2*nums[4]=2
 * 
 * Example 2:
 * Input: nums = [2,4,3,5,1]
 * Output: 3
 * 
 * LeetCode: https://leetcode.com/problems/reverse-pairs/
 * 
 * Follow-up Questions:
 * 1. How would you handle very large arrays (e.g., 10^5 elements)?
 *    - The merge sort based solution has O(n log n) time complexity which is efficient for large inputs.
 * 2. What if we need to return the actual pairs instead of just the count?
 *    - We could modify the solution to collect and return the pairs instead of just counting them.
 * 3. How would you handle floating point numbers in the array?
 *    - The solution would work the same way as it's based on comparison operations.
 * 
 * Related Problems:
 * - Count of Range Sum (https://leetcode.com/problems/count-of-range-sum/)
 * - Count of Smaller Numbers After Self (https://leetcode.com/problems/count-of-smaller-numbers-after-self/)
 */
public class ReversePairs {
    private int count = 0;
    
    /**
     * Counts the number of reverse pairs in the array.
     * 
     * @param nums Input array
     * @return Number of reverse pairs
     */
    public int reversePairs(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }
        
        count = 0;
        mergeSortAndCount(nums, 0, nums.length - 1);
        return count;
    }
    
    /**
     * Recursively sorts the array using merge sort and counts reverse pairs.
     */
    private void mergeSortAndCount(int[] nums, int start, int end) {
        if (start >= end) {
            return;
        }
        
        int mid = start + (end - start) / 2;
        
        // Sort left and right halves
        mergeSortAndCount(nums, start, mid);
        mergeSortAndCount(nums, mid + 1, end);
        
        // Count reverse pairs between left and right halves
        countReversePairs(nums, start, mid, end);
        
        // Merge the sorted halves
        merge(nums, start, mid, end);
    }
    
    /**
     * Counts reverse pairs between the two sorted halves.
     */
    private void countReversePairs(int[] nums, int start, int mid, int end) {
        int left = start;
        int right = mid + 1;
        
        // For each element in the left half, find the first element in the right half
        // that satisfies nums[i] > 2 * nums[j]
        while (left <= mid && right <= end) {
            // Use long to prevent integer overflow
            if ((long)nums[left] > 2L * nums[right]) {
                // All elements after right in the right half will also satisfy the condition
                count += mid - left + 1;
                right++;
            } else {
                left++;
            }
        }
        
        // Sort the two halves to maintain the sorted order
        left = start;
        right = mid + 1;
        
        // Count reverse pairs in a more efficient way
        while (left <= mid) {
            while (right <= end && (long)nums[left] > 2L * nums[right]) {
                right++;
            }
            count += right - (mid + 1);
            left++;
        }
    }
    
    /**
     * Merges two sorted subarrays.
     */
    private void merge(int[] nums, int start, int mid, int end) {
        int[] temp = new int[end - start + 1];
        int i = start, j = mid + 1, k = 0;
        
        while (i <= mid && j <= end) {
            if (nums[i] <= nums[j]) {
                temp[k++] = nums[i++];
            } else {
                temp[k++] = nums[j++];
            }
        }
        
        while (i <= mid) {
            temp[k++] = nums[i++];
        }
        
        while (j <= end) {
            temp[k++] = nums[j++];
        }
        
        // Copy the merged array back to the original array
        System.arraycopy(temp, 0, nums, start, temp.length);
    }
    
    /**
     * Alternative solution using Binary Indexed Tree (Fenwick Tree).
     * This approach is more complex but has the same time complexity.
     */
    public int reversePairsBIT(int[] nums) {
        // Handle edge cases
        if (nums == null || nums.length < 2) {
            return 0;
        }
        
        // Create a sorted array of all unique elements and their doubles
        Set<Long> set = new TreeSet<>();
        for (int num : nums) {
            set.add((long)num);
            set.add(2L * num);
        }
        
        // Map each value to its rank (1-based index)
        Map<Long, Integer> rankMap = new HashMap<>();
        int rank = 1;
        for (long num : set) {
            rankMap.put(num, rank++);
        }
        
        // Use a Fenwick Tree to count elements
        FenwickTree ft = new FenwickTree(rankMap.size());
        int count = 0;
        
        // Process the array from right to left
        for (int i = nums.length - 1; i >= 0; i--) {
            long num = nums[i];
            // Find the rank of the largest number that is less than num/2.0
            // This is equivalent to finding the first number >= ceil(num/2.0) and then subtracting 1
            long target = num % 2 == 0 ? num / 2 : (num / 2) + 1;
            
            // Find the largest rank that is less than the rank of target
            Integer targetRank = rankMap.get(target);
            if (targetRank != null) {
                // Count numbers less than target
                count += ft.query(targetRank - 1);
            }
            
            // Update the Fenwick Tree with the current number's rank
            ft.update(rankMap.get(num), 1);
        }
        
        return count;
    }
    
    /**
     * Binary Indexed Tree (Fenwick Tree) implementation.
     */
    private static class FenwickTree {
        private final int[] bit;
        private final int size;
        
        public FenwickTree(int size) {
            this.size = size + 1; // 1-based indexing
            this.bit = new int[this.size];
        }
        
        /**
         * Updates the value at the given index.
         */
        public void update(int index, int delta) {
            while (index < size) {
                bit[index] += delta;
                index += index & -index; // Move to parent
            }
        }
        
        /**
         * Queries the sum from index 1 to the given index.
         */
        public int query(int index) {
            int sum = 0;
            while (index > 0) {
                sum += bit[index];
                index -= index & -index; // Move to previous
            }
            return sum;
        }
    }
    
    /**
     * Brute force solution for verification (O(n^2) time).
     * This is only suitable for small inputs.
     */
    public int reversePairsBruteForce(int[] nums) {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if ((long)nums[i] > 2L * (long)nums[j]) {
                    count++;
                }
            }
        }
        return count;
    }
}
