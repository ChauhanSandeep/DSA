package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Find min transactions required to balance accounts in Splitwise.
 * https://leetcode.com/problems/optimal-account-balancing/
 */
public class OptimalAccountBalancing {

    public static void main(String[] args) {
        int[][] transactions = {
                {0, 1, 10}, // [from, to, amount]
                {1, 0, 1},
                {1, 2, 5},
                {2, 0, 5}};
        int minTransfers = new OptimalAccountBalancing().minTransfers(transactions);
        System.out.println("Minimum Transactions: " + minTransfers);
    }

    public int minTransfers(int[][] transactions) {
        // Step 1: Compute net balance of each person
        Map<Integer, Integer> balanceMap = new HashMap<>();
        for (int[] transaction : transactions) {
            int from = transaction[0], to = transaction[1], amount = transaction[2];
            balanceMap.put(from, balanceMap.getOrDefault(from, 0) + amount);
            balanceMap.put(to, balanceMap.getOrDefault(to, 0) - amount);
        }

        // Step 2: Store only non-zero balances
        List<Integer> balances = new ArrayList<>();
        for (int balance : balanceMap.values()) {
            if (balance != 0) balances.add(balance);
        }

        // Step 3: Use DFS with backtracking to minimize transactions
        return dfs(balances, 0);
    }

    private int dfs(List<Integer> balances, int index) {
        // Skip fully settled persons
        while (index < balances.size() && balances.get(index) == 0) {
            index++;
        }

        // If all accounts are settled
        if (index == balances.size()) return 0;

        int minTransactions = Integer.MAX_VALUE;

        for (int i = index + 1; i < balances.size(); i++) {
            if (balances.get(i) * balances.get(index) < 0) { // They can settle
                // Try settling balances[index] with balances[i]
                balances.set(i, balances.get(i) + balances.get(index));

                // Recursively settle remaining accounts
                minTransactions = Math.min(minTransactions, 1 + dfs(balances, index + 1));

                // Backtrack to previous state
                balances.set(i, balances.get(i) - balances.get(index));
            }
        }

        return minTransactions;
    }
}
