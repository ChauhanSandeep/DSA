package stacksandqueues.adityavermaplaylist;

import java.util.*;

/**
 * Problem Statement:
 * Given a list of daily stock prices, calculate the "stock span" for each day.
 *
 * The span of the stock's price today is defined as the maximum number of consecutive days (starting from today and going backward)
 * for which the stock price was less than or equal to today's price.
 *
 * Example:
 * Input:  [100, 80, 60, 70, 60, 75, 85]
 * Output: [1, 1, 1, 2, 1, 4, 6]
 *
 * Link: https://leetcode.com/problems/online-stock-span/
 *
 */
public class StockSpanProblem {

    /**
     * Calculates the stock span for each day using the Nearest Greater to Left (NGL) concept.
     *
     * Intuition:
     * - For each day, we need to find how far back we can go until we find a greater price.
     * - This is essentially finding the **index of the nearest greater element to the left**.
     *
     * How Nearest Greater to Left (NGL) is used:
     * - If we know the index of the nearest previous greater element,
     *   then the span = (current index - index of nearest greater to left).
     * - If no greater element exists on the left, the span is (current index + 1).
     *
     * Approach:
     * - Use a stack to maintain a decreasing sequence (prices with their indices).
     * - For each price:
     *    - Pop all prices smaller than or equal to current price.
     *    - If the stack is empty, no greater element exists → span = i + 1.
     *    - Else, span = i - top of stack's index.
     *    - Push the current price and its index onto the stack.
     *
     * Time Complexity: O(n)
     * - Each price is pushed and popped from the stack at most once.
     *
     * Space Complexity: O(n)
     * - Stack is used to store prices and their indices.
     *
     * @param prices Array of daily stock prices
     * @return Array containing the stock span for each day
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

        int[] inputPrices = {100, 80, 60, 70, 60, 75, 85};
        int[] outputSpans = solver.calculateStockSpan(inputPrices);

        System.out.println("Stock spans: " + Arrays.toString(outputSpans));
    }
}