package DynamicProgramming;

import java.util.Arrays;

/**
 * Given an array of  positive elements, you have to flip the sign of some of its elements
 * such that the resultant sum of the elements of array should be minimum
 * non-negative(as close to zero as possible).
 * Return the minimum no. of elements whose sign needs to be flipped such
 * that the resultant sum is minimum non-negative.
 */
public class FlipArray {
    public static void main(String[] args) {
        int[] arr = {8, 4, 5, 7, 6, 2};
        int result = new FlipArray().solve(arr);
        System.out.println(result);
    }

    public int solve(int[] arr) {
        int sum=0;
        for(Integer element: arr) sum += element;

        int[] dp= new int[sum+1];
        Arrays.fill(dp,Integer.MAX_VALUE);
        dp[sum]=0; // 0 flips required to keep sum same.

        for(int i=0;i<arr.length;i++){
            for(int j=0;j<=sum;j++){
                if(dp[j]!=Integer.MAX_VALUE){
                    int diff = 2*arr[i]; // difference in sum if current element is flipped
                    if(j - diff >= 0){
                        dp[j - diff] = Math.min(1 + dp[j], dp[j - diff]);
                    }
                }
            }
        }

        int ans=0;
        for(int i=0;i<dp.length;i++){
            if(dp[i]!=Integer.MAX_VALUE){
                ans=dp[i];
                break;
            }
        }
        return ans;
    }
}
