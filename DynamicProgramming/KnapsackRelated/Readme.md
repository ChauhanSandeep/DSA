## **Problem Name:** 0/1 Knapsack

*   **Problem Statement:** Given a set of items, each with a weight and a value, and a knapsack with a maximum weight capacity, determine the maximum total value of items that can be included in the knapsack without exceeding the weight capacity. Each item can be included only once (0/1 constraint).
* **Problem Intuition** The 0/1 Knapsack problem's intuition is about making optimal `include/exclude` decisions for each item to maximize the value in a knapsack, constrained by its capacity. For each item, we decide whether including it (if it fits) increases the overall value more than excluding it. Dynamic programming systematically explores all these choices, storing the maximum value achievable for each item and capacity combination, ultimately finding the best selection.


### **Memoized Recursive Code (Java):**

```java
public static int knapsackRecursive(int[] weights, int[] values, int length, int maxCapacity) {
  // Create memoization table with default value -1
  int[][] memo = new int[length][maxCapacity + 1]; // memo[i][j] denotes max value for first i items and capacity j
  for (int[] row : memo) {
    java.util.Arrays.fill(row, -1);
  }
  return knapsackRecursiveHelper(weights, values, 0, maxCapacity, memo);
}

private static int knapsackRecursiveHelper(int[] weights, int[] values, int currentIndex, int remainingCapacity, int[][] memo) {
  // Base condition: no more items to check
  if (currentIndex == weights.length) {
    return 0;
  }

  // Return cached value if already computed
  if (memo[currentIndex][remainingCapacity] != -1) {
    return memo[currentIndex][remainingCapacity];
  }

  // Option 1: Do not include the current item
  int exclude = knapsackRecursiveHelper(weights, values, currentIndex + 1, remainingCapacity, memo);

  // Option 2: Include the current item (if it fits)
  int include = 0;
  if (weights[currentIndex] <= remainingCapacity) {
    include = values[currentIndex] +
        knapsackRecursiveHelper(weights, values, currentIndex + 1, remainingCapacity - weights[currentIndex], memo);
  }

  // Store and return the maximum of both options
  memo[currentIndex][remainingCapacity] = Math.max(include, exclude);
  return memo[currentIndex][remainingCapacity];
}
```

### **Iterative Code:**

```java
    public static int knapsackIterative(int[] weights, int[] values, int length, int capacity) {
  int[][] dp = new int[length][capacity + 1]; // dp[i][j] denotes max value for first i items and capacity j

  // Initialize for the first item.
  for (int w = weights[0]; w <= capacity; w++) {
    dp[0][w] = values[0];
  }

  for (int itemIndex = 1; itemIndex < length; itemIndex++) {
    for (int remainingCapacity = 0; remainingCapacity <= capacity; remainingCapacity++) {
      int excludeItem = dp[itemIndex - 1][remainingCapacity];
      int includeItem = 0;
      if (weights[itemIndex] <= remainingCapacity) {
        // If we can include the item, calculate the value.
        // Include the item and add its value to the remaining capacity.
        includeItem = values[itemIndex] + dp[itemIndex - 1][remainingCapacity - weights[itemIndex]];
      }
      dp[itemIndex][remainingCapacity] = Math.max(includeItem, excludeItem);
    }
  }

  return dp[length - 1][capacity];
}
```

*   **Time and Space Complexity:**
   *   **Memoized Recursive:**
      *   Time Complexity: O(numberOfItems * capacity) - Due to memoization.
      *   Space Complexity: O(numberOfItems * capacity) + O(numberOfItems) -  For the `dp` table and the recursion call stack.
   *   **Tabulation:**
      *   Time Complexity: O(numberOfItems * capacity)
      *   Space Complexity: O(numberOfItems * capacity)

## **Child Problems:**

### 1.  **Subset Sum**
   *   **Problem Statement:** Given a set of non-negative integers and a target sum, determine if there exists a subset of the given set whose elements sum up to the target sum.

   *   **High-Level Approach:** Use dynamic programming to build a table indicating whether a subset summing to a particular value exists using the first 'i' elements. The table `dp[i][sum]` stores true if a subset of the first 'i' elements can sum to 'sum', and false otherwise.

   *   **Connection to Parent Problem (Knapsack):**  Subset Sum can be viewed as a special case of the Knapsack problem where the value of each item is equal to its weight, and the target sum is the knapsack capacity. The problem simplifies to determining if there's a combination of item weights that perfectly fills the "knapsack" (reaches the target sum). The core `include/exclude` decision-making process from Knapsack is directly applicable here.

### 2.  **Count Subsets with Given Sum**
   *   **Problem Statement:** Given a set of non-negative integers and a target sum, count the number of subsets whose elements sum up to the target sum.

   *   **High-Level Approach:** Similar to Subset Sum, use a DP table `dp[i][sum]`. But instead of storing a boolean (true/false), store the *count* of subsets of the first `i` elements that sum to `sum`. When you include an element, you *add* the number of ways to achieve the remaining sum using the previous elements.

   *   **Connection to Parent Problem (Knapsack):**  Again, akin to Knapsack and Subset Sum, we make an `include/exclude` decision for each element. The key difference is that instead of maximizing value (Knapsack) or checking for existence (Subset Sum), we're *accumulating* the number of valid subset combinations. This shifts the operation from `Math.max()` or `||` to addition. We're essentially counting the different paths to reach the target sum.

### 3.  **Equal Sum Partition**
   *   **Problem Statement:** Given a set of non-negative integers, determine if the set can be partitioned into two subsets such that the sum of elements in both subsets is equal.

   *   **High-Level Approach:** Calculate the sum of all elements in the set. If the total sum is odd, it's impossible to partition the set into two equal sum subsets. If the total sum is even, the problem reduces to finding if a subset with sum equal to `totalSum / 2` exists (which is the Subset Sum problem).

   *   **Connection to Parent Problem (Knapsack):** This is a direct application of the Subset Sum problem.  We transform the Equal Sum Partition problem into a Subset Sum problem by calculating the total sum and then checking if a subset exists with a sum equal to half the total sum.

### 4.  **Minimum Subset Sum Difference**
   *   **Problem Statement:** Given a set of integers, divide the set into two subsets such that the absolute difference of the subset sums is minimized.

   *   **High-Level Approach:**
      1.  Calculate the total sum of all elements.
      2.  Use the Subset Sum problem to determine all possible subset sums that can be formed using the given elements.
      3.  Iterate through the possible subset sums (from the Subset Sum result). For each possible subset sum `subsetSum1`, the sum of the other subset `subsetSum2` will be `totalSum - subsetSum1`. The absolute difference is `abs(subsetSum1 - subsetSum2) = abs(subsetSum1 - (totalSum - subsetSum1)) = abs(2 * subsetSum1 - totalSum)`.
      4.  Minimize this absolute difference.

   *   **Connection to Parent Problem (Knapsack):**  This problem *uses* the Subset Sum problem as a building block. We're leveraging the DP solution for Subset Sum to find the range of possible subset sums, and then we perform an optimization step to minimize the difference between the two partitions.

### 5.  **Count Subset with Given Difference**
   *   **Problem Statement:** Given an array of integers and a target difference, find the number of ways to partition the array into two subsets such that the difference between the sums of the two subsets equals the target difference.

   *   **High-Level Approach:**
       Let `S1` and `S2` be the two subsets, and `sum(S1)` and `sum(S2)` be their respective sums. We are given that `sum(S1) - sum(S2) = difference`. Also, we know that `sum(S1) + sum(S2) = totalSum` (where `totalSum` is the sum of all elements in the array).
       Adding these two equations, we get:  `2 * sum(S1) = difference + totalSum`
       Therefore, `sum(S1) = (difference + totalSum) / 2`.  Now the problem reduces to finding the number of subsets with sum equal to `(difference + totalSum) / 2`. If `(difference + totalSum)` is odd, there are no such subsets.

   *   **Connection to Parent Problem (Knapsack):**  This problem is *transformed* into a Count Subsets with Given Sum problem. By using the mathematical derivation, we convert the problem from finding subsets with a specific difference to counting subsets with a specific sum.

### 6.  **Target Sum**


   *   **Problem Statement:** Given an array of integers and a target value, find the number of ways to assign '+' or '-' signs to each integer in the array such that the sum of the signed integers equals the target value.

   *   **High-Level Approach:** This problem is equivalent to Count Subset with Given Difference.  If you assign '+' to the elements in subset `S1` and '-' to the elements in subset `S2`, then `sum(S1) - sum(S2) = target`. The problem becomes the same as finding the number of subsets with a given difference (target).

   *   **Connection to Parent Problem (Knapsack):** Like Count Subset with Given Difference, this problem reduces to a Count Subsets with Given Sum after a transformation. We recognize that assigning signs is equivalent to partitioning the array into two subsets with a specific sum difference.

### 7.  **Shortest Subarray Sum**


   *   **Problem Statement:** Given an array of integers and a target value, find the minimum length of subarray such that the sum is greater than or equal to the target value.

   *   **High-Level Approach:**
       This is solved using sliding window approach. The array is traversed from left to right. If the current subarray sum is less than target value then the window size is increased. If the subarray sum is greater or equal to the target value then window size is reduced, while doing this also compute the minimum window size.

   *   **Connection to Parent Problem (Knapsack):** This is no way directly connected to Knapsack problem.

