package hashing;

import java.util.*;

/**
 * Problem: Invalid Transactions
 *
 * A transaction is invalid when its amount is over 1000, or when another
 * transaction by the same person occurs within 60 minutes in a different city.
 * Return every transaction string that satisfies either invalid condition.
 *
 * Leetcode: https://leetcode.com/problems/invalid-transactions/ (Medium)
 * Rating:   1659
 * Pattern:  Hashing | Parsing | Pair comparison by user
 *
 * Example:
 *   Input:  ["alice,20,800,mtv", "alice,50,100,beijing"]
 *   Output: ["alice,20,800,mtv", "alice,50,100,beijing"]
 *   Why:    the same person appears in different cities only 30 minutes apart,
 *           so both transactions are invalid.
 *
 * Follow-ups:
 *   1. How would you scale this when each user has many transactions?
 *      Group by user, sort by time, and compare only transactions inside a 60-minute window.
 *   2. How would you support real-time fraud alerts?
 *      Keep recent transactions per user in a time-indexed sliding window.
 *   3. How would you preserve output order?
 *      Track invalid indices in a boolean array and scan the original input at the end.
 *   4. How should time zones be handled in production data?
 *      Normalize all event times to one canonical timeline before applying the rule.
 *
 * Related: Alert Using Same Key-Card Three or More Times in a One Hour Period (1604).
 */
public class InvalidTransactions {

    public static void main(String[] args) {
        InvalidTransactions solver = new InvalidTransactions();
        String[][] cases = {
            { "alice,20,800,mtv", "alice,50,100,beijing" },
            { "bob,689,1910,barcelona" }
        };
        String[] expected = {
            "[alice,20,800,mtv, alice,50,100,beijing]",
            "[bob,689,1910,barcelona]"
        };

        for (int i = 0; i < cases.length; i++) {
            List<String> got = solver.invalidTransactions(cases[i]);
            Collections.sort(got);
            System.out.printf("transactions=%s -> %s  expected=%s%n",
                Arrays.toString(cases[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: every invalid rule can be checked after parsing the transaction
     * fields. The amount rule is local to one transaction, while the city rule
     * compares that transaction against every other transaction by the same name.
     *
     * Algorithm:
     *   1. Parse every transaction string into a Transaction object.
     *   2. Mark transactions whose amount is greater than 1000.
     *   3. Compare each pair and mark both when name matches, city differs, and times are within 60 minutes.
     *
     * Time:  O(n^2) - every transaction may be compared with every other transaction.
     * Space: O(n) - parsed transactions and the invalid set grow with the input.
     *
     * @param transactions raw transaction strings in name,time,amount,city format
     * @return transaction strings that are invalid by either rule
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