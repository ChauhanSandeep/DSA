*   **Problem Name:** 0/1 Knapsack

*   **Problem Statement:** Given a set of items, each with a weight and a value, and a knapsack with a maximum weight capacity, determine the maximum total value of items that can be included in the knapsack without exceeding the weight capacity. Each item can be included only once (0/1 constraint).
* **Problem Intuition** The 0/1 Knapsack problem's intuition is about making optimal `include/exclude` decisions for each item to maximize the value in a knapsack, constrained by its capacity. For each item, we decide whether including it (if it fits) increases the overall value more than excluding it. Dynamic programming systematically explores all these choices, storing the maximum value achievable for each item and capacity combination, ultimately finding the best selection.


*   **Memoized Recursive Code (Java):**

```java
public class KnapsackRevision {

    public static int knapsackMemoized(int[] weights, int[] values, int capacity, int index, int[][] dp) {
        // Base cases:
        if (index == 0 || capacity == 0) {
            return 0;
        }

        // Check if the result is already memoized
        if (dp[index][capacity] != -1) {
            return dp[index][capacity];
        }

        // If the current item's weight is less than or equal to the remaining capacity:
        if (weights[index - 1] <= capacity) {
            // Choose the maximum of either including the item or excluding the item
            dp[index][capacity] = Math.max(
                values[index - 1] + knapsackMemoized(weights, values, capacity - weights[index - 1], index - 1, dp),  // Include
                knapsackMemoized(weights, values, capacity, index - 1, dp)                                             // Exclude
            );
        } else {
            // If the current item's weight is greater than the remaining capacity, exclude the item
            dp[index][capacity] = knapsackMemoized(weights, values, capacity, index - 1, dp);
        }

        return dp[index][capacity];
    }
    
```

*   **Tabulation Code (Java):**

```java
    public static int knapsackTabulation(int[] weights, int[] values, int capacity, int numberOfItems) {
        int[][] dp = new int[numberOfItems + 1][capacity + 1];

        // Initialization (Base Cases):
        // If either no items or no capacity, the value is 0
        for (int itemIndex = 0; itemIndex <= numberOfItems; itemIndex++) {
            for (int currentCapacity = 0; currentCapacity <= capacity; currentCapacity++) {
                if (itemIndex == 0 || currentCapacity == 0) {
                    dp[itemIndex][currentCapacity] = 0;
                }
                // If the current item's weight is less than or equal to the current capacity
                else if (weights[itemIndex - 1] <= currentCapacity) {
                    // Choose the maximum of either including the item or excluding the item
                    dp[itemIndex][currentCapacity] = Math.max(
                        values[itemIndex - 1] + dp[itemIndex - 1][currentCapacity - weights[itemIndex - 1]], // Include
                        dp[itemIndex - 1][currentCapacity]                                                   // Exclude
                    );
                } else {
                    // If the current item's weight is greater than the current capacity, exclude the item
                    dp[itemIndex][currentCapacity] = dp[itemIndex - 1][currentCapacity];
                }
            }
        }

        return dp[numberOfItems][capacity];
    }
}
```

*   **Time and Space Complexity:**
   *   **Memoized Recursive:**
      *   Time Complexity: O(numberOfItems * capacity) - Due to memoization.
      *   Space Complexity: O(numberOfItems * capacity) + O(numberOfItems) -  For the `dp` table and the recursion call stack.
   *   **Tabulation:**
      *   Time Complexity: O(numberOfItems * capacity)
      *   Space Complexity: O(numberOfItems * capacity)

**Child Problems:**

1.  **Subset Sum**

   *   **Problem Name:** Subset Sum

   *   **Problem Statement:** Given a set of non-negative integers and a target sum, determine if there exists a subset of the given set whose elements sum up to the target sum.

   *   **High-Level Approach:** Use dynamic programming to build a table indicating whether a subset summing to a particular value exists using the first 'i' elements. The table `dp[i][sum]` stores true if a subset of the first 'i' elements can sum to 'sum', and false otherwise.

   *   **Connection to Parent Problem (Knapsack):**  Subset Sum can be viewed as a special case of the Knapsack problem where the value of each item is equal to its weight, and the target sum is the knapsack capacity. The problem simplifies to determining if there's a combination of item weights that perfectly fills the "knapsack" (reaches the target sum). The core `include/exclude` decision-making process from Knapsack is directly applicable here.

2.  **Count Subsets with Given Sum**

   *   **Problem Name:** Count Subsets with Given Sum

   *   **Problem Statement:** Given a set of non-negative integers and a target sum, count the number of subsets whose elements sum up to the target sum.

   *   **High-Level Approach:** Similar to Subset Sum, use a DP table `dp[i][sum]`. But instead of storing a boolean (true/false), store the *count* of subsets of the first `i` elements that sum to `sum`. When you include an element, you *add* the number of ways to achieve the remaining sum using the previous elements.

   *   **Connection to Parent Problem (Knapsack):**  Again, akin to Knapsack and Subset Sum, we make an `include/exclude` decision for each element. The key difference is that instead of maximizing value (Knapsack) or checking for existence (Subset Sum), we're *accumulating* the number of valid subset combinations. This shifts the operation from `Math.max()` or `||` to addition. We're essentially counting the different paths to reach the target sum.

3.  **Equal Sum Partition**

   *   **Problem Name:** Equal Sum Partition

   *   **Problem Statement:** Given a set of non-negative integers, determine if the set can be partitioned into two subsets such that the sum of elements in both subsets is equal.

   *   **High-Level Approach:** Calculate the sum of all elements in the set. If the total sum is odd, it's impossible to partition the set into two equal sum subsets. If the total sum is even, the problem reduces to finding if a subset with sum equal to `totalSum / 2` exists (which is the Subset Sum problem).

   *   **Connection to Parent Problem (Knapsack):** This is a direct application of the Subset Sum problem.  We transform the Equal Sum Partition problem into a Subset Sum problem by calculating the total sum and then checking if a subset exists with a sum equal to half the total sum.

4.  **Minimum Subset Sum Difference**

   *   **Problem Name:** Minimum Subset Sum Difference

   *   **Problem Statement:** Given a set of integers, divide the set into two subsets such that the absolute difference of the subset sums is minimized.

   *   **High-Level Approach:**
      1.  Calculate the total sum of all elements.
      2.  Use the Subset Sum problem to determine all possible subset sums that can be formed using the given elements.
      3.  Iterate through the possible subset sums (from the Subset Sum result). For each possible subset sum `subsetSum1`, the sum of the other subset `subsetSum2` will be `totalSum - subsetSum1`. The absolute difference is `abs(subsetSum1 - subsetSum2) = abs(subsetSum1 - (totalSum - subsetSum1)) = abs(2 * subsetSum1 - totalSum)`.
      4.  Minimize this absolute difference.

   *   **Connection to Parent Problem (Knapsack):**  This problem *uses* the Subset Sum problem as a building block. We're leveraging the DP solution for Subset Sum to find the range of possible subset sums, and then we perform an optimization step to minimize the difference between the two partitions.

5.  **Count Subset with Given Difference**

   *   **Problem Name:** Count Subset with Given Difference

   *   **Problem Statement:** Given an array of integers and a target difference, find the number of ways to partition the array into two subsets such that the difference between the sums of the two subsets equals the target difference.

   *   **High-Level Approach:**
       Let `S1` and `S2` be the two subsets, and `sum(S1)` and `sum(S2)` be their respective sums. We are given that `sum(S1) - sum(S2) = difference`. Also, we know that `sum(S1) + sum(S2) = totalSum` (where `totalSum` is the sum of all elements in the array).
       Adding these two equations, we get:  `2 * sum(S1) = difference + totalSum`
       Therefore, `sum(S1) = (difference + totalSum) / 2`.  Now the problem reduces to finding the number of subsets with sum equal to `(difference + totalSum) / 2`. If `(difference + totalSum)` is odd, there are no such subsets.

   *   **Connection to Parent Problem (Knapsack):**  This problem is *transformed* into a Count Subsets with Given Sum problem. By using the mathematical derivation, we convert the problem from finding subsets with a specific difference to counting subsets with a specific sum.

6.  **Target Sum**

   *   **Problem Name:** Target Sum

   *   **Problem Statement:** Given an array of integers and a target value, find the number of ways to assign '+' or '-' signs to each integer in the array such that the sum of the signed integers equals the target value.

   *   **High-Level Approach:** This problem is equivalent to Count Subset with Given Difference.  If you assign '+' to the elements in subset `S1` and '-' to the elements in subset `S2`, then `sum(S1) - sum(S2) = target`. The problem becomes the same as finding the number of subsets with a given difference (target).

   *   **Connection to Parent Problem (Knapsack):** Like Count Subset with Given Difference, this problem reduces to a Count Subsets with Given Sum after a transformation. We recognize that assigning signs is equivalent to partitioning the array into two subsets with a specific sum difference.

7.  **Shortest Subarray Sum**

   *   **Problem Name:** Shortest Subarray Sum

   *   **Problem Statement:** Given an array of integers and a target value, find the minimum length of subarray such that the sum is greater than or equal to the target value.

   *   **High-Level Approach:**
       This is solved using sliding window approach. The array is traversed from left to right. If the current subarray sum is less than target value then the window size is increased. If the subarray sum is greater or equal to the target value then window size is reduced, while doing this also compute the minimum window size.

   *   **Connection to Parent Problem (Knapsack):** This is no way directly connected to Knapsack problem.

