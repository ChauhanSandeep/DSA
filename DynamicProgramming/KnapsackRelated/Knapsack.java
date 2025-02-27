package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

public class Knapsack {

    /**
     * Find the maximum values that can be collected given that weight cannot exceed capacity.
     * @return
     */
    public static void main(String[] args) {
        int[] values = {1, 6, 10, 16};
        int[] weights = {1, 2, 3, 5};
        int capacity = 6;
        System.out.println(knapsackIter(weights, values, capacity, 4));
    }

    /**
     * Recursive
     */
    public static int knapsackRec(int[] weights, int[] values, int capacity, int size) {
        if(size==0 || capacity==0) {
            return 0;
        }
        if(weights[size-1 ] <= capacity) {
            return Math.max(values[size-1] + knapsackRec(weights, values, capacity-weights[size-1], size-1),
                    knapsackRec(weights, values, capacity, size-1));
        }else{
            return knapsackRec(weights, values, capacity, size-1);
        }
    }

    /**
     * Recursive + memoization
     */
    public static int knapsackHelper(int[] weights, int[] values, int capacity, int size) {
        int[][] dp = new int[size+1][capacity+1];
        for(int[] row: dp) {
            Arrays.fill(row, -1);
        }
        return knapsackDp(weights, values, capacity, size, dp);
    }

    public static int knapsackDp(int[] weights, int[] values, int capacity, int size, int[][] dp) {
        if(size ==0 || capacity ==0) {
            return 0;
        }
        if(dp[size][capacity] != -1) {
            return dp[size][capacity];
        }

        int result = -1;
        if(weights[size-1 ] <= capacity) {
            result = Math.max(values[size-1] + knapsackDp(weights, values, capacity-weights[size-1], size-1, dp),
                    knapsackDp(weights, values, capacity, size-1, dp));
        }else{
            result = knapsackDp(weights, values, capacity, size-1, dp);
        }
        dp[size][capacity] = result;
        return result;
    }

    /**
     * Iteration
     */
    public static int knapsackIter(int[] weights, int[] values, int capacity, int size) {
        int[][] dp = new int[size+1][capacity+1];
        for(int[] row: dp) {
            Arrays.fill(row, -1);
        }
        for(int i=0; i<dp.length; i++) {
            dp[i][0] = 0;
        }
        Arrays.fill(dp[0], 0);

        for (int i = 1; i <= size; i++) {
            int currentWeight = weights[i - 1];  // Weight of the current item
            int currentValue = values[i - 1];      // Value of the current item

            for (int j = 1; j <= capacity; j++) {
                // Option 1: Exclude the current item
                int valueIfExcluded = dp[i - 1][j];

                // Option 2: Include the current item (if it fits)
                int valueIfIncluded = 0;
                if (currentWeight <= j) {
                    int remainingCapacity = j - currentWeight; // Remaining capacity after including the current item
                    int bestValueForRemainingCapacity = dp[i - 1][remainingCapacity]; // Best value possible with remaining capacity
                    valueIfIncluded = currentValue + bestValueForRemainingCapacity;
                }

                // Choose the best of including or excluding the current item
                dp[i][j] = Math.max(valueIfExcluded, valueIfIncluded);
            }
        }
        return dp[size][capacity];
    }

}
