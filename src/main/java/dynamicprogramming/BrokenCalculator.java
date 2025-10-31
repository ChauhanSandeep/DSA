package dynamicprogramming;

import java.util.*;

/**
 * Problem: Broken Calculator
 *
 * There is a broken calculator that has the integer startValue on its display initially.
 * In one operation, you can:
 * - Multiply the number on display by 2, or
 * - Subtract 1 from the number on display
 *
 * Given two integers startValue and target, return the minimum number of operations
 * needed to display target on the calculator.
 *
 * Example:
 * Input: startValue = 2, target = 3
 * Output: 2
 * Explanation: Use double operation and then decrement {2 -> 4 -> 3}.
 *
 * Constraints:
 * - 1 <= startValue, target <= 10^9
 *
 * LeetCode Problem: https://leetcode.com/problems/broken-calculator
 *
 * Follow-up Questions:
 *
 * 1. What if we have three operations: multiply by 2, subtract 1, and add 1?
 *    Answer: Still use reverse approach but now we can either divide by 2 (if even) or
 *    subtract 1 to reach startValue. The greedy choice remains optimal.
 *
 * 2. How would you modify if multiplication factor is k instead of 2?
 *    Answer: In reverse, divide by k when target is divisible by k, otherwise increment.
 *    The algorithm structure remains the same with k replacing 2.
 *
 * 3. What if you need to return the actual sequence of operations?
 *    Answer: Store each operation in a list during the reverse process, then reverse
 *    the list and swap operations (divide becomes multiply, add becomes subtract).
 *
 * 4. Can you solve this if the operations cost different amounts?
 *    Answer: This becomes a weighted shortest path problem. Use Dijkstra's algorithm
 *    or dynamic programming to find the minimum cost path from startValue to target.
 *
 * 5. How would you handle if both multiply and divide operations are available?
 *    Answer: The problem becomes more complex as we have four operations. Use BFS with
 *    state space exploration to find the shortest path, or apply bidirectional search.
 */
public class BrokenCalculator {

  /**
   * Finds minimum operations using reverse greedy approach.
   *
   * Algorithm:
   * 1. Work backwards from target to startValue instead of forward
   * 2. If target is even, divide by 2 (reverse of multiply)
   * 3. If target is odd, add 1 (reverse of subtract)
   * 4. When target <= startValue, only subtract operations remain
   * 5. Count total operations performed
   *
   * Key insight: Working backwards simplifies the problem because:
   * - From any number, we have two choices going forward (multiply or subtract)
   * - Going backwards, the choice is deterministic based on parity
   * - If even: must divide (only way to reach this from smaller even number efficiently)
   * - If odd: must add 1 first to make it even, then divide
   *
   * Time Complexity: O(log target). Each division by 2 reduces target exponentially,
   * similar to binary representation. Worst case is when target is large power of 2.
   *
   * Space Complexity: O(1) using only a few variables.
   *
   * @param startValue initial value on calculator display
   * @param target desired value to reach
   * @return minimum number of operations needed
   */
  public int brokenCalc(int startValue, int target) {
    int operations = 0;

    // Work backwards from target to startValue
    while (target > startValue) {
      if (target % 2 == 0) {
        // Target is even: divide by 2 (reverse of multiply by 2)
        target /= 2;
      } else {
        // Target is odd: add 1 to make it even (reverse of subtract 1)
        target++;
      }
      operations++;
    }

    // At this point the target is less than or equal to startValue
    // Only option is to subtract the difference
    int difference = startValue - target;
    operations += difference;
    return operations;
  }

  /**
   * Alternative forward approach using BFS (less efficient but shows direct solution).
   *
   * Algorithm:
   * 1. Use BFS to explore all possible states from startValue
   * 2. For each state, try both multiply and subtract operations
   * 3. Track visited states to avoid cycles
   * 4. Return when target is reached
   *
   * Time Complexity: O(target) in worst case as we might explore many states.
   * Much slower than reverse greedy approach.
   *
   * Space Complexity: O(target) for queue and visited set.
   *
   * Note: This approach is included for educational purposes. The reverse greedy
   * approach is significantly more efficient and is the preferred solution.
   *
   * @param startValue initial value on calculator display
   * @param target desired value to reach
   * @return minimum number of operations needed
   */
  public int brokenCalcBFS(int startValue, int target) {
    if (startValue >= target) {
      return startValue - target;
    }

    Queue<int[]> queue = new LinkedList<>();
    Set<Integer> visited = new HashSet<>();

    queue.offer(new int[]{startValue, 0}); // {current value, operations count}
    visited.add(startValue);

    while (!queue.isEmpty()) {
      int[] current = queue.poll();
      int value = current[0];
      int ops = current[1];

      if (value == target) {
        return ops;
      }

      // Try multiply by 2
      int doubled = value * 2;
      if (doubled <= target * 2 && !visited.contains(doubled)) {
        visited.add(doubled);
        queue.offer(new int[]{doubled, ops + 1});
      }

      // Try subtract 1
      if (value > 1) {
        int decremented = value - 1;
        if (!visited.contains(decremented)) {
          visited.add(decremented);
          queue.offer(new int[]{decremented, ops + 1});
        }
      }
    }

    return -1;
  }
}
