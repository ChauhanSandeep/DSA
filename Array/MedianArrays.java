package Array;

/**
 * Find the median of two sorted arrays.
 *
 * https://leetcode.com/problems/median-of-two-sorted-arrays/
 */
public class MedianArrays {

    public static void main(String[] args) {
        int[] arr1 = {1, 2};
        int[] arr2 = {3, 4};

        System.out.println("Two Pointer Median: " + findMedianTwoPointer(arr1, arr2));
        System.out.println("Binary Search Median: " + findMedianBinarySearch(arr1, arr2));
    }

    /**
     * Approach 1: Two-pointer Merge Approach (O(n + m))
     * This method merges two sorted arrays while tracking the median position.
     */
    static double findMedianTwoPointer(int[] nums1, int[] nums2) {
        int n1 = nums1.length, n2 = nums2.length;
        int totalLen = n1 + n2;
        int mid = totalLen / 2;
        int i = 0, j = 0, count = 0;
        double curr = -1, prev = -1;

        while (count <= mid) {
            prev = curr;
            if (i < n1 && (j >= n2 || nums1[i] < nums2[j])) {
                curr = nums1[i++];
            } else {
                curr = nums2[j++];
            }
            count++;
        }

        return (totalLen % 2 == 0) ? (curr + prev) / 2.0 : curr;
    }

    /**
     * Approach 2: Binary Search on the Smaller Array (O(log(min(n, m))))
     * Uses partitioning to find the correct split between the two arrays.
     */
    public static double findMedianBinarySearch(int[] nums1, int[] nums2) {
        // Ensure nums1 is the smaller array for optimal binary search
        if (nums1.length > nums2.length) {
            int[] temp = nums1;
            nums1 = nums2;
            nums2 = temp;
        }

        int n1 = nums1.length, n2 = nums2.length;
        int low = 0, high = n1;

        while (low <= high) {
            int cut1 = (low + high) / 2;
            int cut2 = (n1 + n2 + 1) / 2 - cut1;

            int left1 = (cut1 == 0) ? Integer.MIN_VALUE : nums1[cut1 - 1];
            int left2 = (cut2 == 0) ? Integer.MIN_VALUE : nums2[cut2 - 1];

            int right1 = (cut1 == n1) ? Integer.MAX_VALUE : nums1[cut1];
            int right2 = (cut2 == n2) ? Integer.MAX_VALUE : nums2[cut2];

            if (left1 <= right2 && left2 <= right1) { // Valid partition found
                if ((n1 + n2) % 2 == 0)
                    return (Math.max(left1, left2) + Math.min(right1, right2)) / 2.0;
                else
                    return Math.max(left1, left2);
            } 
            else if (left1 > right2) { // Need to shift partition left
                high = cut1 - 1;
            } 
            else { // Need to shift partition right
                low = cut1 + 1;
            }
        }

        return 0.0; // Edge case (should not reach here)
    }
}
