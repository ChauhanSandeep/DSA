package DynamicProgramming.KnapsackRelated;

/**
 * Given a array, find the number of subsets with provided sum
 */
public class CountSubsetSum {

    public static void main(String[] args) {
        int[] arr = {2, 3, 5, 6 ,8, 10};
        int sum = 10;
        int count = countSubsetSum(arr, sum, arr.length);
        System.out.println(count);
    }

    public static int countSubsetSum(int[] arr, int sum, int size) {
        int[][] dp = new int[size+1][sum+1];
        for(int i=0; i<size+1; i++){
            dp[i][0] = 1; // one way to make subset of 0 sum, empty array
        }

        for(int i=1; i<size+1; i++) {
            for(int j=1; j<sum+1; j++) {
                if(arr[i-1] <= j) {
                    dp[i][j] = dp[i-1][j] + dp[i-1][j-arr[i-1]];
                }else{
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        return dp[size][sum];
    }
}
