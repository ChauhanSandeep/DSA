package graphs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;


/**
 * Problem: Optimal Account Balancing
 *
 * Given a list of money transfers between people, settle everyone so each
 * person's final net balance becomes zero. Return the fewest new transactions
 * needed, not the actual list of payments.
 *
 * Leetcode: https://leetcode.com/problems/optimal-account-balancing/
 * Rating:   acceptance 50.6% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | Backtracking | Debt netting with memoization
 *
 * Example:
 *   Input:  transactions = [[0,1,10],[2,0,5]]
 *   Output: 2
 *   Why:    person 0 is owed 5 overall, person 1 owes 10, and person 2 is owed 5,
 *           so person 1 must pay two different creditors.
 *
 * Follow-ups:
 *   1. Return the actual settlement transactions?
 *      Store the chosen pair at each recursive step and replay the best branch.
 *   2. Minimize total transferred amount instead of transaction count?
 *      Net balances already minimize total money; the optimization target changes the search state.
 *   3. Add per-person limits on who can pay whom?
 *      Treat each attempted settlement as a constrained edge and skip infeasible pairs.
 *   4. Scale to thousands of people?
 *      Exact search is exponential; use greedy matching or min-cost flow approximations.
 */
public class OptimalAccountBalancing {

    public static void main(String[] args) {
        OptimalAccountBalancing solver = new OptimalAccountBalancing();
        int[][][] inputs = {{{0, 1, 10}, {2, 0, 5}}, {{0, 1, 10}, {1, 0, 10}}, {}};
        int[] expected = {2, 0, 0};
        for (int i = 0; i < inputs.length; i++) {
            int output = solver.minTransfers(inputs[i]);
            System.out.printf("transactions=%s -> %d  expected=%d%n", Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }
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
        Map<Integer, Integer> personBalanceMap = calculateNetBalances(transactions);

        // Step 2: Store only non-zero balances
        List<Integer> nonZeroBalances = personBalanceMap.values().stream()
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


}
