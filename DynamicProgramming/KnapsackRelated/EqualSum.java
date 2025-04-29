package DynamicProgramming.KnapsackRelated;

/**
 * Find if the array can be divided into two partitions such that sum of each partition is the same
 */
public class EqualSum {
  public static void main(String[] args) {
    int[] nums = {1, 5, 11, 5};

    System.out.println("Recursive: " + canPartitionRecursive(nums));  // true
    System.out.println("Iterative: " + canPartitionIterative(nums));  // true
  }

  /**
   * Recursive Approach:
   *
   * Intuition:
   * - If total sum is odd, we can't split into two equal parts.
   * - Else, reduce the problem to checking if subset sum = total / 2 exists (use Subset Sum logic).
   *
   * Time Complexity: O(n * sum)
   * Space Complexity: O(n * sum) for memo + O(n) recursion stack
   */
  public static boolean canPartitionRecursive(int[] nums) {
    int total = 0;
    for (int num : nums) total += num;
    if (total % 2 != 0) return false;

    int target = total / 2;
    Boolean[][] dp = new Boolean[nums.length][target + 1];
    return isSubsetSum(nums, nums.length - 1, target, dp);
  }

  private static boolean isSubsetSum(int[] nums, int index, int target, Boolean[][] dp) {
    if (target == 0) return true;
    if (index == 0) return nums[0] == target;
    if (dp[index][target] != null) return dp[index][target];

    boolean notTake = isSubsetSum(nums, index - 1, target, dp);
    boolean take = false;
    if (nums[index] <= target) {
      take = isSubsetSum(nums, index - 1, target - nums[index], dp);
    }

    return dp[index][target] = take || notTake;
  }

  /**
   * Iterative Tabulation Approach:
   *
   * Intuition:
   * Check for subset with sum = total / 2 using Subset Sum DP table.
   *
   * Time Complexity: O(n * sum)
   * Space Complexity: O(n * sum)
   */
  public static boolean canPartitionIterative(int[] nums) {
    int total = 0;
    for (int num : nums) total += num;
    if (total % 2 != 0) return false;

    int size = nums.length;
    int sum = total / 2;
    boolean[][] dp = new boolean[size][sum + 1];

    for (int i = 0; i < size; i++) {
      dp[i][0] = true;
    }
    // Initialize first row
    if (nums[0] <= sum) {
      dp[0][nums[0]] = true;
    }

    for (int elementIndex = 1; elementIndex < size; elementIndex++) {
      for (int currentSum = 1; currentSum <= sum; currentSum++) {
        boolean notTake = dp[elementIndex - 1][currentSum];
        boolean take = false;
        if (nums[elementIndex] <= currentSum) {
          take = dp[elementIndex - 1][currentSum - nums[elementIndex]];
        }
        dp[elementIndex][currentSum] = take || notTake;
      }
    }

    return dp[size - 1][sum];
  }


}