package stacksandqueues;

import java.util.Stack;

/**
 * Problem: Remove K Digits
 *
 * Given a non-negative integer as a string, remove exactly k digits so the
 * remaining number is as small as possible. The output must not contain leading
 * zeros unless the number itself is zero.
 *
 * Leetcode: https://leetcode.com/problems/remove-k-digits/ (Medium)
 * Rating:   acceptance 37.3% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Monotonic stack | Greedy digit removal | Leading zero trim
 *
 * Example:
 *   Input:  num = "1432219", k = 3
 *   Output: "1219"
 *   Why:    removing 4, 3, and one 2 lets the smaller digits form 1219, the smallest remaining order.
 *
 * Follow-ups:
 *   1. Remove k digits to make the largest possible number?
 *      Reverse the stack comparison and pop smaller previous digits before larger current digits.
 *   2. Need lexicographically smallest string with non-digit characters too?
 *      Use the same monotonic deletion idea with the target ordering over characters.
 *   3. Need to process a million digits with low overhead?
 *      Use a char array as the stack and build the answer by slicing it.
 *   4. Need exactly m digits kept instead of k removed?
 *      Set k = n - m and run the same greedy removal.
 *
 * Related: Create Maximum Number (321), Monotone Increasing Digits (738).
 */
public class RemoveKDigits {
        /**
     * Intuition: smaller earlier digits make the whole number smaller. When a
     * new digit is smaller than the stack top, removing that larger previous
     * digit improves the prefix immediately. Leftover removals are cheapest at
     * the end of the number.
     *
     * Algorithm:
     *   1. Handle k = 0 and removing every digit.
     *   2. Pop larger previous digits while k remains.
     *   3. Push each current digit.
     *   4. Remove leftover digits from the tail.
     *   5. Build the result and trim leading zeros.
     *
     * Time:  O(n) - each digit is pushed and popped at most once.
     * Space: O(n) - stack and result builder store digits.
     *
     * @param num non-negative integer represented as a string
     * @param k number of digits to remove
     * @return smallest possible number after removing k digits
     */
public String removeKdigits(String num, int k) {
        // Edge cases
        if (k == 0) return num;
        if (num.length() == k) return "0";

        Stack<Character> stack = new Stack<>();

        for (char digit : num.toCharArray()) {
            // While the last digit in the stack is larger than the current digit and we can still remove digits
            while (k > 0 && !stack.isEmpty() && stack.peek() > digit) {
                stack.pop();
                k--;
            }
            stack.push(digit);
        }

        // Remove remaining k digits from the end if needed
        while (k > 0 && !stack.isEmpty()) {
            stack.pop();
            k--;
        }

        // Build the result string from the stack
        StringBuilder result = new StringBuilder();
        while (!stack.isEmpty()) {
            result.insert(0, stack.pop());
        }

        // Remove leading zeros
        int i = 0;
        while (i < result.length() && result.charAt(i) == '0') {
            i++;
        }

        // If all digits were zeros, return "0"
        if (i == result.length()) {
            return "0";
        }

        return result.substring(i);
    }

    /**
     * Optimized solution using a char array instead of a stack.
     * This approach is more memory efficient.
     */
    public String removeKdigitsOptimized(String num, int k) {
        int n = num.length();
        if (k == n) return "0";

        char[] digits = num.toCharArray();
        char[] stack = new char[n];
        int top = -1; // Points to the top of the stack

        for (char digit : digits) {
            while (k > 0 && top >= 0 && stack[top] > digit) {
                top--; // Remove the top element
                k--;
            }
            stack[++top] = digit; // Push to stack
        }

        // If there are still digits to remove, remove from the end
        int end = top - k;

        // Find the first non-zero digit
        int start = 0;
        while (start <= end && stack[start] == '0') {
            start++;
        }

        // If all digits were zeros, return "0"
        if (start > end) {
            return "0";
        }

        return new String(stack, start, end - start + 1);
    }

    /**
     * Recursive solution (less efficient but demonstrates an alternative approach).
     * This approach is not recommended for large inputs due to stack overflow risk.
     */
    public String removeKdigitsRecursive(String num, int k) {
        // Base cases
        if (k == 0) return num;
        if (num.length() == k) return "0";

        // Find the first digit that's greater than the next digit
        int i = 0;
        while (i < num.length() - 1 && num.charAt(i) <= num.charAt(i + 1)) {
            i++;
        }

        // Remove the digit at position i
        String newNum = num.substring(0, i) + num.substring(i + 1);

        // Remove leading zeros
        int j = 0;
        while (j < newNum.length() && newNum.charAt(j) == '0') {
            j++;
        }
        newNum = j == newNum.length() ? "0" : newNum.substring(j);

        // Recurse with k-1
        return removeKdigitsRecursive(newNum, k - 1);
    }

    public static void main(String[] args) {
        RemoveKDigits solver = new RemoveKDigits();
        String[] nums = {"10", "1432219", "10200", "9", "112"};
        int[] kValues = {2, 3, 1, 1, 1};
        String[] expected = {"0", "1219", "200", "0", "11"};
        for (int i = 0; i < nums.length; i++) {
            String got = solver.removeKdigits(nums[i], kValues[i]);
            System.out.printf("num=%s k=%d -> %s  expected=%s%n", nums[i], kValues[i], got, expected[i]);
        }
    }
}
