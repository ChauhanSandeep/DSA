package graphs.unionfind;

import java.util.*;

/**
 * Problem: Accounts Merge
 *
 * Accounts contain a name followed by email addresses. Accounts sharing at least
 * one email belong to the same person and should be merged, with emails sorted in
 * the merged result.
 *
 * Leetcode: https://leetcode.com/problems/accounts-merge/ (Medium)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Graph | Union-Find | Connected components of emails/accounts
 *
 * Example:
 *   Input:  [["John","a@mail.com","b@mail.com"],["John","b@mail.com","c@mail.com"]]
 *   Output: [["John","a@mail.com","b@mail.com","c@mail.com"]]
 *   Why:    the shared email b@mail.com connects both accounts into one component.
 *
 * Follow-ups:
 *   1. Handle millions of accounts?
 *      Stream the email-to-account map and union operations, or shard by normalized email hash.
 *   2. Emails have aliases or case differences?
 *      Normalize addresses before building graph or Union-Find connections.
 *   3. Need to prevent unsafe merges?
 *      Require verified email ownership and keep an audit trail for every merge decision.
 *
 * Related: Similar String Groups (839), Number of Provinces (547), Redundant Connection (684).
 */
public class AccountsMerge {

    public static void main(String[] args) {
        AccountsMerge solver = new AccountsMerge();
        List<List<String>> accounts1 = Arrays.asList(
            Arrays.asList("John", "johnsmith@mail.com", "john_newyork@mail.com"),
            Arrays.asList("John", "johnsmith@mail.com", "john00@mail.com"));
        List<List<String>> accounts2 = Arrays.asList(
            Arrays.asList("Mary", "mary@mail.com"));

        System.out.printf("accounts=%s -> %s  expected=[[John, john00@mail.com, john_newyork@mail.com, johnsmith@mail.com]]%n",
            accounts1, solver.accountsMerge(accounts1));
        System.out.printf("accounts=%s -> %s  expected=[[Mary, mary@mail.com]]%n",
            accounts2, solver.accountsMerge(accounts2));
    }

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
            String primaryEmail = account.get(1);
            
            for (int i = 1; i < account.size(); i++) {
                String email = account.get(i);
                emailToName.put(email, name);
                
                // Undirected graph connections
                emailGraph.putIfAbsent(primaryEmail, new HashSet<>()).add(email);
                emailGraph.putIfAbsent(email, new HashSet<>()).add(primaryEmail);
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
     * Intuition: shared emails connect accounts into components. Union-Find merges
     * account indices whenever an email is seen in more than one account; after all
     * unions, each root owns the sorted union of emails in its component.
     *
     * Algorithm:
     *   1. Map each email to its first seen account index.
     *   2. Union the current account with the previous account whenever an email repeats.
     *   3. Group every account's emails under its root account.
     *   4. Sort each root's email set and prepend the root account name.
     *
     * Time:  O(A * E * a(A) + M log M) - email scans plus sorting merged email groups.
     * Space: O(M + A) - email maps, grouped email sets, and Union-Find arrays.
     *
     * @param accounts account rows with name followed by emails
     * @return merged accounts with sorted emails
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