# 0/1 Knapsack Problem & Associated Child Problems

## 0/1 Knapsack Problem
The **0/1 Knapsack Problem** is a fundamental dynamic programming problem that revolves around selecting a subset of items to maximize the total value without exceeding a given weight capacity.

### Logic:
1. **Given**:
    - A set of items, each with a **weight** and **value**.
    - A **capacity** `W` (maximum weight).

2. **Goal**:
    - Maximize the total value of selected items such that their combined weight does not exceed the capacity `W`.

3. **Approach**:
    - Define a **DP table** where `dp[i][w]` represents the maximum value that can be obtained by considering the first `i` items with a weight limit `w`.
    - **Recurrence relation**:
      ``` 
      dp[i][w] = max(dp[i-1][w], dp[i-1][w - weight[i-1]] + value[i-1])
      ```
        - If item `i` is **excluded**, the value is `dp[i-1][w]`.
        - If item `i` is **included**, the value is `dp[i-1][w - weight[i-1]] + value[i-1]`.

4. **Base case**:
    - If there are no items or the capacity is zero, the maximum value is `0`:
      ```
      dp[0][w] = 0  for all w
      dp[i][0] = 0  for all i
      ```

---

## Child Problems of 0/1 Knapsack

### 1. **Subset Sum Problem**
- **Description**: Given a set of numbers, find if there is a subset whose sum is exactly equal to a target sum `S`.
- **Key Idea**: This is a special case of the 0/1 Knapsack where the value of each item is equal to its weight, and the target capacity is the desired sum `S`.

**Recurrence**:

dp[i][w] = dp[i-1][w] || dp[i-1][w - weight[i-1]]

### 2. **Equal Sum Partition**
- **Description**: Determine if an array can be partitioned into two subsets such that their sum is equal.
- **Key Idea**: This can be reduced to the Subset Sum problem. If we can find a subset with sum `S/2`, the rest of the array automatically sums to `S/2`.

**Logic**:
- First, calculate the total sum of the array. If the sum is odd, it's impossible to split it into two equal parts.
- Then, check if there's a subset whose sum is `total_sum / 2`.

### 3. **Count of Subsets with Given Sum**
- **Description**: Count the number of subsets that sum to a given value `S`.
- **Key Idea**: This is a modification of the 0/1 Knapsack, where instead of storing the maximum value, we store the count of ways to achieve each weight.

**Recurrence**:

dp[i][w] = dp[i-1][w] + dp[i-1][w - weight[i-1]]

- The count increases when an item is included.

### 4. **Minimum Subset Sum Difference**
- **Description**: Partition the array into two subsets such that the difference between their sums is minimized.
- **Key Idea**: This is based on the observation that if you find a subset sum closest to `total_sum / 2`, the difference will be minimized.

**Logic**:
- First, solve the Subset Sum problem for all values up to `total_sum / 2`.
- The best subset sum is the one closest to `total_sum / 2`, and the difference is `total_sum - 2 * best_sum`.

### 5. **Count of Subsets with Given Difference**
- **Description**: Find the number of subsets that have a given difference between their sums.
- **Key Idea**: This can be reduced to the Subset Sum problem using a transformation.

**Transformation**:
- Given the problem of finding subsets with difference `D`, let the sum of the subsets be `S1` and `S2`. The difference is:
  - S1 - S2 = diff
  - S1 + S2 = total_sum

- Solve for `S1 = (total_sum + diff) / 2`. Now, count subsets that sum to `S1`.

### 6. **Target Sum**
- **Description**: Given an array, find the number of ways to assign signs (`+` or `-`) to each number to reach a target sum.
- **Key Idea**: This is a variant of the 0/1 Knapsack problem, where you can either include or exclude an item (number) in the calculation.

**Transformation**:
- This problem can be reduced to a subset sum problem by considering the target sum as a new capacity.

