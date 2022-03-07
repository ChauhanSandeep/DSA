package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Find min transactions required to balance accounts in splitwise
 * https://leetcode.com/problems/optimal-account-balancing/
 * Splitwise
 */
public class OptimalAccountBalancing {

    public static void main(String[] args) {
        int[][] transactions = {
                {0, 1, 10}, // [from, to, amount]
                {1, 0, 1},
                {1, 2, 5},
                {2, 0, 5}};
        int minTransfers = new OptimalAccountBalancing().minTransfers(transactions);
        System.out.println(minTransfers);
    }

    int min;

    public int minTransfers(int[][] transactions) {
        if (transactions == null || transactions.length == 0) return 0;
        min = transactions.length;

        Map<Integer, Integer> userBalance = new HashMap<>();
        for (int[] transaction : transactions) {
            int amt = transaction[2];
            int from = transaction[0];
            int to = transaction[1];

            userBalance.put(from, userBalance.getOrDefault(from, 0) + amt);
            userBalance.put(to, userBalance.getOrDefault(to, 0) - amt);
        }

        List<Integer> positives = new ArrayList<>();
        List<Integer> negatives = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : userBalance.entrySet()) {
            int val = entry.getValue();

            if (val == 0) continue;
            if (val > 0) positives.add(val);
            if (val < 0) negatives.add(val);
        }

        dfs(positives, negatives, 0);
        return min;

    }

    public void dfs(List<Integer> positives, List<Integer> negatives, int curr) {
        if (positives.size() == 0 && negatives.size() == 0) {
            min = Math.min(min, curr);
            return;
        }
        if (curr > min) return;

        int positive = positives.get(0);

        // match every positive and negative account
        for (int i = 0; i < negatives.size(); i++) {
            // new positive and new negative after doing transaction
            int negative = negatives.get(i);
            int nPositive = Math.max(positive + negative, 0);
            int nNegative = Math.min(positive + negative, 0);

            // update positives and negatives list
            if (nPositive == 0) {
                positives.remove(0);
            } else {
                positives.set(0, nPositive);
            }

            if (nNegative == 0) {
                negatives.remove(i);
            } else {
                negatives.set(i, nNegative);
            }

            // dfs for remaining
            dfs(positives, negatives, curr + 1);

            // backtrack. keep values back
            if (nPositive == 0) {
                positives.add(0, positive);
            } else {
                positives.set(0, positive);
            }

            if (nNegative == 0) {
                negatives.add(i, negative);
            } else {
                negatives.set(i, negative);
            }
        }
    }
}
