package stacksandqueues;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;


/**
 * Problem: Asteroid Collision
 *
 * Asteroids move in a line at the same speed. Positive values move right,
 * negative values move left, and only a right-moving asteroid followed by a
 * left-moving asteroid can collide. The smaller asteroid explodes; equal sizes
 * destroy both.
 *
 * Leetcode: https://leetcode.com/problems/asteroid-collision/ (Medium)
 * Rating:   acceptance 48.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Stack | Collision simulation | Survivors
 *
 * Example:
 *   Input:  asteroids = [10, 2, -5]
 *   Output: [10]
 *   Why:    2 loses to -5, then -5 loses to 10, so only 10 remains.
 *
 * Follow-ups:
 *   1. Asteroids have different speeds and starting positions?
 *      Use an event simulation ordered by collision time, updating neighbors as asteroids disappear.
 *   2. Need O(1) extra space besides the returned array?
 *      Reuse the input array as a stack with a write pointer.
 *   3. Collisions reduce mass instead of deleting the smaller asteroid?
 *      Store mutable remaining mass and keep resolving the current asteroid until it is gone or safe.
 *   4. Extend to 2D trajectories?
 *      Model each asteroid as a ray and solve pairwise intersection times with a spatial index.
 *
 * Related: Car Fleet (853), Robot Collisions (2751).
 */
public class AsteroidCollision {

    /**
   * Intuition: the stack stores asteroids that survived the processed prefix.
   * Only a negative current asteroid and positive stack top move toward each
   * other, so all other pairs are already safe. Repeatedly resolving the top
   * fight handles chain reactions through smaller right-moving asteroids.
   *
   * Algorithm:
   *   1. Scan asteroids from left to right.
   *   2. While current is negative and stack top is positive, compare sizes.
   *   3. Pop smaller stack asteroids; pop equal and mark current destroyed.
   *   4. Push the current asteroid only if it survives collision handling.
   *   5. Pop the stack into the result array in reverse order.
   *
   * Time:  O(n) - each asteroid is pushed once and popped at most once.
   * Space: O(n) - the survivor stack can hold all asteroids.
   *
   * @param asteroids array of asteroid sizes and directions
   * @return array representing final state after all collisions
   */
public int[] asteroidCollision(int[] asteroids) {
    Deque<Integer> stack = new ArrayDeque<>();

    for (int asteroid : asteroids) {
      boolean isCurrentAlive = true;

      while (isCurrentAlive && !stack.isEmpty() && asteroid < 0 && stack.peek() > 0) {
        int top = stack.peek();

        if (top < -asteroid) {
          // asteroid on stack explodes
          stack.pop();
          continue;
        } else if (top == -asteroid) {
          // both asteroids explode
          stack.pop();
        }

        // current asteroid is destroyed
        isCurrentAlive = false;
      }

      if (isCurrentAlive) {
        // current asteroid survived, push to stack
        stack.push(asteroid);
      }
    }

    int[] result = new int[stack.size()];

    for (int i = result.length - 1; i >= 0; i--) {
      result[i] = stack.pop();
    }

    return result;
  }

  /**
   * Alternative in-place approach using input array as stack to optimize space.
   *
   * Algorithm:
   * 1. Use write pointer to track position in result array
   * 2. Process each asteroid, using array positions [0, write) as stack
   * 3. Handle collisions by comparing with asteroids[write - 1]
   * 4. Mark current destroyed when the stored asteroid is greater or equal
   * 5. Only increment write when asteroid survives
   * 6. Return subarray of first write elements
   *
   * Time Complexity: O(N) where N is the number of asteroids.
   *
   * Space Complexity: O(1) extra space as we modify input array in-place.
   * Output array is required by problem definition and doesn't count toward space complexity.
   *
   * @param asteroids array of asteroid sizes and directions
   * @return array representing final state after all collisions
   */
  public int[] asteroidCollisionInPlace(int[] asteroids) {
    int write = 0;

    for (int asteroid : asteroids) {
      boolean isCurrentAlive = true;

      while (isCurrentAlive && write > 0 && asteroids[write - 1] > 0 && asteroid < 0) {
        if (asteroids[write - 1] < -asteroid) {
          write--;
          continue;
        } else if (asteroids[write - 1] == -asteroid) {
          write--;
        }

        // current asteroid is destroyed
        isCurrentAlive = false;
      }

      if (isCurrentAlive) {
        asteroids[write++] = asteroid;
      }
    }

    int[] result = new int[write];
    System.arraycopy(asteroids, 0, result, 0, write);
    return result;
  }

  public static void main(String[] args) {
    AsteroidCollision solver = new AsteroidCollision();
    int[][] inputs = {
        {},
        {5, 10, 15},
        {5, 10, -5},
        {8, -8},
        {10, 2, -5},
        {-2, -1, 1, 2},
        {-2, 2, -1, -2}
    };
    String[] expected = {
        "[]",
        "[5, 10, 15]",
        "[5, 10]",
        "[]",
        "[10]",
        "[-2, -1, 1, 2]",
        "[-2]"
    };
    for (int i = 0; i < inputs.length; i++) {
      int[] got = solver.asteroidCollision(inputs[i].clone());
      System.out.printf("asteroids=%s -> %s  expected=%s%n", Arrays.toString(inputs[i]), Arrays.toString(got), expected[i]);
    }
  }
}
