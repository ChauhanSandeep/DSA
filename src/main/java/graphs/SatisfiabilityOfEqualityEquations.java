package graphs;

import java.util.Arrays;

/**
 * Problem: Satisfiability of Equality Equations
 *
 * Each equation says two lowercase variables are equal or not equal. Decide
 * whether there is any assignment of values to variables that satisfies all
 * equations at the same time.
 *
 * Leetcode: https://leetcode.com/problems/satisfiability-of-equality-equations/
 * Rating:   1638 (zerotrac Elo)
 * Pattern:  Graph | Union-Find | Equality components before inequality checks
 *
 * Example:
 *   Input:  equations = ["a==b","b!=a"]
 *   Output: false
 *   Why:    the equality forces a and b into the same group, but the inequality
 *           then demands that the same two variables be different.
 *
 * Follow-ups:
 *   1. Support more than 26 variable names?
 *      Map each variable string to a compact integer id before using Union-Find.
 *   2. Return one contradictory equation?
 *      After unioning equalities, return the first inequality whose endpoints share a root.
 *   3. Add constraints like a < b?
 *      Use graph ordering or difference constraints instead of plain Union-Find.
 *
 * Related: Evaluate Division (399), Possible Bipartition (886), Sentence Similarity II (737).
 */
public class SatisfiabilityOfEqualityEquations {

    public static void main(String[] args) {
        SatisfiabilityOfEqualityEquations solver = new SatisfiabilityOfEqualityEquations();
        String[][] inputs = {{"a==b", "b!=a"}, {"b==a", "a==b"}};
        boolean[] expected = {false, true};
        for (int i = 0; i < inputs.length; i++) {
            boolean output = solver.equationsPossible(inputs[i]);
            System.out.printf("equations=%s -> %b  expected=%b%n", Arrays.toString(inputs[i]), output, expected[i]);
        }
    }
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
