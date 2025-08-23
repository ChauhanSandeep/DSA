package Array;

/**
 * Non Decreasing Array
 * 
 * Problem: Determine if array can be made non-decreasing by modifying at most one element.
 * 
 * Example: nums = [4,2,3] -> Output: true
 * Change 4 to 1 or change 2 to 4: [1,2,3] or [4,4,3] (then change 3 to 4).
 * Actually, change 4 to something ≤ 2: [1,2,3] works.
 * 
 * LeetCode: https://leetcode.com/problems/non-decreasing-array
 * 
 * Follow-up Questions:
 * - What if we can modify at most k elements? (Use dynamic programming)
 * - How to find the actual element to modify? (Track modification during scan)
 * - What if modification has different costs? (Choose optimal modification)
 */
public class NonDecreasingArray {

    /**
     * Checks if array can be made non-decreasing with at most one modification.
     * 
     * Algorithm:
     * 1. Scan array to find violations (nums[i] > nums[i+1])
     * 2. If more than one violation found, return false
     * 3. If one violation found, check if we can fix it by:
     *    a) Modifying nums[i] to be ≤ nums[i+1], or
     *    b) Modifying nums[i+1] to be ≥ nums[i]
     * 4. Consider constraints from neighboring elements
     * 
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     * 
     * @param nums input array
     * @return true if can be made non-decreasing with ≤1 modification
     */
    public boolean checkPossibility(int[] nums) {
        int n = nums.length;
        int violations = 0;

        for (int i = 0; i < n - 1; i++) {
            if (nums[i] > nums[i + 1]) {
                violations++;

                // More than one violation - impossible
                if (violations > 1) return false;

                // Try to fix the violation
                // Option 1: Modify nums[i] to be <= nums[i+1]
                // Check if this doesn't violate constraint with nums[i-1]
                if (i == 0 || nums[i - 1] <= nums[i + 1]) {
                    nums[i] = nums[i + 1]; // Modify nums[i]
                } else {
                    // Option 2: Modify nums[i+1] to be >= nums[i]
                    // Check if this doesn't violate constraint with nums[i+2]
                    if (i + 2 >= n || nums[i] <= nums[i + 2]) {
                        nums[i + 1] = nums[i]; // Modify nums[i+1]
                    } else {
                        return false; // Can't fix this violation
                    }
                }
            }
        }

        return true;
    }

    /**
     * Alternative approach without modifying input array
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public boolean checkPossibilityNoModify(int[] nums) {
        int n = nums.length;
        int violations = 0;
        int violationIndex = -1;

        // Find violations
        for (int i = 0; i < n - 1; i++) {
            if (nums[i] > nums[i + 1]) {
                violations++;
                if (violations > 1) return false;
                violationIndex = i;
            }
        }

        // No violations found
        if (violations == 0) return true;

        // Check if we can fix the single violation
        int i = violationIndex;

        // Can we modify nums[i] to fix violation?
        boolean canModifyLeft = (i == 0) || (nums[i - 1] <= nums[i + 1]);

        // Can we modify nums[i+1] to fix violation?  
        boolean canModifyRight = (i + 2 >= n) || (nums[i] <= nums[i + 2]);

        return canModifyLeft || canModifyRight;
    }

    /**
     * Detailed approach that tracks what modifications are made
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public boolean checkPossibilityDetailed(int[] nums) {
        int n = nums.length;
        int modificationCount = 0;

        for (int i = 0; i < n - 1; i++) {
            if (nums[i] <= nums[i + 1]) continue;

            // Found violation: nums[i] > nums[i+1]
            modificationCount++;

            if (modificationCount > 1) return false;

            // Decide how to fix: modify nums[i] or nums[i+1]
            if (i == 0) {
                // At beginning, safe to modify nums[i]
                nums[i] = nums[i + 1];
            } else if (i == n - 2) {
                // At end, safe to modify nums[i+1]
                nums[i + 1] = nums[i];
            } else {
                // In middle, need to check both options
                if (nums[i - 1] <= nums[i + 1]) {
                    // Can modify nums[i] without breaking left constraint
                    nums[i] = nums[i + 1];
                } else if (nums[i] <= nums[i + 2]) {
                    // Can modify nums[i+1] without breaking right constraint
                    nums[i + 1] = nums[i];
                } else {
                    // Neither modification works
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Clean implementation focusing on the core logic
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public boolean checkPossibilityClean(int[] nums) {
        int count = 0;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] > nums[i]) {
                count++;
                if (count > 1) return false;

                // Fix violation by choosing appropriate modification
                if (i == 1 || nums[i - 2] <= nums[i]) {
                    nums[i - 1] = nums[i];
                } else {
                    nums[i] = nums[i - 1];
                }
            }
        }

        return true;
    }
}
