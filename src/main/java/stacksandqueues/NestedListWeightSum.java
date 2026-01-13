package stacksandqueues;

import java.util.*;

/**
 * Problem: Nested List Weight Sum
 *
 * You are given a nested list of integers `nestedList`. Each element is either an integer or a list
 * whose elements may also be integers or other lists.
 * The depth of an integer is the number of lists that it is inside of. For example, the nested list
 * [1,[4,[6]]] has each integer's value multiplied by its depth: 1*1 + 4*2 + 6*3 = 17.
 * Return the sum of each integer in nestedList multiplied by its depth.
 *
 * Example:
 * Input: nestedList = [[1,1],2,[1,1]]
 * Output: 10
 * Explanation: Four 1's at depth 2, one 2 at depth 1: 1*2 + 1*2 + 2*1 + 1*2 + 1*2 = 10
 *
 * LeetCode: https://leetcode.com/problems/nested-list-weight-sum
 *
 * Follow-up Questions:
 * 1. What if we need to weight by reverse depth (deeper elements have less weight)?
 *    Answer: First find max depth, then use (maxDepth - currentDepth + 1) as multiplier.
 *    Related: https://leetcode.com/problems/nested-list-weight-sum-ii/
 *
 * 2. How would you handle cyclic nested structures?
 *    Answer: Use visited set to detect cycles and avoid infinite recursion.
 *
 * 3. What if the nested structure is extremely deep?
 *    Answer: Use iterative approach with explicit stack to avoid stack overflow.
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
     * Calculates weighted sum using recursive depth-first search.
     *
     * Algorithm:
     * 1. For each element in the nested list, check if it's an integer or list
     * 2. If integer, multiply by current depth and add to sum
     * 3. If list, recursively process with incremented depth
     * 4. Return total weighted sum
     *
     * Time Complexity: O(n) where n is total number of elements (integers + lists)
     * Space Complexity: O(d) where d is maximum depth (recursion stack)
     *
     * @param nestedList List of nested integers
     * @return Sum of integers weighted by their depth
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
}