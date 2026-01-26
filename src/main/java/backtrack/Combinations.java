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
 * Explanation: There are 4 numbers, we need to choose 2 => 6 total combinations.
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
 * LeetCode Contest Rating: Not available (not a contest problem)
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

    private void backtrack(int maxNumber, int combinationSize, int start, List<Integer> current, List<List<Integer>> result) {
        // Base case: found a valid combinationSize-sized combination
        if (current.size() == combinationSize) {
            result.add(new ArrayList<>(current));
            return;
        }

        // Pruning: if not enough numbers remaining, stop early
        int numbersStillNeeded = combinationSize - current.size();
        int numbersAvailable = maxNumber - start + 1;

        for (int i = start; i <= maxNumber; i++) {
            // Optimization: stop if remaining numbers insufficient
            if (numbersAvailable < numbersStillNeeded) {
                break;
            }

            current.add(i);
            backtrack(maxNumber, combinationSize, i + 1, current, result);
            current.remove(current.size() - 1);

            numbersAvailable--;
        }
    }
}