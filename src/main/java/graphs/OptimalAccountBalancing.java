package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Optimal Account Balancing (Splitwise Problem)
 * LeetCode: https://leetcode.com/problems/optimal-account-balancing/
 *
 * --- Problem Description ---
 * You are given an array of transactions where transactions[i] = [from_i, to_i, amount_i]
 * indicates that the person with ID = from_i gave amount_i $ to the person with ID = to_i.
 *
 * Return the minimum number of transactions required to settle the debt.
 *
 * Example:
 * Input: transactions = [[0,1,10],[2,0,5]]
 * Output: 2
 * Explanation:
 * Person #0 gave person #1 $10.
 * Person #2 gave person #0 $5.
 * Two transactions are needed. One way to settle the debt is person
 * #1 pays person #0 $5
 * #1 pays person #2 $5
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. How would you handle real-time updates to transactions during settlement?
 *    Answer: Use incremental algorithms with cached intermediate results and delta updates.
 * 2. What if we need to minimize total money transferred instead of number of transactions?
 *    Answer: Modify DP to track monetary amounts and use weighted optimization criteria.
 * 3. How to scale this for thousands of people with complex debt relationships?
 *    Answer: Use graph clustering, approximation algorithms, or distributed computing approaches.
 * 4. What if some people have credit limits or can only participate in certain transactions?
 *    Answer: Add constraint satisfaction with feasibility checks in the backtracking process.
 *
 * Related Problems:
 * - LeetCode 322: Coin Change (DP optimization)
 * - LeetCode 698: Partition to K Equal Sum Subsets (Subset partitioning)
 * - LeetCode 473: Matchsticks to Square (Backtracking with pruning)
 */
public class OptimalAccountBalancing {
    // Memoization cache for overlapping subproblems
    private Map<String, Integer> memo = new HashMap<>();

    /**
     * Finds the minimum number of transactions needed to settle all debts.
     *
     * @param transactions Array of transactions where transactions[i] = [from, to, amount]
     * @return Minimum number of transactions required to balance accounts
     */
    public int minTransfers(int[][] transactions) {
        if (transactions == null || transactions.length == 0) return 0;
        memo.clear();

        // Step 1: Compute net balance of each person
        Map<Integer, Integer> netBalanceMap = calculateNetBalances(transactions);

        // Step 2: Store only non-zero balances
        List<Integer> nonZeroBalances = netBalanceMap.values().stream()
            .filter(value -> value != 0)
            .collect(Collectors.toList());

        // Step 3: Use DFS with backtracking to minimize transactions
        return settleDebtsRecursively(nonZeroBalances, 0);
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
            int from = transaction[0];
            int to = transaction[1];
            int amount = transaction[2];

            balanceMap.put(from, balanceMap.getOrDefault(from, 0) - amount);
            balanceMap.put(to, balanceMap.getOrDefault(to, 0) + amount);
        }
        return balanceMap;
    }

    /**
     * Recursively settles debts using backtracking with pruning and memoization.
     * IMPROVED: Made the settlement logic explicit and added memoization.
     *
     * Algorithm:
     * 1. Find the first person with non-zero balance
     * 2. Try settling their debt with every other person with opposite sign
     * 3. EXPLICITLY mark the current person as settled (balance = 0)
     * 4. Recursively solve the reduced problem
     * 5. Backtrack and restore both balances
     *
     * Pruning optimizations:
     * - Skip people with zero balance
     * - Only match opposite signs (debtors with creditors)
     * - Use memoization to avoid recalculating same states
     *
     * @param balances list of current balances (modified during recursion)
     * @param currentIndex current position in settlement process
     * @return minimum transactions from this state
     */
    private int settleDebtsRecursively(List<Integer> balances, int currentIndex) {
        // Create memoization key
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = currentIndex; i < balances.size(); i++) {
            keyBuilder.append(balances.get(i)).append(",");
        }
        String memoKey = keyBuilder.toString();

        if (memo.containsKey(memoKey)) {
            return memo.get(memoKey);
        }

        // Skip people with zero balance (already settled)
        while (currentIndex < balances.size() && balances.get(currentIndex) == 0) {
            currentIndex++;
        }

        // Base case: all people are settled
        if (currentIndex == balances.size()) {
            memo.put(memoKey, 0);
            return 0;
        }

        int minTransactions = Integer.MAX_VALUE;

        // Try settling current person's debt with each subsequent person
        for (int partnerIndex = currentIndex + 1; partnerIndex < balances.size(); partnerIndex++) {
            // Only settle between people with opposite balance signs
            if (hasOppositeSign(balances.get(currentIndex), balances.get(partnerIndex))) {

                // EXPLICIT SETTLEMENT: Store original balances for backtracking
                int currentBalance = balances.get(currentIndex);
                int partnerBalance = balances.get(partnerIndex);

                // Perform the settlement:
                // 1. Transfer all debt from currentPerson to partner
                // 2. Mark currentPerson as completely settled
                balances.set(currentIndex, 0);  // settled!
                balances.set(partnerIndex, partnerBalance + currentBalance); // adjust partner's balance

                // Recursively solve remaining problem (skip to next person)
                int transactionsNeeded = 1 + settleDebtsRecursively(balances, currentIndex + 1);
                minTransactions = Math.min(minTransactions, transactionsNeeded);

                // Backtrack: Restore both balances
                balances.set(currentIndex, currentBalance);
                balances.set(partnerIndex, partnerBalance);
            }
        }

        memo.put(memoKey, minTransactions);
        return minTransactions;
    }

    // Checks if two balances have opposite signs (one positive, one negative)
    private boolean hasOppositeSign(int balance1, int balance2) {
        return (balance1 > 0 && balance2 < 0) || (balance1 < 0 && balance2 > 0);
    }

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

}
