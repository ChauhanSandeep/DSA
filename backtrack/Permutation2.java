package backtrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Problem: Generate all unique permutations of an array of integers that may contain duplicates.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/permutations-ii/
 *
 * 🔍 Example:
 * Input: [1, 1, 2]
 * Output: [[1, 1, 2], [1, 2, 1], [2, 1, 1]]
 *
 * 🧠 Intuition:
 * - When elements are repeated, swapping or selecting the same number again can lead to duplicate permutations.
 * - Sorting helps to group duplicates and enables skipping repeated elements during backtracking.
 *
 * ✅ Constraints:
 * - The array can contain duplicates.
 *
 * 📌 Follow-up Questions (FAANG-style):
 * 1. How would you extend this to generate unique permutations for strings with duplicate characters?
 *    - Convert the string to a char array, sort it, and apply the same backtracking logic.
 *
 * 2. Can you solve this without extra space (e.g., without `used[]`)?
 *    - It’s difficult without `used[]`, as index tracking is needed for deduplication.
 *
 * 3. Can you deduplicate on-the-fly while building permutations?
 *    - Yes. Use a set to track duplicates per recursion level (less efficient than `used[]` with sorting).
 */
public class Permutation2 {

  public static void main(String[] args) {
    int[] input = {3, 3, 0, 3};
    List<List<Integer>> uniquePermutations = generateUniquePermutations(input);
    System.out.println(uniquePermutations);
  }

  /**
   * Generates all unique permutations of a given array with possible duplicates.
   *
   * Steps:
   * 1. Sort the array to group duplicates.
   * 2. Use backtracking with a boolean `used[]` array to track which elements are already included.
   * 3. Skip duplicate elements unless the previous duplicate was used in the current path.
   *
   * Time Complexity: O(N * N!) → N! permutations, O(N) to copy each one.
   * Space Complexity: O(N) → recursion + used array.
   *
   * @param nums array of integers (may include duplicates)
   * @return list of all unique permutations
   */
  public static List<List<Integer>> generateUniquePermutations(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    Arrays.sort(nums); // Sorting is critical to detect and skip duplicates
    boolean[] used = new boolean[nums.length];
    backtrackPermutations(nums, new ArrayList<>(), used, result);
    return result;
  }

  /**
   * Recursive helper function to generate unique permutations via backtracking.
   *
   * @param nums         sorted input array
   * @param current      current permutation being built
   * @param used         boolean array to track used elements
   * @param result       list of all unique permutations
   */
  private static void backtrackPermutations(int[] nums, List<Integer> current, boolean[] used,
      List<List<Integer>> result) {
    if (current.size() == nums.length) {
      result.add(new ArrayList<>(current));
      return;
    }

    for (int i = 0; i < nums.length; i++) {
      // Skip used numbers
      if (used[i]) {
        continue;
      }

      // Skip duplicates: ensure the same number isn't used twice in the same recursion depth
      // Note: The condition !used[i - 1] ensures that duplicate elements are skipped unless the previous duplicate
      // element has already been used in the current recursion path. Otherwise, it can lead to same path
      if (i > 0 && (nums[i] == nums[i - 1] && !used[i - 1])) {
        continue;
      }

      // Choose
      used[i] = true;
      current.add(nums[i]);

      // Explore
      backtrackPermutations(nums, current, used, result);

      // Un-choose (backtrack)
      used[i] = false;
      current.remove(current.size() - 1);
    }
  }

  /**
   * Generates all unique permutations using a frequency counter (HashMap).
   *
   * Key idea: Handle duplicates by tracking frequency (count) of each number.
   * At each step, only pick numbers with count > 0, then backtrack.
   * This avoids generating duplicate permutations without needing extra sorting or visited[].
   *
   * Steps:
   * 1. Count occurrences of each element using HashMap.
   * 2. Start backtracking using a combination list and recursively build permutations.
   * 3. At each step, pick only those elements whose count > 0.
   *
   * Time Complexity: O(N * N!) → N! permutations, O(N) to copy each one.
   * Space Complexity: O(N) → recursion + current combination.
   *
   * @param nums input array with possible duplicates
   * @return list of all unique permutations
   */
  public List<List<Integer>> permuteUniqueUsingFreqMap(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();

    // Step 1: Build frequency map of input elements
    // freqMap ensures that the duplicates are not counted multiple times because
    // we will only use each number as many times as it appears in the input.
    Map<Integer, Integer> freqMap = new HashMap<>();
    for (int num : nums) {
      freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
    }

    // Step 2: Begin recursive backtracking
    backtrack(new LinkedList<>(), nums.length, freqMap, result);
    return result;
  }

  /**
   * Recursive backtracking helper that builds permutations using frequency map.
   *
   * @param current     current combination being built
   * @param totalLength total number of elements to include
   * @param freqMap     frequency of remaining usable numbers
   * @param result      final list of unique permutations
   */
  private void backtrack(LinkedList<Integer> current, int totalLength, Map<Integer, Integer> freqMap,
      List<List<Integer>> result) {
    // Base case: permutation is complete
    if (current.size() == totalLength) {
      result.add(new ArrayList<>(current));
      return;
    }

    for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
      int num = entry.getKey();
      int count = entry.getValue();

      if (count == 0) {
        continue;
      }

      // Choose
      current.addLast(num);
      freqMap.put(num, count - 1);

      // Explore
      backtrack(current, totalLength, freqMap, result);

      // Backtrack
      current.removeLast();
      freqMap.put(num, count);
    }
  }
}
