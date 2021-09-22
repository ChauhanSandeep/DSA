package Array;

public class MedianArrays {

    public static void main(String[] args) {
        int[] arr1 = {1, 2};
        int[] arr2 = {3, 4};
        double median = getMedian(arr1, arr2);
        System.out.println(median);
    }

    /**
     * Get median using two pointer
     * @param nums1
     * @param nums2
     * @return
     */
    static double getMedian(int[] nums1, int[] nums2) {
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
}
