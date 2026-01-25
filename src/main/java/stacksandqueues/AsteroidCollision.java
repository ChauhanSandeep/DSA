package stacksandqueues;

import java.util.Stack;


/**
 * Given an array asteroids of integers representing asteroids in a row, where the absolute
 * value represents size and the sign represents direction (positive = right, negative = left).
 * Each asteroid moves at the same speed. Find the state of asteroids after all collisions.
 *
 * When two asteroids meet, the smaller one explodes. If both are the same size, both explode.
 * Two asteroids moving in the same direction will never meet.
 *
 * Example:
 * Input: asteroids = [10,2,-5]
 * Output: [10]
 * Explanation: The 2 and -5 collide resulting in -5. The 10 and -5 collide resulting in 10.
 *
 * LeetCode Problem: https://leetcode.com/problems/asteroid-collision
 *
 * Follow-up Questions:
 *
 * 1. What if asteroids move at different speeds?
 *    Answer: We would need additional information (speed values) and simulate collisions
 *    based on when asteroids actually meet in time. This would require sorting by position
 *    and calculating collision times, making it significantly more complex.
 *
 * 2. How would you extend this to 2D space where asteroids can move in any direction?
 *    Answer: Use velocity vectors for each asteroid and calculate if trajectories intersect.
 *    This becomes a computational geometry problem requiring line intersection algorithms.
 *
 * 3. What if we need to track the order of collisions?
 *    Answer: Store collision events in a list as they occur. Each event would record the
 *    two colliding asteroids and the result. This adds O(K) space where K is the number
 *    of collisions.
 *
 * 4. Can you solve this without using extra space?
 *    Answer: Yes, we can use the input array as a stack by maintaining a write pointer.
 *    Simulate collisions in-place and return the new length. This achieves O(1) extra space.
 *
 * 5. How would you handle asteroids that can survive partial collisions?
 *    Answer: Track remaining mass for each asteroid. On collision, reduce the smaller
 *    asteroid's mass by the collision impact. Continue until mass reaches zero or no more
 *    collisions occur.
 *    Related problem: https://leetcode.com/problems/car-fleet/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class AsteroidCollision {

  /**
   * Simulates asteroid collisions using a stack to track surviving asteroids.
   *
   * Algorithm:
   * 1. Iterate through each asteroid in the array
   * 2. For right-moving asteroids (positive), push to stack
   * 3. For left-moving asteroids (negative), check for collisions:
   *    - While stack is non-empty and top is right-moving (positive):
   *      - If left-moving asteroid is larger, pop the stack (right one explodes)
   *      - If equal size, pop the stack and mark left as destroyed (both explode)
   *      - If left-moving is smaller, mark it as destroyed (it explodes)
   * 4. If left-moving asteroid survives all collisions, push to stack
   * 5. Convert stack to array for the result
   *
   * Time Complexity: O(N) where N is the number of asteroids
   * Space Complexity: O(N) for the stack storing surviving asteroids
   *
   * @param asteroids array of asteroid sizes and directions
   * @return array representing final state after all collisions
   */
  public int[] asteroidCollision(int[] asteroids) {
    Stack<Integer> stack = new Stack<>();

    for (int asteroid : asteroids) {
      while (!stack.isEmpty() && asteroid < 0 && stack.peek() > 0) {
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
        continue;
      }

      // current asteroid survived, push to stack
      stack.push(asteroid);
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
   * 4. Only increment write when asteroid survives
   * 5. Return subarray of first write elements
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
      while (write > 0 && asteroids[write - 1] > 0 && asteroid < 0) {
        if (asteroids[write - 1] < -asteroid) {
          write--;
          continue;
        } else if (asteroids[write - 1] == -asteroid) {
          write--;
        }

        // current asteroid is destroyed
        continue;
      }

      asteroids[write++] = asteroid;
    }

    int[] result = new int[write];
    System.arraycopy(asteroids, 0, result, 0, write);
    return result;
  }
}