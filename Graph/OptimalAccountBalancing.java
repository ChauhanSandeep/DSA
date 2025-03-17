package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Optimal Account Balancing (Splitwise Problem)
 * LeetCode: https://leetcode.com/problems/optimal-account-balancing/
 *
 * --- Problem Description ---
 * Given a list of transactions, find the minimum number of transactions needed
 * to balance all debts among individuals.
 *
 * --- Approach ---
 * 1. **Calculate net balances**: Compute how much each person owes or is owed.
 * 2. **Filter non-zero balances**: We only need to process non-zero balances.
 * 3. **Use DFS with backtracking** to explore all possible ways to settle debts.
 *    - Try to settle one person's balance with another.
 *    - Backtrack to explore other possibilities.
 *    - Minimize the number of transactions required.
 *
 * --- Complexity Analysis ---
 * - **Time Complexity:** O(N!), where N is the number of non-zero balances.
 * - **Space Complexity:** O(N), due to recursive stack and balance storage.
 */
public class OptimalAccountBalancing {

    public static void main(String[] args) {
        int[][] transactions = {
                {0, 1, 10}, // [from, to, amount]
                {1, 0, 1},
                {1, 2, 5},
                {2, 0, 5}
        };
        int minTransfers = new OptimalAccountBalancing().minTransfers(transactions);
        System.out.println("Minimum Transactions: " + minTransfers);
    }

    /**
     * Finds the minimum number of transactions needed to settle all debts.
     *
     * @param transactions Array of transactions where transactions[i] = [from, to, amount]
     * @return Minimum number of transactions required to balance accounts
     */
    public int minTransfers(int[][] transactions) {
        // Step 1: Compute net balance of each person
        Map<Integer, Integer> netBalanceMap = calculateNetBalances(transactions);

        // Step 2: Store only non-zero balances
        List<Integer> nonZeroBalances = new ArrayList<>();
        for (int balance : netBalanceMap.values()) {
            if (balance != 0) {
                nonZeroBalances.add(balance);
            }
        }

        // Step 3: Use DFS with backtracking to minimize transactions
        return settleDebts(nonZeroBalances, 0);
    }

    /**
     * Computes net balance for each person after all transactions.
     *
     * @param transactions Array of transactions
     * @return Map containing the net balance of each person
     */
    private Map<Integer, Integer> calculateNetBalances(int[][] transactions) {
        Map<Integer, Integer> balanceMap = new HashMap<>();
        for (int[] transaction : transactions) {
            int from = transaction[0], to = transaction[1], amount = transaction[2];

            // Increase balance for sender, decrease for receiver
            balanceMap.put(from, balanceMap.getOrDefault(from, 0) + amount);
            balanceMap.put(to, balanceMap.getOrDefault(to, 0) - amount);
        }
        return balanceMap;
    }

    /**
     * Uses DFS with backtracking to minimize the number of transactions required.
     *
     * @param balances List of non-zero balances to be settled
     * @param index    Current index in the balances list
     * @return Minimum number of transactions required to settle all debts
     */
    private int settleDebts(List<Integer> balances, int index) {
        // Skip settled persons (zero balance)
        while (index < balances.size() && balances.get(index) == 0) {
            index++;
        }

        // If all balances are settled, return 0 transactions needed
        if (index == balances.size()) return 0;

        int minTransactions = Integer.MAX_VALUE;

        // Try to settle balances[index] with other unsettled balances
        for (int i = index + 1; i < balances.size(); i++) {
            if (balances.get(i) * balances.get(index) < 0) { // Opposite signs mean possible settlement
                
                // Settle debts[index] with debts[i]
                balances.set(i, balances.get(i) + balances.get(index));

                // Recur for the next unsettled person
                minTransactions = Math.min(minTransactions, 1 + settleDebts(balances, index + 1));

                // Backtrack to previous state
                balances.set(i, balances.get(i) - balances.get(index));
            }
        }

        return minTransactions;
    }
}
