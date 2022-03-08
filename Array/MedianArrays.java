package Array;

/**
 * Find median of two sorted arrays
 *
 * https://leetcode.com/problems/median-of-two-sorted-arrays/
 */
public class MedianArrays {

    public static void main(String[] args) {
        int[] arr1 = {1, 2};
        int[] arr2 = {3, 4};
        double median = findMedian(arr1, arr2);
        System.out.println(median);
    }

    /**
     * Get median using two pointer
     * @param nums1
     * @param nums2
     * @return
     */
    static double getMedianPointer(int[] nums1, int[] nums2) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        int count;
        double curr = -1;
        double prev = -1;

        int i = 0;
        int j = 0;
        if ((len2 + len1) % 2 == 1) {
            for (count = 0; count <= (len1 + len2) / 2; count++) {
                if (i != len1 && j != len2) {
                    curr = (nums1[i] > nums2[j]) ? nums2[j++] : nums1[i++];
                } else if (i < len1) {
                    curr = nums1[i++];
                } else {
                    curr = nums2[j++];
                }
            }
            return curr;
        } else {
            for (count = 0; count <= (len1 + len2) / 2; count++) {
                prev = curr;
                if (i != len1 && j != len2) {
                    curr = (nums1[i] > nums2[j]) ? nums2[j++] : nums1[i++];
                } else if (i < len1) {
                    curr = nums1[i++];
                } else {
                    curr = nums2[j++];
                }
            }
            return (curr + prev) / 2;
        }
    }


    /**
     * https://www.youtube.com/watch?v=NTop3VTjmxk
     */
    public static double findMedian(int[] nums1, int[] nums2) {
        if(nums2.length < nums1.length) return findMedian(nums2, nums1);
        int len1 = nums1.length;
        int len2 = nums2.length;
        int low = 0, high = len1;

        while(low <= high) {
            int cut1 = (low+high)/2;
            int cut2 = (len1 + len2 + 1) / 2 - cut1;


            int left1 = cut1 == 0 ? Integer.MIN_VALUE : nums1[cut1-1];
            int left2 = cut2 == 0 ? Integer.MIN_VALUE : nums2[cut2-1];

            int right1 = cut1 == len1 ? Integer.MAX_VALUE : nums1[cut1];
            int right2 = cut2 == len2 ? Integer.MAX_VALUE : nums2[cut2];


            if(left1 <= right2 && left2 <= right1) { // found correct cuts
                if( (len1 + len2) % 2 == 0 )
                    return (Math.max(left1, left2) + Math.min(right1, right2)) / 2.0;
                else
                    return Math.max(left1, left2);
            }
            else if(left1 > right2) {
                high = cut1 - 1;
            }
            else {
                low = cut1 + 1;
            }
        }
        return 0.0;
    }
}
