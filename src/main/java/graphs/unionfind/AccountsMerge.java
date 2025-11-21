package graphs.unionfind;

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
 * Input: accounts = [
 *      ["John","johnsmith@mail.com","john_newyork@mail.com"],
 *      ["John","johnsmith@mail.com","john00@mail.com"],
 *      ["Mary","mary@mail.com"],
 *      ["John","johnnybravo@mail.com"]
 * ]
 * Output: [
 *      ["John","john00@mail.com","john_newyork@mail.com","johnsmith@mail.com"],
 *      ["Mary","mary@mail.com"],
 *      ["John","johnnybravo@mail.com"]
 * ]
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
     * BFS method : Merges accounts using BFS to find connected components of emails.
     * Step-by-step:
     *  1. Build an undirected graph where each email is a node and edges exist between emails in the same account
     *  2. Use BFS to find all connected components in the email graph
     *  3. For each connected component, sort emails and prepend the account name
     * 
     * Key Insight:
     * Emails in the same connected component belong to the same person. BFS efficiently explores all reachable emails from a starting email.
     * Algorithm: Graph traversal using BFS.
     * 
     * Time Complexity: O(n * k log k) where n is number of accounts and k is average emails per account (log k for sorting emails).
     * Space Complexity: O(n * k) for graph storage and visited set.
     */
    public List<List<String>> accountsMergeBFS(List<List<String>> accounts) {
        Map<String, Set<String>> emailGraph = new HashMap<>(); // email -> connected emails
        Map<String, String> emailToName = new HashMap<>(); // email -> name
        
        // Build adjacency list (same as DFS approach)
        for (List<String> account : accounts) {
            String name = account.get(0);
            String firstEmail = account.get(1);
            
            for (int emailIndex = 1; emailIndex < account.size(); emailIndex++) {
                String email = account.get(emailIndex);
                emailToName.put(email, name);
                
                // Undirected graph connections
                emailGraph.putIfAbsent(firstEmail, new HashSet<>()).add(email);
                emailGraph.putIfAbsent(email, new HashSet<>()).add(firstEmail);
            }
        }
        
        // Find connected components using BFS
        Set<String> visited = new HashSet<>();
        List<List<String>> result = new ArrayList<>();
        
        for (String email : emailGraph.keySet()) {
            if (!visited.contains(email)) {
                List<String> connectedEmails = new ArrayList<>();
                Queue<String> queue = new LinkedList<>();
                
                queue.offer(email);
                visited.add(email);
                
                while (!queue.isEmpty()) {
                    String currentEmail = queue.poll();
                    connectedEmails.add(currentEmail);
                    
                    if (emailGraph.containsKey(currentEmail)) {
                        for (String neighbor : emailGraph.get(currentEmail)) {
                            if (!visited.contains(neighbor)) {
                                visited.add(neighbor);
                                queue.offer(neighbor);
                            }
                        }
                    }
                }
                
                // Sort and create merged account
                Collections.sort(connectedEmails);
                List<String> mergedAccount = new ArrayList<>();
                mergedAccount.add(emailToName.get(email));
                mergedAccount.addAll(connectedEmails);
                
                result.add(mergedAccount);
            }
        }
        
        return result;
    }

    /**
     * Main method: Merges accounts using Union-Find (Disjoint Set Union).
     * Step-by-step:
     *  1. Map each email to its first occurrence account index (email -> accountIndex)
     *  2. Initialize Union-Find structure with number of accounts
     *  3. For each account, union all its emails with first account having any of those emails
     *  4. Group emails by their root account (using findRoot operation)
     *  5. For each group, create merged account with name and sorted emails
     *
     * Key Insight:
     * Emails are the connection points between accounts. If two accounts share an email,
     * they belong to the same person (connected component). Union-Find efficiently handles
     * transitive connections (A connects to B, B connects to C => A connects to C).
     *
     * Algorithm: Union-Find with Path Compression and Union by Rank.
     * Time Complexity: O(n * k * α(n) + n * k * log k) where:
     *                  - n = number of accounts
     *                  - k = average emails per account
     *                  - α(n) = inverse Ackermann function (nearly constant)
     *                  - n*k*log k for sorting emails in merged accounts
     * Space Complexity: O(n * k) for email mappings and result storage.
     */
    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        int accountCount = accounts.size();
        UnionFind unionFind = new UnionFind(accountCount);
        
        // Map email to account index (first occurrence)
        Map<String, Integer> emailToAccount = new HashMap<>();
        
        // Build union-find structure
        for (int accountIndex = 0; accountIndex < accountCount; accountIndex++) {
            List<String> account = accounts.get(accountIndex);
            
            // Process all emails in this account (skip first element which is name)
            for (int emailIndex = 1; emailIndex < account.size(); emailIndex++) {
                String email = account.get(emailIndex);
                
                if (emailToAccount.containsKey(email)) {
                    // Email seen before - union with previous account
                    int previousAccount = emailToAccount.get(email);
                    unionFind.union(accountIndex, previousAccount);
                } else {
                    // First time seeing this email
                    emailToAccount.put(email, accountIndex);
                }
            }
        }
        
        // Group emails by root account
        Map<Integer, Set<String>> rootToEmails = new HashMap<>();
        for (int accountIndex = 0; accountIndex < accountCount; accountIndex++) {
            int root = unionFind.findRoot(accountIndex);
            rootToEmails.putIfAbsent(root, new HashSet<>());
            
            // Add all emails from this account to the root's email set
            List<String> account = accounts.get(accountIndex);
            for (int emailIndex = 1; emailIndex < account.size(); emailIndex++) {
                rootToEmails.get(root).add(account.get(emailIndex));
            }
        }
        
        // Build result with sorted emails
        List<List<String>> result = new ArrayList<>();
        for (Map.Entry<Integer, Set<String>> entry : rootToEmails.entrySet()) {
            int rootAccount = entry.getKey();
            String name = accounts.get(rootAccount).get(0);
            
            List<String> mergedAccount = new ArrayList<>();
            mergedAccount.add(name);
            
            // Add sorted emails
            List<String> sortedEmails = new ArrayList<>(entry.getValue());
            Collections.sort(sortedEmails);
            mergedAccount.addAll(sortedEmails);
            
            result.add(mergedAccount);
        }
        
        return result;
    }

    /**
     * Union-Find data structure for efficient set operations.
     */
    private static class UnionFind {
        private int[] parent; // parent[i] points to the parent of i
        private int[] rank; // rank[i] is the depth of the tree rooted at i

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        public int findRoot(int account) {
            if (parent[account] != account) {
                parent[account] = findRoot(parent[account]); // Path compression
            }
            return parent[account];
        }

        public void union(int accountX, int accountY) {
            int rootX = findRoot(accountX);
            int rootY = findRoot(accountY);

            if (rootX != rootY) {
                // Attach smaller rank tree under root of higher rank tree
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