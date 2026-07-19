package stacksandqueues.monotonicstack;

import java.util.*;

/**
 * Problem: Find the Most Competitive Subsequence
 *
 * Given nums and k, return the lexicographically smallest subsequence of length
 * k while preserving original order. A smaller earlier value is worth taking
 * only if enough remaining values still exist to fill the subsequence.
 *
 * Leetcode: https://leetcode.com/problems/find-the-most-competitive-subsequence/ (Medium)
 * Rating:   1802
 * Pattern:  Stack | Greedy | Monotonic increasing subsequence
 *
 * Example:
 *   Input:  nums = [3,5,2,6], k = 2
 *   Output: [2,6]
 *   Why:    2 can replace 3 and 5 while still leaving 6 to complete length 2.
 *
 * Follow-ups:
 *   1. Return the largest competitive subsequence?
 *      Reverse the comparison and pop smaller previous values when safe.
 *   2. What if removals have different costs?
 *      Track remaining removal budget instead of only remaining element count.
 *   3. Answer many k values for the same nums?
 *      Precompute next minimum positions with range-min structures, or rerun the O(n) greedy per k.
 *   4. Preserve stable choices among equal values?
 *      Keep the strict > comparison so equal earlier values are not popped.
 *
 * Related: Remove K Digits (402), Create Maximum Number (321), Smallest Subsequence of Distinct Characters (1081).
 */

public class FindTheMostCompetitiveSubsequence {

        /**
     * Intuition: build the answer from left to right, and whenever a smaller
     * nums[i] appears, remove larger previous choices only if the remaining
     * numbers can still fill k slots. The stack therefore keeps the best prefix
     * possible without sacrificing the required length.
     *
     * Algorithm:
     *   1. Scan nums from left to right.
     *   2. While stack.peek() > nums[i] and enough elements remain, pop the stack.
     *   3. Push nums[i] only while the stack has fewer than k values.
     *   4. Pop the stack into result from right to left to preserve order.
     *
     * Time:  O(n) - each value is pushed and popped at most once.
     * Space: O(k) - the stack and result hold k selected values.
     *
     * @param nums input array
     * @param k required subsequence length
     * @return lexicographically smallest subsequence of length k
     */

    public int[] mostCompetitive(int[] nums, int k) {
        Stack<Integer> stack = new Stack<>();
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            // Pop elements that are larger than current and can be safely removed
            while (!stack.isEmpty()
                   && stack.peek() > nums[i]
                   && stack.size() + (n - i) > k) {
                stack.pop();
            }

            // Add current element if we haven't reached k elements yet
            if (stack.size() < k) {
                stack.push(nums[i]);
            }
        }

        // Convert stack to array
        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            result[i] = stack.pop();
        }

        return result;
    }

    /**
     * Deque-based approach for more flexibility in operations.
     * Provides same functionality but with different data structure.
     */
    public int[] mostCompetitiveDeque(int[] nums, int k) {
        Deque<Integer> deque = new ArrayDeque<>();
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            // Remove larger elements from the end if we can afford to
            while (!deque.isEmpty()
                   && deque.peekLast() > nums[i]
                   && deque.size() + (n - i) > k) {
                deque.removeLast();
            }

            if (deque.size() < k) {
                deque.addLast(nums[i]);
            }
        }

        return deque.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Array-based approach without extra data structures.
     * Uses array indices to simulate stack behavior.
     */
    public int[] mostCompetitiveArray(int[] nums, int k) {
        int[] result = new int[k];
        int top = -1; // Stack pointer
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            // Pop elements that are larger and can be replaced
            while (top >= 0
                   && result[top] > nums[i]
                   && (top + 1) + (n - i) > k) {
                top--;
            }

            // Push current element if space available
            if (top + 1 < k) {
                result[++top] = nums[i];
            }
        }

        return result;
    }

    /**
     * Two-pointer approach with explicit remaining count tracking.
     * More intuitive for understanding the algorithm logic.
     */
    public int[] mostCompetitiveTwoPointer(int[] nums, int k) {
        List<Integer> result = new ArrayList<>();
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            int remaining = n - i; // Elements remaining including current

            // Remove larger elements if we have enough elements left
            while (!result.isEmpty()
                   && result.get(result.size() - 1) > nums[i]
                   && result.size() + remaining > k) {
                result.remove(result.size() - 1);
            }

            // Add current element if we need more elements
            if (result.size() < k) {
                result.add(nums[i]);
            }
        }

        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Recursive approach for educational purposes.
     * Not efficient but shows the decision-making process clearly.
     */
    public int[] mostCompetitiveRecursive(int[] nums, int k) {
        List<Integer> result = new ArrayList<>();
        findMostCompetitive(nums, 0, k, result, new ArrayList<>());
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    /** Explores include/exclude choices for the recursive baseline. */
    private void findMostCompetitive(int[] nums, int index, int k, List<Integer> best, List<Integer> current) {
        if (current.size() == k) {
            if (best.isEmpty() || isLexicographicallySmaller(current, best)) {
                best.clear();
                best.addAll(current);
            }
            return;
        }

        if (index >= nums.length || current.size() + (nums.length - index) < k) {
            return;
        }

        // Include current element
        current.add(nums[index]);
        findMostCompetitive(nums, index + 1, k, best, current);
        current.remove(current.size() - 1);

        // Exclude current element
        findMostCompetitive(nums, index + 1, k, best, current);
    }

    /** Checks whether list1 is lexicographically smaller than list2. */
    private boolean isLexicographicallySmaller(List<Integer> list1, List<Integer> list2) {
        for (int i = 0; i < Math.min(list1.size(), list2.size()); i++) {
            if (list1.get(i) < list2.get(i)) return true;
            if (list1.get(i) > list2.get(i)) return false;
        }
        return list1.size() < list2.size();
    }

    /**
     * Priority queue approach for finding k smallest elements with position constraints.
     * Alternative perspective on the problem.
     */
    public int[] mostCompetitivePriorityQueue(int[] nums, int k) {
        // This approach doesn't work directly due to ordering constraints
        // Included for completeness but reverts to stack approach
        return mostCompetitive(nums, k);
    }

    /**
     * Segment tree approach for range minimum queries.
     * Overkill for this problem but demonstrates advanced techniques.
     */
    public int[] mostCompetitiveAdvanced(int[] nums, int k) {
        // For this specific problem, the stack approach is optimal
        // Segment tree would be useful if we had multiple queries
        return mostCompetitive(nums, k);
    }

    public static void main(String[] args) {
        FindTheMostCompetitiveSubsequence solver = new FindTheMostCompetitiveSubsequence();
        int[][] inputs = { {3, 5, 2, 6}, {2, 4, 3, 3, 5, 4, 9, 6}, {1} };
        int[] ks = { 2, 4, 1 };
        int[][] expected = { {2, 6}, {2, 3, 3, 4}, {1} };

        for (int i = 0; i < inputs.length; i++) {
            int[] got = solver.mostCompetitive(inputs[i], ks[i]);
            System.out.printf("nums=%s k=%d -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), ks[i], Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }
}