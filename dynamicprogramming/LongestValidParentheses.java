package dynamicprogramming;

import java.util.Deque;
import java.util.ArrayDeque;


/**
 * Problem: Find the length of the longest valid (well-formed) parentheses substring.
 *
 * Approaches:
 * 1. **Stack-based O(N) Approach**
 *    - Push indices of unmatched '(' onto the stack.
 *    - If a matching ')' is found, pop '(' and compute the valid substring length.
 *
 * 2. **O(N) Optimized Two-Pass Approach (No Stack)**
 *    - Use two counters: left & right parentheses count.
 *    - Scan left-to-right and right-to-left to track balanced sequences.
 *
 * Time Complexity: **O(N)**
 * Space Complexity:
 * - **Stack Approach:** O(N) (worst case)
 * - **Two-Pass Approach:** O(1) (constant space)
 */
public class LongestValidParentheses {
  public static void main(String[] args) {
    LongestValidParentheses lvp = new LongestValidParentheses();
    System.out.println(lvp.longestValidParenthesesStack("(()"));        // Output: 2
    System.out.println(lvp.longestValidParenthesesStack("(())"));       // Output: 4
    System.out.println(lvp.longestValidParenthesesStack("))()()(()()()"));// Output: 6

    System.out.println(lvp.longestValidParenthesesOptimized("(()"));        // Output: 2
    System.out.println(lvp.longestValidParenthesesOptimized("(())"));       // Output: 4
    System.out.println(lvp.longestValidParenthesesOptimized("))()()(()()()"));// Output: 6
  }

  /**
   * Approach 1: O(N) using Stack
   * - Use a stack to store indices of unmatched '('
   * - When a matching ')' is found, pop '(' and compute max valid length
   */
  public int longestValidParenthesesStack(String str) {
    int maxLength = 0;
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(-1); // Base case for valid length computation

    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (c == '(') {
        stack.push(i);
      } else { // c == ')'
        stack.pop();
        if (stack.isEmpty()) {
          stack.push(i); // Store last invalid index
        } else {
          maxLength = Math.max(maxLength, i - stack.peek());
        }
      }
    }
    return maxLength;
  }

  /**
   * Approach 2: O(N) Optimized Two-Pass Traversal (No Stack)
   * - Scan left to right, count '(' and ')' occurrences.
   * - If '(' == ')', update max valid length.
   * - If ')' > '(', reset count.
   * - Repeat in reverse direction to handle edge cases.
   */
  public int longestValidParenthesesOptimized(String str) {
    int maxLength = 0, left = 0, right = 0;

    // Left to Right Scan
    for (char c : str.toCharArray()) {
        if (c == '(') {
            left++;
        } else {
            right++;
        }

      if (left == right) {
        maxLength = Math.max(maxLength, 2 * right);
      } else if (right > left) {
        left = right = 0; // Reset when unbalanced
      }
    }

    left = right = 0;

    // Right to Left Scan
    // scanning in reverse to catch cases like "(()"
    for (int i = str.length() - 1; i >= 0; i--) {
      char c = str.charAt(i);
        if (c == ')') {
            right++;
        } else {
            left++;
        }

      if (left == right) {
        maxLength = Math.max(maxLength, 2 * left);
      } else if (left > right) {
        left = right = 0; // Reset when unbalanced
      }
    }

    return maxLength;
  }
}
