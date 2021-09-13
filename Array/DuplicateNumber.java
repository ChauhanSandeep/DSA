package Array;

/**
 * Find duplicate number in array containing elements from 1 - n(duplicate occures 1 or more times)
 * You must solve the problem without modifying the array nums and uses only constant extra space.
 * https://leetcode.com/problems/find-the-duplicate-number/
 */
public class DuplicateNumber {

    public static void main(String[] args) {
        int[] nums = {1,3,4,2,2};
        int result = new DuplicateNumber().findDuplicate(nums);
        System.out.println(result);
    }

    /**
     * If count of numbers less than or equal to a current number are more than current number, then there is duplicate present in lower range of numbers
     * Else duplicate is present in higher range of numbers.
     * Time complexity O(nLogn) -> log n for binary search, n for finding count/occurence
     */
    public int findDuplicate(int[] nums) {
        int low = 1;
        int high = nums.length - 1;

        while (low <= high) {
            int curr = (low + high) / 2;
            int count = 0;
            int occurence = 0;
            for (int num : nums) {
                if(num == curr) {
                    occurence++;
                    if(occurence > 1) return num;
                }
                if (num <= curr) {
                    count++;
                    if(count > curr) {
                        break;
                    }
                }
            }

            if (count > curr) {
                high = curr - 1;
            } else {
                low = curr + 1;
            }
        }
        return -1;
    }

}
