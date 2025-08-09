package Backtracking;

import java.util.ArrayList;
import java.util.List;


/**
 * Problem: Generate all possible permutations of an array of distinct integers.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/permutations/
 *
 * 🔍 Example:
 * Input: [1, 2, 3]
 * Output: [
 *   [1, 2, 3],
 *   [1, 3, 2],
 *   [2, 1, 3],
 *   [2, 3, 1],
 *   [3, 1, 2],
 *   [3, 2, 1]
 * ]
 *
 * 🧠 Intuition:
 * - Fix each number at the current index and permute the remaining numbers recursively.
 * - Use backtracking to build permutations step-by-step and undo the choice afterward.
 *
 * ✅ Constraints:
 * - Input contains distinct integers.
 *
 * 📌 Follow-up Questions (FAANG-style):
 * 1. What if input has duplicates? Can you return only unique permutations?
 *    - Use a frequency map or boolean + sort + skip duplicates.
 *    - Related: https://leetcode.com/problems/permutations-ii/
 *
 * 2. How would you generate permutations lazily (on-demand)?
 *    - Implement an iterator or generator to yield permutations one-by-one.
 *
 * 3. Can you solve this iteratively?
 *    - Yes, using a queue or dynamic programming-like approach to build permutations level-by-level.
 */
public class Permutation {

  public static void main(String[] args) {
    int[] input = {1, 2, 3};
    List<List<Integer>> allPermutations = generateAllPermutations(input);
    System.out.println(allPermutations);
  }

  /**
   * Generates all possible permutations of a given array of distinct integers using backtracking.
   *
   * Steps:
   * 1. Use a boolean array to track which elements are used in the current permutation.
   * 2. Recursively build the permutation list by picking unused elements.
   * 3. Backtrack after each recursive call to explore other possibilities.
   *
   * Time Complexity: O(N!) → Total number of permutations. Because each permutation takes O(N) time to construct.
   * Space Complexity: O(N) → For recursion stack and boolean tracking.
   *
   * @param nums array of distinct integers
   * @return list of all possible permutations
   */
  public static List<List<Integer>> generateAllPermutations(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    boolean[] isUsed = new boolean[nums.length];
    backtrackPermutations(nums, new ArrayList<>(), result, isUsed);
    return result;
  }

  /**
   * Recursive helper function to build permutations using backtracking.
   *
   * @param nums     original input array
   * @param current  current partial permutation
   * @param result   list of all complete permutations
   * @param isUsed   tracks whether a number is already used in current permutation
   */
  private static void backtrackPermutations(int[] nums, List<Integer> current, List<List<Integer>> result,
      boolean[] isUsed) {
    // Base condition: permutation is complete
    if (current.size() == nums.length) {
      result.add(new ArrayList<>(current));
      return;
    }

    // Explore each unused number
    for (int i = 0; i < nums.length; i++) {
        if (isUsed[i]) {
            continue;
        }

      // Choose
      current.add(nums[i]);
      isUsed[i] = true;

      // Explore
      backtrackPermutations(nums, current, result, isUsed);

      // Un-choose (backtrack)
      current.remove(current.size() - 1);
      isUsed[i] = false;
    }
  }
}
