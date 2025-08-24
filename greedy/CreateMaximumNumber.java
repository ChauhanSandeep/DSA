package greedy;

/**
 * Given two arrays of integers nums1 and nums2, and an integer k, create the maximum number of length k from digits of the two arrays.
 * The relative order of the digits from the same array must be preserved.
 * 
 * Example 1:
 * Input: nums1 = [3,4,6,5], nums2 = [9,1,2,5,8,3], k = 5
 * Output: [9,8,6,5,3]
 * Explanation: The number 98653 is the largest possible number that can be formed by selecting one digit from each array.
 * 
 * Example 2:
 * Input: nums1 = [6,7], nums2 = [6,0,4], k = 5
 * Output: [6,7,6,0,4]
 * 
 * LeetCode: https://leetcode.com/problems/create-maximum-number/
 * 
 * Follow-up Questions:
 * 1. How would you handle negative numbers in the input arrays?
 *    - The problem constraints specify digits 0-9, but if negatives were allowed, we'd need to modify the merge strategy to handle sign comparison.
 * 2. What if k is larger than the sum of lengths of both arrays?
 *    - According to constraints, k is guaranteed to be valid (0 ≤ k ≤ m + n).
 * 3. How would you optimize if one array is significantly larger than the other?
 *    - We already optimize by only considering valid splits between the two arrays.
 * 
 * Related Problems:
 * - Remove K Digits (https://leetcode.com/problems/remove-k-digits/)
 * - Maximum Swap (https://leetcode.com/problems/maximum-swap/)
 */
public class CreateMaximumNumber {
    /**
     * Main method to create the maximum number by combining elements from two arrays.
     * 
     * @param nums1 First array of digits
     * @param nums2 Second array of digits
     * @param k Length of the resulting maximum number
     * @return The maximum number as an array of digits
     */
    public int[] maxNumber(int[] nums1, int[] nums2, int k) {
        int m = nums1.length, n = nums2.length;
        int[] result = new int[k];
        
        // Try all possible ways to split k between nums1 and nums2
        for (int i = Math.max(0, k - n); i <= Math.min(k, m); i++) {
            int[] candidate = merge(
                maxArray(nums1, i),
                maxArray(nums2, k - i)
            );
            if (greater(candidate, 0, result, 0)) {
                result = candidate;
            }
        }
        return result;
    }
    
    // Helper method to create the maximum number of length k from a single array
    private int[] maxArray(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[k];
        int len = 0; // current length of result
        
        for (int i = 0; i < n; i++) {
            // While we can remove elements from result to make a larger number
            while (len > 0 && len + n - i > k && result[len - 1] < nums[i]) {
                len--;
            }
            if (len < k) {
                result[len++] = nums[i];
            }
        }
        return result;
    }
    
    // Merge two arrays to form the largest possible number
    private int[] merge(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int[] result = new int[m + n];
        int i = 0, j = 0, k = 0;
        
        while (i < m && j < n) {
            // Compare which array has the larger number
            result[k++] = greater(nums1, i, nums2, j) ? nums1[i++] : nums2[j++];
        }
        
        // Add remaining elements
        while (i < m) result[k++] = nums1[i++];
        while (j < n) result[k++] = nums2[j++];
        
        return result;
    }
    
    // Compare two arrays to determine which one represents a larger number
    private boolean greater(int[] nums1, int i, int[] nums2, int j) {
        while (i < nums1.length && j < nums2.length && nums1[i] == nums2[j]) {
            i++;
            j++;
        }
        return j == nums2.length || (i < nums1.length && nums1[i] > nums2[j]);
    }
}
