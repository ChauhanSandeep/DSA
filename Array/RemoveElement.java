package Array;

/**
 * Remove Element
 * 
 * Problem: Remove all occurrences of given value from array in-place.
 * Return new length. Order of remaining elements can be changed.
 * 
 * Example: nums = [3,2,2,3], val = 3 -> Output: 2, nums = [2,2,_,_]
 * Remove all occurrences of 3, resulting in [2,2] with length 2.
 * 
 * LeetCode: https://leetcode.com/problems/remove-element
 * 
 * Follow-up Questions:
 * - What if we need to maintain original order? (Use two-pointer technique)
 * - How to remove multiple different values? (Generalize condition)
 * - What if removal is expensive but assignment is cheap? (Use swap technique)
 */
public class RemoveElement {

    /**
     * Removes all occurrences of val using two-pointer technique.
     * 
     * Algorithm:
     * 1. Use slow pointer for next position to place non-val element
     * 2. Use fast pointer to scan through array
     * 3. When non-val element found, place it at slow pointer position
     * 4. Return slow pointer as new array length
     * 
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     * 
     * @param nums input array
     * @param val value to remove
     * @return new length after removing val
     */
    public int removeElement(int[] nums, int val) {
        int slow = 0; // Position for next non-val element

        for (int fast = 0; fast < nums.length; fast++) {
            // Keep element if it's not equal to val
            if (nums[fast] != val) {
                nums[slow] = nums[fast];
                slow++;
            }
        }

        return slow;
    }

    /**
     * Optimized approach using swap technique (fewer writes when val is rare)
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int removeElementSwap(int[] nums, int val) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            if (nums[left] == val) {
                // Swap with element from right
                nums[left] = nums[right];
                right--;
            } else {
                left++;
            }
        }

        return left;
    }

    /**
     * Alternative implementation maintaining order
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int removeElementMaintainOrder(int[] nums, int val) {
        int writeIndex = 0;

        for (int readIndex = 0; readIndex < nums.length; readIndex++) {
            if (nums[readIndex] != val) {
                if (writeIndex != readIndex) {
                    nums[writeIndex] = nums[readIndex];
                }
                writeIndex++;
            }
        }

        return writeIndex;
    }

    /**
     * Generic version that removes elements based on predicate
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int removeElementIf(int[] nums, Predicate<Integer> shouldRemove) {
        int writeIndex = 0;

        for (int i = 0; i < nums.length; i++) {
            if (!shouldRemove.test(nums[i])) {
                nums[writeIndex] = nums[i];
                writeIndex++;
            }
        }

        return writeIndex;
    }

    /**
     * Version that returns actual remaining elements
     * Time Complexity: O(n), Space Complexity: O(n) for result
     */
    public int[] removeElementReturnArray(int[] nums, int val) {
        List<Integer> remaining = new ArrayList<>();

        for (int num : nums) {
            if (num != val) {
                remaining.add(num);
            }
        }

        return remaining.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Counting approach (useful when you just need the count)
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int countRemainingElements(int[] nums, int val) {
        int count = 0;

        for (int num : nums) {
            if (num != val) {
                count++;
            }
        }

        return count;
    }

    /**
     * Stream-based approach for functional programming
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int removeElementStream(int[] nums, int val) {
        int[] remaining = Arrays.stream(nums)
                               .filter(x -> x != val)
                               .toArray();

        // Copy back to original array
        System.arraycopy(remaining, 0, nums, 0, remaining.length);

        return remaining.length;
    }

    /**
     * In-place removal with explicit gap handling
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int removeElementExplicitGaps(int[] nums, int val) {
        int gapStart = -1; // Start of current gap
        int writePos = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == val) {
                // Start or extend gap
                if (gapStart == -1) {
                    gapStart = i;
                }
            } else {
                // Non-val element found
                if (gapStart != -1) {
                    // Fill gap
                    nums[gapStart] = nums[i];
                    gapStart++;
                }
                writePos = (gapStart == -1) ? i + 1 : gapStart;
            }
        }

        return writePos;
    }

    /**
     * Helper method to verify removal correctness
     */
    public boolean verifyRemoval(int[] nums, int newLength, int val) {
        // Check that first newLength elements don't contain val
        for (int i = 0; i < newLength; i++) {
            if (nums[i] == val) {
                return false;
            }
        }
        return true;
    }
}
