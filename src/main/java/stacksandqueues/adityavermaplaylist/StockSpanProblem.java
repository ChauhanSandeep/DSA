package stacksandqueues.adityavermaplaylist;

import java.util.*;

/**
 * Problem: Online Stock Span
 *
 * For each day's stock price, return how many consecutive days ending today had
 * price less than or equal to today's price. The first strictly greater price
 * to the left stops the span.
 *
 * Leetcode: https://leetcode.com/problems/online-stock-span/ (Medium)
 * Rating:   1709
 * Pattern:  Stack | Monotonic decreasing stack | Nearest greater to left
 *
 * Example:
 *   Input:  prices = [100,80,60,70,60,75,85]
 *   Output: [1,1,1,2,1,4,6]
 *   Why:    price 85 can include 75,60,70,60,80 but stops before 100, so its span is 6.
 *
 * Follow-ups:
 *   1. Implement the streaming StockSpanner API?
 *      Keep the same stack across calls and return each new span immediately.
 *   2. What if equal prices should stop the span?
 *      Change the pop condition from <= to < so equal prices remain blockers.
 *   3. Return the previous greater index too?
 *      Store indexStack.peek() before computing the span.
 *   4. Support rolling windows of only W days?
 *      Drop indices older than W while preserving the decreasing stack invariant.
 *
 * Related: Daily Temperatures (739), Next Greater Element I (496), Largest Rectangle in Histogram (84).
 */

public class StockSpanProblem {

        /**
     * Intuition: today's span reaches left until the nearest previous price that
     * is strictly greater. Any previous price <= currentElement cannot block
     * today or future higher prices, so the stack pops those and leaves the
     * nearest greater index on top.
     *
     * Algorithm:
     *   1. Scan prices from left to right while indexStack stores decreasing prices.
     *   2. Pop indices whose prices are <= currentElement.
     *   3. If the stack is empty, span is currentIndex - (-1); otherwise currentIndex - indexStack.peek().
     *   4. Push currentIndex for future days.
     *
     * Time:  O(n) - each index is pushed once and popped at most once.
     * Space: O(n) - spans and indexStack can each store n entries.
     *
     * @param prices daily stock prices
     * @return span for each day
     */

    public int[] calculateStockSpan(int[] prices) {
        int length = prices.length;
        int[] spans = new int[length];
        Stack<Integer> indexStack = new Stack<>();

        for (int currentIndex = 0; currentIndex < length; currentIndex++) {
            int currentElement = prices[currentIndex];

            // Compare using prices[indexStack.peek()]
            while (!indexStack.isEmpty() && prices[indexStack.peek()] <= currentElement) {
                indexStack.pop();
            }

            // Either the stack is empty or we have a greater element
            // The span is current index - index of nearest greater element
            if (indexStack.isEmpty()) {
                spans[currentIndex] = currentIndex - (-1); // -1 is considered as the index of the nearest greater element
            } else {
                spans[currentIndex] = currentIndex - indexStack.peek();
            }

            indexStack.push(currentIndex); // Push current index to stack for future comparisons
        }

        return spans;
    }

        public static void main(String[] args) {
        StockSpanProblem solver = new StockSpanProblem();
        int[][] inputs = { {100, 80, 60, 70, 60, 75, 85}, {31, 41, 48, 59}, {} };
        int[][] expected = { {1, 1, 1, 2, 1, 4, 6}, {1, 2, 3, 4}, {} };

        for (int i = 0; i < inputs.length; i++) {
            int[] got = solver.calculateStockSpan(inputs[i]);
            System.out.printf("prices=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }
}