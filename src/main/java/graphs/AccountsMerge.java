package graph;

import java.util.*;

/**
 * Problem: Accounts Merge
 *
 * Given a list of accounts where each element accounts[i] is a list of strings, where the first element
 * accounts[i][0] is a name, and the rest of the elements are emails representing emails of the account.
 * Now, we would like to merge these accounts. Two accounts definitely belong to the same person if there
 * is some common email to both accounts. After merging the accounts, return the accounts in the following
 * format: the first element of each account is the name, and the rest of the elements are emails in sorted order.
 *
 * Example:
 * Input: accounts = [["John","johnsmith@mail.com","john_newyork@mail.com"],["John","johnsmith@mail.com","john00@mail.com"],["Mary","mary@mail.com"],["John","johnnybravo@mail.com"]]
 * Output: [["John","john00@mail.com","john_newyork@mail.com","johnsmith@mail.com"],["Mary","mary@mail.com"],["John","johnnybravo@mail.com"]]
 *
 * LeetCode: https://leetcode.com/problems/accounts-merge
 *
 * Follow-up Questions:
 * 1. How would you handle millions of accounts efficiently?
 *    Answer: Use distributed Union-Find or MapReduce approach for parallel processing.
 *
 * 2. What if email addresses can have different formats (case sensitivity, aliases)?
 *    Answer: Normalize emails before processing (lowercase, remove dots in Gmail, etc.).
 *
 * 3. How would you detect and prevent malicious account merging?
 *    Answer: Add validation, rate limiting, and audit trails for account merging operations.
 *    Related: https://leetcode.com/problems/similar-string-groups/
 *
 * @author Sandeep
 */
public class AccountsMerge {

    /**
     * Merges accounts using Union-Find (Disjoint Set Union) data structure.
     *
     * Algorithm:
     * 1. Build email to name mapping and email to account index mapping
     * 2. Use Union-Find to group accounts that share common emails
     * 3. Group emails by their root parent in Union-Find structure
     * 4. Build final result with sorted emails for each group
     *
     * Time Complexity: O(n * α(n) + m log m) where n is total emails, m is unique emails
     * Space Complexity: O(n) for Union-Find structure and mappings
     *
     * @param accounts List of accounts, each starting with name followed by emails
     * @return List of merged accounts with sorted emails
     */
    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return new ArrayList<>();
        }

        UnionFind uf = new UnionFind(accounts.size());
        Map<String, String> emailToName = new HashMap<>();
        Map<String, Integer> emailToAccountId = new HashMap<>();

        // Build mappings and union accounts with common emails
        for (int i = 0; i < accounts.size(); i++) {
            List<String> account = accounts.get(i);
            String name = account.get(0);

            for (int j = 1; j < account.size(); j++) {
                String email = account.get(j);
                emailToName.put(email, name);

                if (emailToAccountId.containsKey(email)) {
                    // Union current account with the account that already has this email
                    uf.union(i, emailToAccountId.get(email));
                } else {
                    emailToAccountId.put(email, i);
                }
            }
        }

        // Group emails by their root parent
        Map<Integer, Set<String>> rootToEmails = new HashMap<>();
        for (Map.Entry<String, Integer> entry : emailToAccountId.entrySet()) {
            String email = entry.getKey();
            int accountId = entry.getValue();
            int root = uf.find(accountId);

            rootToEmails.computeIfAbsent(root, k -> new TreeSet<>()).add(email);
        }

        // Build final result
        List<List<String>> result = new ArrayList<>();
        for (Map.Entry<Integer, Set<String>> entry : rootToEmails.entrySet()) {
            Set<String> emails = entry.getValue();
            String name = emailToName.get(emails.iterator().next());

            List<String> mergedAccount = new ArrayList<>();
            mergedAccount.add(name);
            mergedAccount.addAll(emails);
            result.add(mergedAccount);
        }

        return result;
    }

    /**
     * Union-Find data structure for efficient set operations.
     */
    private static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX != rootY) {
                // Union by rank
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }
}