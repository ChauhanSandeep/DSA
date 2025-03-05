package Array;

/**
 * https://leetcode.com/problems/maximum-score-of-spliced-array/
 * 
 * Given two integer arrays nums1 and nums2 of the same length, we can splice either array into the other.
 * Our goal is to maximize the sum after splicing.
 */
public class MaxScoreSplicedArray {

    public int maximumSplicedArray(int[] nums1, int[] nums2) {
        int sumNums1 = arraySum(nums1);
        int sumNums2 = arraySum(nums2);

        // Find max gain when swapping elements from nums2 into nums1 and vice versa
        int maxGain1 = maxSubarrayGain(nums2, nums1);  // Gain from swapping nums2 into nums1
        int maxGain2 = maxSubarrayGain(nums1, nums2);  // Gain from swapping nums1 into nums2

        // Max of original sum or modified sums
        return Math.max(sumNums1 + maxGain1, sumNums2 + maxGain2);
    }

    // Helper function to calculate total sum of an array
    private int arraySum(int[] arr) {
        int sum = 0;
        for (int num : arr) sum += num;
        return sum;
    }

    // Helper function implementing Kadane’s Algorithm for max subarray difference
    private int maxSubarrayGain(int[] arr1, int[] arr2) {
        int maxGain = 0, currentGain = 0;
        for (int i = 0; i < arr1.length; i++) {
            currentGain += (arr1[i] - arr2[i]);
            maxGain = Math.max(maxGain, currentGain);
            if (currentGain < 0) currentGain = 0;
        }
        return maxGain;
    }

    public static void main(String[] args) {
        MaxScoreSplicedArray obj = new MaxScoreSplicedArray();
        int[] nums1 = {60, 60, 60};
        int[] nums2 = {10, 90, 10};

        System.out.println("Max Spliced Score: " + obj.maximumSplicedArray(nums1, nums2));
    }
}
