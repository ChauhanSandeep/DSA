package stacksandqueues;

import java.util.*;

/**
 * Problem: Nested List Weight Sum
 *
 * A nested list contains integers or more nested lists. Each integer contributes
 * value * depth, where top-level integers have depth 1 and each nested list
 * increases depth by one. Return the total weighted sum.
 *
 * Leetcode: https://leetcode.com/problems/nested-list-weight-sum/ (Medium)
 * Rating:   acceptance 86.0% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  DFS | BFS | Depth tracking
 *
 * Example:
 *   Input:  nestedList = [[1,1],2,[1,1]]
 *   Output: 10
 *   Why:    four 1s each count twice, and the top-level 2 counts once, for 1*2*4 + 2 = 10.
 *
 * Follow-ups:
 *   1. Weight by reverse depth instead?
 *      First find max depth, then use maxDepth - depth + 1 for each integer.
 *   2. The nesting depth can exceed the call-stack limit?
 *      Use the iterative queue/stack traversal and store depth explicitly.
 *   3. Need to parse from a string like "[123,[4,[6]]]"?
 *      Build NestedInteger objects with a parser stack, then run the same traversal.
 *   4. Need repeated queries after updates to nested lists?
 *      Cache subtree weighted sums and depths, invalidating only changed ancestors.
 *
 * Related: Nested List Weight Sum II (364), Mini Parser (385).
 */
public class NestedListWeightSum {

    // This is the interface that allows for creating nested lists
    public interface NestedInteger {
        // @return true if this NestedInteger holds a single integer, rather than a nested list
        public boolean isInteger();

        // @return the single integer that this NestedInteger holds, if it holds a single integer
        // Return null if this NestedInteger holds a nested list
        public Integer getInteger();

        // @return the nested list that this NestedInteger holds, if it holds a nested list
        // Return empty list if this NestedInteger holds a single integer
        public List<NestedInteger> getList();
    }

        /**
     * Intuition: recursion mirrors the nested structure. The current call knows
     * the current depth, adds integer values multiplied by that depth, and sends
     * child lists to the same logic with depth + 1.
     *
     * Algorithm:
     *   1. Return 0 for null or empty input.
     *   2. Start depth-first calculation at depth 1.
     *   3. Add integer value * depth for integers.
     *   4. Recurse into nested lists with depth + 1.
     *
     * Time:  O(n) - each nested element is visited once.
     * Space: O(d) - recursion depth is maximum nesting depth.
     *
     * @param nestedList list of nested integers
     * @return sum of integers weighted by depth
     */
public int depthSum(List<NestedInteger> nestedList) {
        if (nestedList == null || nestedList.isEmpty()) {
            return 0;
        }

        return calculateDepthSum(nestedList, 1);
    }

    // Helper method for recursive calculation
    private int calculateDepthSum(List<NestedInteger> nestedList, int depth) {
        int sum = 0;

        for (NestedInteger element : nestedList) {
            if (element.isInteger()) {
                // Add integer value multiplied by its depth
                sum += element.getInteger() * depth;
            } else {
                // Recursively process nested list with incremented depth
                sum += calculateDepthSum(element.getList(), depth + 1);
            }
        }

        return sum;
    }

    /**
     * Iterative approach using explicit stack to avoid recursion.
     * Better for very deep nested structures.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(w) where w is maximum width at any level
     */
    public int depthSumIterative(List<NestedInteger> nestedList) {
        if (nestedList == null || nestedList.isEmpty()) {
            return 0;
        }

        Queue<NestedIntegerWithDepth> queue = new LinkedList<>();

        // Initialize queue with first level elements
        for (NestedInteger element : nestedList) {
            queue.offer(new NestedIntegerWithDepth(element, 1));
        }

        int totalSum = 0;

        while (!queue.isEmpty()) {
            NestedIntegerWithDepth current = queue.poll();

            if (current.nestedInteger.isInteger()) {
                totalSum += current.nestedInteger.getInteger() * current.depth;
            } else {
                // Add all elements of nested list to queue with incremented depth
                for (NestedInteger element : current.nestedInteger.getList()) {
                    queue.offer(new NestedIntegerWithDepth(element, current.depth + 1));
                }
            }
        }

        return totalSum;
    }

    // Helper classes for advanced functionality
    static class NestedIntegerWithDepth {
        NestedInteger nestedInteger;
        int depth;

        NestedIntegerWithDepth(NestedInteger nestedInteger, int depth) {
            this.nestedInteger = nestedInteger;
            this.depth = depth;
        }
    }

    public static void main(String[] args) {
        class SimpleNestedInteger implements NestedInteger {
            private final Integer value; private final List<NestedInteger> list;
            SimpleNestedInteger(int value) { this.value = value; this.list = null; }
            SimpleNestedInteger(List<NestedInteger> list) { this.value = null; this.list = list; }
            public boolean isInteger() { return value != null; }
            public Integer getInteger() { return value; }
            public List<NestedInteger> getList() { return list == null ? Collections.emptyList() : list; }
        }
        NestedListWeightSum solver = new NestedListWeightSum();
        List<List<NestedInteger>> inputs = Arrays.asList(Collections.emptyList(), Arrays.asList(new SimpleNestedInteger(Arrays.asList(new SimpleNestedInteger(1), new SimpleNestedInteger(1))), new SimpleNestedInteger(2), new SimpleNestedInteger(Arrays.asList(new SimpleNestedInteger(1), new SimpleNestedInteger(1)))));
        int[] expected = {0, 10};
        for (int i = 0; i < inputs.size(); i++) {
            int got = solver.depthSum(inputs.get(i));
            System.out.printf("case=%d -> %d  expected=%d%n", i + 1, got, expected[i]);
        }
    }
}
