package hashing;

import java.util.*;

/**
 * 1169. Invalid Transactions
 *
 * Problem: A transaction is invalid if: amount exceeds $1000, or it occurs within
 * 60 minutes of another transaction in a different city by the same person.
 * Return a list of invalid transactions.
 *
 * Example:
 * Input: transactions = ["alice,20,800,mtv","alice,50,100,beijing"]
 * Output: ["alice,20,800,mtv","alice,50,100,beijing"]
 * Explanation: First transaction is valid. Second is invalid (different city within 60 minutes).
 *
 * LeetCode: https://leetcode.com/problems/invalid-transactions
 *
 * Follow-up questions:
 * Q: How to optimize for millions of transactions?
 * A: Use time-based indexing, spatial hashing, or sliding window approaches.
 *
 * Q: What if we need real-time fraud detection?
 * A: Use streaming algorithms with time-window data structures.
 *
 * Q: How to handle timezone differences?
 * A: Normalize all timestamps to UTC before processing.
 * LeetCode Contest Rating: 1659
 **/
public class InvalidTransactions {

    /**
     * Validates transactions based on amount and location/time constraints.
     *
     * Algorithm:
     * - Parse each transaction into structured data
     * - Check amount > 1000 for immediate invalidation
     * - For each transaction, check others by same person within 60 minutes
     * - If different cities within time window, mark both invalid
     *
     * Time Complexity: O(n^2) where n is number of transactions
     * Space Complexity: O(n) for parsing and result storage
     */
    public List<String> invalidTransactions(String[] transactions) {
        List<Transaction> parsed = new ArrayList<>();
        Set<String> invalid = new HashSet<>();

        // Parse all transactions
        for (String trans : transactions) {
            parsed.add(new Transaction(trans));
        }

        // Check each transaction
        for (int i = 0; i < parsed.size(); i++) {
            Transaction t1 = parsed.get(i);

            // Check amount threshold
            if (t1.amount > 1000) {
                invalid.add(transactions[i]);
            }

            // Check against other transactions by same person
            for (int j = 0; j < parsed.size(); j++) {
                if (i == j) continue;

                Transaction t2 = parsed.get(j);

                // Same person, different city, within 60 minutes
                if (t1.name.equals(t2.name) &&
                    !t1.city.equals(t2.city) &&
                    Math.abs(t1.time - t2.time) <= 60) {
                    invalid.add(transactions[i]);
                    invalid.add(transactions[j]);
                }
            }
        }

        return new ArrayList<>(invalid);
    }

    // Transaction data structure
    private static class Transaction {
        String name;
        int time;
        int amount;
        String city;

        Transaction(String transaction) {
            String[] parts = transaction.split(",");
            this.name = parts[0];
            this.time = Integer.parseInt(parts[1]);
            this.amount = Integer.parseInt(parts[2]);
            this.city = parts[3];
        }
    }

    /**
     * Optimized approach using grouping by person name.
     * Reduces comparisons by only checking within same person's transactions.
     */
    public List<String> invalidTransactionsOptimized(String[] transactions) {
        Map<String, List<Integer>> personTransactions = new HashMap<>();
        List<Transaction> parsed = new ArrayList<>();
        Set<Integer> invalidIndices = new HashSet<>();

        // Parse and group by person
        for (int i = 0; i < transactions.length; i++) {
            Transaction t = new Transaction(transactions[i]);
            parsed.add(t);
            personTransactions.computeIfAbsent(t.name, k -> new ArrayList<>()).add(i);
        }

        // Check each person's transactions
        for (String person : personTransactions.keySet()) {
            List<Integer> indices = personTransactions.get(person);

            for (int i : indices) {
                Transaction t1 = parsed.get(i);

                // Check amount
                if (t1.amount > 1000) {
                    invalidIndices.add(i);
                }

                // Check against other transactions by same person
                for (int j : indices) {
                    if (i == j) continue;

                    Transaction t2 = parsed.get(j);

                    if (!t1.city.equals(t2.city) && Math.abs(t1.time - t2.time) <= 60) {
                        invalidIndices.add(i);
                        invalidIndices.add(j);
                    }
                }
            }
        }

        List<String> result = new ArrayList<>();
        for (int i : invalidIndices) {
            result.add(transactions[i]);
        }

        return result;
    }

    /**
     * Time-window approach using sorting for efficiency.
     * Sorts transactions by time to optimize time-based comparisons.
     */
    public List<String> invalidTransactionsTimeWindow(String[] transactions) {
        List<TransactionWithIndex> indexed = new ArrayList<>();
        Set<Integer> invalid = new HashSet<>();

        // Parse with original indices
        for (int i = 0; i < transactions.length; i++) {
            indexed.add(new TransactionWithIndex(transactions[i], i));
        }

        // Group by person and sort by time
        Map<String, List<TransactionWithIndex>> byPerson = new HashMap<>();
        for (TransactionWithIndex t : indexed) {
            byPerson.computeIfAbsent(t.name, k -> new ArrayList<>()).add(t);
        }

        // Process each person's transactions
        for (List<TransactionWithIndex> personTrans : byPerson.values()) {
            personTrans.sort((a, b) -> Integer.compare(a.time, b.time));

            for (int i = 0; i < personTrans.size(); i++) {
                TransactionWithIndex t1 = personTrans.get(i);

                // Check amount
                if (t1.amount > 1000) {
                    invalid.add(t1.originalIndex);
                }

                // Check time window (only need to check forward due to sorting)
                for (int j = i + 1; j < personTrans.size(); j++) {
                    TransactionWithIndex t2 = personTrans.get(j);

                    if (t2.time - t1.time > 60) {
                        break; // No more transactions within window
                    }

                    if (!t1.city.equals(t2.city)) {
                        invalid.add(t1.originalIndex);
                        invalid.add(t2.originalIndex);
                    }
                }
            }
        }

        List<String> result = new ArrayList<>();
        for (int i : invalid) {
            result.add(transactions[i]);
        }

        return result;
    }

    // Transaction with original index tracking
    private static class TransactionWithIndex {
        String name;
        int time;
        int amount;
        String city;
        int originalIndex;

        TransactionWithIndex(String transaction, int index) {
            String[] parts = transaction.split(",");
            this.name = parts[0];
            this.time = Integer.parseInt(parts[1]);
            this.amount = Integer.parseInt(parts[2]);
            this.city = parts[3];
            this.originalIndex = index;
        }
    }

    /**
     * Streaming approach for real-time fraud detection.
     * Maintains sliding window of recent transactions per person.
     */
    public List<String> invalidTransactionsStreaming(String[] transactions) {
        Map<String, TreeMap<Integer, List<TransactionWithIndex>>> personTimeMap = new HashMap<>();
        Set<Integer> invalid = new HashSet<>();

        for (int i = 0; i < transactions.length; i++) {
            TransactionWithIndex current = new TransactionWithIndex(transactions[i], i);

            // Check amount
            if (current.amount > 1000) {
                invalid.add(i);
            }

            // Get person's transaction history
            TreeMap<Integer, List<TransactionWithIndex>> timeMap =
                personTimeMap.computeIfAbsent(current.name, k -> new TreeMap<>());

            // Check transactions in 60-minute window
            int startTime = current.time - 60;
            int endTime = current.time + 60;

            for (Map.Entry<Integer, List<TransactionWithIndex>> entry :
                 timeMap.subMap(startTime, true, endTime, true).entrySet()) {

                for (TransactionWithIndex t : entry.getValue()) {
                    if (!current.city.equals(t.city)) {
                        invalid.add(current.originalIndex);
                        invalid.add(t.originalIndex);
                    }
                }
            }

            // Add current transaction to history
            timeMap.computeIfAbsent(current.time, k -> new ArrayList<>()).add(current);
        }

        List<String> result = new ArrayList<>();
        for (int i : invalid) {
            result.add(transactions[i]);
        }

        return result;
    }

    /**
     * Parallel processing approach for large datasets.
     * Uses multiple threads to process different people concurrently.
     */
    public List<String> invalidTransactionsParallel(String[] transactions) {
        // Group by person first
        Map<String, List<Integer>> personTransactions = new HashMap<>();
        List<Transaction> parsed = new ArrayList<>();

        for (int i = 0; i < transactions.length; i++) {
            Transaction t = new Transaction(transactions[i]);
            parsed.add(t);
            personTransactions.computeIfAbsent(t.name, k -> new ArrayList<>()).add(i);
        }

        // Process each person's transactions in parallel
        Set<Integer> invalid = java.util.concurrent.ConcurrentHashMap.newKeySet();

        personTransactions.entrySet().parallelStream().forEach(entry -> {
            List<Integer> indices = entry.getValue();

            for (int i : indices) {
                Transaction t1 = parsed.get(i);

                if (t1.amount > 1000) {
                    invalid.add(i);
                }

                for (int j : indices) {
                    if (i == j) continue;

                    Transaction t2 = parsed.get(j);

                    if (!t1.city.equals(t2.city) && Math.abs(t1.time - t2.time) <= 60) {
                        invalid.add(i);
                        invalid.add(j);
                    }
                }
            }
        });

        List<String> result = new ArrayList<>();
        for (int i : invalid) {
            result.add(transactions[i]);
        }

        return result;
    }

    /**
     * Memory-optimized approach for very large transaction sets.
     * Processes transactions in batches to control memory usage.
     */
    public List<String> invalidTransactionsBatched(String[] transactions) {
        Set<String> invalid = new HashSet<>();
        int batchSize = 1000; // Process in batches

        for (int start = 0; start < transactions.length; start += batchSize) {
            int end = Math.min(start + batchSize, transactions.length);

            // Process current batch against all transactions
            for (int i = start; i < end; i++) {
                Transaction t1 = new Transaction(transactions[i]);

                if (t1.amount > 1000) {
                    invalid.add(transactions[i]);
                }

                // Check against all other transactions (not just batch)
                for (int j = 0; j < transactions.length; j++) {
                    if (i == j) continue;

                    Transaction t2 = new Transaction(transactions[j]);

                    if (t1.name.equals(t2.name) &&
                        !t1.city.equals(t2.city) &&
                        Math.abs(t1.time - t2.time) <= 60) {
                        invalid.add(transactions[i]);
                        invalid.add(transactions[j]);
                    }
                }
            }
        }

        return new ArrayList<>(invalid);
    }
}