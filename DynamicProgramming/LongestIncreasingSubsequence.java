package DynamicProgramming;

import java.util.Arrays;

public class LongestIncreasingSubsequence {
    public static void main(String[] args) {
        int[] arr = {10, 22, 9, 33, 21, 50, 41, 60};
        int lis = findLIS(arr);
        System.out.println(lis);
    }

    /**
     * Find the length of longest increasing subsequence in array
     * @param arr
     * @return
     */
    public static int findLIS(int[] arr) {
        if(arr == null || arr.length == 1) return 1;
        int[] lisArr = new int[arr.length];
        Arrays.fill(lisArr, 1);

        for(int i=1; i<arr.length; i++) {
            for(int j=0; j<i; j++) {
                if(arr[j] < arr[i] && lisArr[j] + 1 > lisArr[i]) {
                    lisArr[i] = lisArr[j] + 1;
                }
            }

        }
        return lisArr[arr.length - 1];
    }
}
