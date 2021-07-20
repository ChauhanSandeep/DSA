package DynamicProgramming.KnapsackRelated;

/**
 * Find if the array can be divided into two partitions such that sum of each partition is same
 */
public class EqualSum {
    public static void main(String[] args) {
        int[] arr = {479, 758, 315, 472, 730, 101, 460, 619};
        System.out.println(equalPartition(arr, arr.length));

        arr = new int[]{1, 3, 5};
        System.out.println(equalPartition(arr, arr.length));
    }

    public static boolean equalPartition(int[] arr, int size) {
        int sum = 0;
        for(int i=0; i<arr.length; i++) {
            sum += arr[i];
        }
        // sum must be event to be equally divisible
        if(sum%2 == 1) return false;
        // check if we can create subarray with sum = sum/2
        return findSubsetSum(arr, sum/2, size);
    }

    public static boolean findSubsetSum(int[] arr, int sum, int size) {
        boolean[][] dp = new boolean[size+1][sum+1];

        for(int i=0; i<size+1; i++) {
            dp[i][0] = true;
        }
        for(int i=1; i<size+1; i++) {
            for(int j=1; j<sum+1; j++) {
                if(arr[i-1] <= j) {
                    dp[i][j] = dp[i-1][j] || dp[i-1][j - arr[i-1]];
                }else{
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        return dp[size][sum];
    }



}
