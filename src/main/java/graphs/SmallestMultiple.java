package graphs;

import java.util.*;


/**
 * Problem: Smallest Multiple With Digits 0 and 1
 *
 * Given a positive integer num, find the smallest positive multiple of num whose
 * decimal representation contains only digits 0 and 1. Return the number as a
 * string because it can be much larger than Java integer ranges.
 *
 * Source: InterviewBit: Smallest Multiple With 0 and 1
 * Pattern:  Graph | BFS over remainders | Parent reconstruction
 *
 * Example:
 *   Input:  num = 55
 *   Output: "110"
 *   Why:    110 is divisible by 55, contains only digits 0 and 1, and the BFS
 *           explores shorter strings before longer ones.
 *
 * Follow-ups:
 *   1. Use a custom digit set such as {1, 2}?
 *      Change the outgoing transitions to append those digits in sorted order.
 *   2. Return only the length of the number?
 *      BFS over remainders can stop at remainder 0 without reconstructing parents.
 *   3. num is very large?
 *      The remainder graph has num states, so memory becomes the main bottleneck.
 */
public class SmallestMultiple {

  public static void main(String[] args) {
    int[] inputs = {1, 3, 7};
    String[] expected = {"1", "111", "1001"};
    for (int i = 0; i < inputs.length; i++) {
      String output = findSmallestMultiple(inputs[i]);
      System.out.printf("num=%d -> %s  expected=%s%n", inputs[i], output, expected[i]);
    }
  }


  /**
   * Uses Breadth-First Search to find the smallest number made of only 0s and 1s that is divisible by `num`.
   * Each remainder is treated as a node in the BFS graph, and transitions are made by appending '0' or '1'.
   *
   * Steps:
   * 1. Start with string "1" and track its remainder modulo `num`.
   * 2. Use BFS to explore appending '0' and '1' to build next valid states.
   * 3. For each unique remainder, store the digit used and its parent remainder to reconstruct the number.
   * 4. Stop when remainder becomes 0.
   *
   * Algorithm: Breadth-First Search on remainder graph.
   * Time Complexity: O(N), where N = num, since each remainder from 0 to num-1 is visited at most once.
   * Space Complexity: O(N) for queues and tracking maps.
   *
   * @param num The integer whose smallest binary-digit multiple is to be found.
   * @return A string representing the smallest such multiple.
   */
  public static String findSmallestMultiple(int num) {
      if (num == 1) {
          return "1";
      }

    Queue<Integer> queue = new ArrayDeque<>();
    char[] lastDigitUsed = new char[num];         // Stores '0' or '1' used to reach each remainder.
    int[] parentRemainder = new int[num];         // Stores the previous remainder for backtracking.

    Arrays.fill(lastDigitUsed, 'x');              // 'x' indicates an unvisited remainder.
    Arrays.fill(parentRemainder, -1);

    // Begin BFS with initial remainder = 1 (from "1")
    queue.offer(1);
    lastDigitUsed[1] = '1';

    while (!queue.isEmpty()) {
      int currentRemainder = queue.poll();

      // Found the target: remainder is zero => number formed is divisible by num
        if (currentRemainder == 0) {
            break;
        }

      // Try appending '0'
      int remAfterZero = (currentRemainder * 10) % num;
      if (lastDigitUsed[remAfterZero] == 'x') {
        lastDigitUsed[remAfterZero] = '0';
        parentRemainder[remAfterZero] = currentRemainder;
        queue.offer(remAfterZero);
      }

      // Try appending '1'
      int remAfterOne = (currentRemainder * 10 + 1) % num;
      if (lastDigitUsed[remAfterOne] == 'x') {
        lastDigitUsed[remAfterOne] = '1';
        parentRemainder[remAfterOne] = currentRemainder;
        queue.offer(remAfterOne);
      }
    }

    // Backtrack using parent map to reconstruct the actual number from digits used
    StringBuilder result = new StringBuilder();
    int current = 0; // remainder == 0 means we found our answer
    while (current != -1) {
      result.insert(0, lastDigitUsed[current]);
      current = parentRemainder[current];
    }

    return result.toString();
  }
}
