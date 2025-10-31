package backtrack;

import java.util.*;

/**
 * Problem: Combinations
 * 
 * Given two integers n and k, return all possible combinations of k numbers
 * chosen from the range [1, n]. You may return the answer in any order.
 * 
 * Example:
 * Input: n = 4, k = 2
 * Output: [[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
 * Explanation: There are 4 choose 2 = 6 total combinations.
 * Note that combinations are unordered, i.e., [1,2] and [2,1] are the same.
 * 
 * Constraints:
 * - 1 <= n <= 20
 * - 1 <= k <= n
 * 
 * LeetCode Problem: https://leetcode.com/problems/combinations
 * 
 * Follow-up Questions:
 * 
 * 1. How would you generate combinations with repetition allowed?
 *    Answer: Remove the restriction of starting from start+1. In the recursive call,
 *    use start instead of start+1, allowing the same number to be picked multiple times.
 *    Related problem: https://leetcode.com/problems/combination-sum/
 * 
 * 2. What if you need to generate all possible subsets (all lengths) instead of just k?
 *    Answer: Don't wait for current.size() == k. Add current to result at every
 *    recursive call, giving all subsets from size 0 to n.
 *    Related problem: https://leetcode.com/problems/subsets/
 * 
 * 3. How would you optimize for very large n with small k?
 *    Answer: The pruning optimization is crucial. Also consider iterative approach
 *    with bit manipulation for small k, or use combinatorial formulas to generate
 *    the i-th combination directly without generating all previous ones.
 * 
 * 4. Can you generate combinations in lexicographical order without sorting?
 *    Answer: Yes, the backtracking approach with start pointer naturally generates
 *    combinations in lexicographical order as we always proceed in increasing order.
 * 
 * 5. How would you modify this to handle negative numbers or arbitrary ranges?
 *    Answer: Instead of using [1, n], accept an array of numbers as input. Iterate
 *    through array indices instead of number values. The backtracking structure remains same.
 */
public class Combinations {
    
    /**
     * Generates all k-sized combinations using backtracking with pruning.
     * 
     * Algorithm:
     * 1. Start with empty combination and first number (1)
     * 2. At each step, try adding numbers from current position to n
     * 3. When combination reaches size k, add to result
     * 4. Backtrack by removing last added number and trying next option
     * 5. Prune branches where remaining numbers can't form k-sized combination
     * 
     * Key insight: Use start pointer to avoid duplicates. Once we pick number i,
     * all future picks must be > i. Pruning check: if (n - start + 1 < k - current.size()),
     * we don't have enough numbers left, so skip this branch.
     * 
     * Time Complexity: O(C(n,k) * k) where C(n,k) is binomial coefficient n!/(k!(n-k)!).
     * We generate C(n,k) combinations, each requiring O(k) to copy to result.
     * 
     * Space Complexity: O(C(n,k) * k) for storing all combinations. Recursion depth
     * is O(k) for the call stack.
     * 
     * @param n upper bound of range [1, n]
     * @param k size of each combination
     * @return list of all k-sized combinations
     */
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(n, k, 1, new ArrayList<>(), result);
        return result;
    }
    
    // Helper method for backtracking with pruning
    private void backtrack(int n, int k, int start, List<Integer> current, List<List<Integer>> result) {
        // Base case: found a valid k-sized combination
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }
        
        // Pruning: if not enough numbers remaining, stop early
        int needed = k - current.size();
        int available = n - start + 1;
        
        for (int i = start; i <= n; i++) {
            // Optimization: stop if remaining numbers insufficient
            if (available < needed) {
                break;
            }
            
            current.add(i);
            backtrack(n, k, i + 1, current, result);
            current.remove(current.size() - 1);
            
            available--;
        }
    }
    
    /**
     * Alternative iterative approach using bit manipulation for small k.
     * Works well when k is small (k <= 3 or 4).
     * 
     * Algorithm:
     * 1. Use k nested loops to generate combinations
     * 2. Each loop variable represents one position in combination
     * 3. Ensure each subsequent number is greater than previous
     * 
     * Time Complexity: O(C(n,k) * k) same as backtracking.
     * 
     * Space Complexity: O(C(n,k) * k) for result storage.
     * 
     * Note: This approach becomes impractical for larger k due to nested loops.
     * Shown here for k=2 as an example.
     * 
     * @param n upper bound of range [1, n]
     * @param k size of each combination (must be 2 for this implementation)
     * @return list of all k-sized combinations
     */
    public List<List<Integer>> combineIterative(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        
        if (k == 2) {
            for (int i = 1; i <= n - 1; i++) {
                for (int j = i + 1; j <= n; j++) {
                    List<Integer> combination = new ArrayList<>();
                    combination.add(i);
                    combination.add(j);
                    result.add(combination);
                }
            }
        }
        // For k > 2, would need more nested loops - not scalable
        
        return result;
    }
    
    /**
     * Cleaner backtracking version without explicit pruning check.
     * Relies on natural loop bounds for termination.
     * 
     * Algorithm:
     * Same backtracking approach but simpler code without explicit pruning.
     * The loop condition i <= n naturally handles the bounds.
     * 
     * Time Complexity: O(C(n,k) * k) slightly slower than pruned version.
     * 
     * Space Complexity: O(C(n,k) * k) for result storage.
     * 
     * @param n upper bound of range [1, n]
     * @param k size of each combination
     * @return list of all k-sized combinations
     */
    public List<List<Integer>> combineSimple(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackSimple(n, k, 1, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrackSimple(int n, int k, int start, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }
        
        for (int i = start; i <= n; i++) {
            current.add(i);
            backtrackSimple(n, k, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}