package dynamicprogramming.MiscellaneousDP;

import java.util.*;

/**
 * Problem: Broken Calculator
 *
 * A calculator starts at startValue. One operation can either multiply the display
 * by 2 or subtract 1. Return the minimum number of operations needed to reach the
 * target.
 *
 * Leetcode: https://leetcode.com/problems/broken-calculator/
 * Rating:   1909 (zerotrac Elo)
 * Pattern:  Greedy | Reverse simulation | Shortest path alternative
 *
 * Example:
 *   Input:  startValue = 2, target = 3
 *   Output: 2
 *   Why:    the shortest forward path is 2 -> 4 -> 3, using one double and one subtract.
 *
 * Follow-ups:
 *   1. Return the actual operation list?
 *      Record reverse operations, then invert and reverse them at the end.
 *   2. What if multiply by k replaces multiply by 2?
 *      Reverse from target, divide by k when divisible, otherwise increment toward divisibility.
 *   3. What if operations have different costs?
 *      Model values as graph nodes and run Dijkstra over bounded reachable values.
 *
 * Related: 2 Keys Keyboard (650), Integer Replacement (397).
 */
public class BrokenCalculator {

  /**
     * Intuition: the forward direction branches at every value, but the reverse
     * direction is nearly forced. If target is even, the best previous value is
     * target / 2 because that undoes a multiply. If target is odd and still above
     * startValue, it could not have come from multiplying an integer, so we undo a
     * subtract by adding 1. Once target drops below startValue, only forward
     * subtract operations are left, so the remaining cost is the difference.
     *
     * Algorithm:
     *   1. Work backward while target is still larger than startValue.
     *   2. If target is even, divide by 2; otherwise add 1 to undo a subtract operation.
     *   3. Count each reverse move, then add startValue - target for the remaining forward subtracts.
     *
     * Time:  O(log target) - repeated divisions by 2 shrink the target quickly.
     * Space: O(1) - only counters and the current target value are stored.
     *
     * @param startValue initial calculator value
     * @param target desired value
     * @return minimum number of operations
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

    public static void main(String[] args) {
        BrokenCalculator solver = new BrokenCalculator();
        int[][] inputs = {{2, 3}, {5, 8}, {10, 1}};
        int[] expected = {2, 2, 9};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.brokenCalc(inputs[i][0], inputs[i][1]);
            System.out.printf("startValue=%d target=%d -> %d  expected=%d%n",
                inputs[i][0], inputs[i][1], got, expected[i]);
        }
    }
}
