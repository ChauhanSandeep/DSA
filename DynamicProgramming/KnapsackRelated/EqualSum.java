package DynamicProgramming.KnapsackRelated;

/**
 * Find if the array can be divided into two partitions such that sum of each partition is the same
 */
public class EqualSum {
  public static void main(String[] args) {
    int[] arr = {479, 758, 315, 472, 730, 101, 460, 619};
    System.out.println(equalPartition(arr));

    arr = new int[]{1, 3, 5};
    System.out.println(equalPartition(arr));
  }

  public static boolean equalPartition(int[] arr) {
    int sum = 0;
    for (int num : arr) {
      sum += num;
    }

    // Sum must be even to be equally divisible
    if (sum % 2 != 0) return false;

    // Check if we can create a subset with sum = sum / 2
    return findSubsetSumOptimized(arr, sum / 2);
  }

  public static boolean findSubsetSumOptimized(int[] arr, int target) {
    boolean[] dp = new boolean[target + 1];
    dp[0] = true;

    for (int num : arr) {
      for (int j = target; j >= num; j--) {
        dp[j] = dp[j] || dp[j - num];
      }
    }
    return dp[target];
  }
}