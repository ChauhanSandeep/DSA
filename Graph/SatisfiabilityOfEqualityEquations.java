package Graph;

/**
 * Problem: Satisfiability of Equality Equations
 * 
 * You are given an array of strings equations that represent relationships between variables.
 * Each string equations[i] has length 4 and takes one of two different forms: "a==b" or "a!=b".
 * Here, a and b are lowercase letters (not necessarily different) that represent one-letter variable names.
 * 
 * Return true if it is possible to assign integers to variable names so as to satisfy all the given equations, or false otherwise.
 * 
 * Example:
 * Input: ["a==b","b!=a"]
 * Output: false
 * Explanation: If we assign say, a = 1 and b = 1, then the first equation is satisfied, but not the second.
 * 
 * LeetCode: https://leetcode.com/problems/satisfiability-of-equality-equations
 * 
 * Time Complexity: O(n) where n is the number of equations
 * Space Complexity: O(1) since we have a fixed number of letters (26)
 */
public class SatisfiabilityOfEqualityEquations {
    private int[] parent;
    
    public boolean equationsPossible(String[] equations) {
        parent = new int[26];
        for (int i = 0; i < 26; i++) {
            parent[i] = i;
        }
        
        // First pass: Union all equal variables
        for (String eq : equations) {
            if (eq.charAt(1) == '=') {
                int x = eq.charAt(0) - 'a';
                int y = eq.charAt(3) - 'a';
                union(x, y);
            }
        }
        
        // Second pass: Check all inequalities
        for (String eq : equations) {
            if (eq.charAt(1) == '!') {
                int x = eq.charAt(0) - 'a';
                int y = eq.charAt(3) - 'a';
                if (find(x) == find(y)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    private void union(int x, int y) {
        parent[find(x)] = find(y);
    }
}
