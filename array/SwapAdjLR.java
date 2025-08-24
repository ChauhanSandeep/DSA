package array;

/**
 * ✅ Problem Statement:
 * Given two strings `start` and `end`, each containing only the characters 'L', 'R', and 'X',
 * you can transform `start` into `end` by performing any number of swaps of adjacent "XL" → "LX" or "RX" → "XR".
 * Basically R can move to right of X and L can move to left of X.
 *
 * Return true if it is possible to transform `start` to `end`, otherwise return false.
 *
 * 🔗 LeetCode: https://leetcode.com/problems/swap-adjacent-in-lr-string/
 *
 * 🧠 Example:
 * Input: start = "RXXLRXRXL", end = "XRLXXRRLX"
 * Output: true
 *
 * ✔️ Constraints:
 * - 'L' can only move left (i.e., LX → XL)
 * - 'R' can only move right (i.e., RX → XR)
 *
 * 🧪 Follow-ups:
 * - How would you verify whether the relative order of L and R is preserved?
 * Answer : You can use two pointers to traverse both strings and ensure that 'L' and 'R' are in the correct order.
 * - Could you simulate swaps instead of just comparing indices?
 * Answer : Yes, you can simulate the swaps by iterating through the strings and swapping adjacent characters as needed.
 */
public class SwapAdjLR {

  public static void main(String[] args) {
    System.out.println(new SwapAdjLR().canTransform("LXR", "LRX"));              // false
    System.out.println(new SwapAdjLR().canTransform("RXXLRXRXL", "XRLXXRRLX"));  // true
  }

  /**
   * Properties of string which can be transformed:
   * 1. The number of 'L's and 'R's in `source` must match the number of 'L's and 'R's in `target`.
   * 3. The relative order of 'L' and 'R' must be same after ignoring 'X'.
   * 4. 'R' can only move to the right of 'X' and 'L' can only move to the left of 'X'.
   * 5. If 'R' appears before 'L' in `source`, it must also appear before 'L' in `target` after ignoring 'X'.
   * 6. If 'L' appears before 'R' in `source`, it must also appear before 'R' in `target` after ignoring 'X'.
   *
   *
   * Logic Summary:
   * 1. Use two pointers to skip over 'X' in both `source` and `target`.
   * 2. When comparing non-'X' characters:
   *    - They must be the same.
   *    - 'R' can only move to the right (i.e., its index must increase).
   *    - 'L' can only move to the left (i.e., its index must decrease).
   * 3. If all characters satisfy the above rules, return true.
   *
   * ⏱ Time Complexity: O(N)
   * 🧠 Space Complexity: O(1)
   *
   * @param source Initial string
   * @param target   Target string
   * @return True if transformation is possible
   */
  public boolean canTransform(String source, String target) {
    if (source.length() != target.length()) {
      return false;
    }

    int sourceIndex = 0, targetIndex = 0;
    int length = source.length();

    while (sourceIndex < length || targetIndex < length) {
      // Skip 'X' characters in both strings
      while (sourceIndex < length && source.charAt(sourceIndex) == 'X') {
        sourceIndex++;
      }
      while (targetIndex < length && target.charAt(targetIndex) == 'X') {
        targetIndex++;
      }

      // If both reached the target, strings are valid
      if (sourceIndex == length && targetIndex == length) {
        return true;
      }

      // One string reached the target, the other didn't → invalid
      if (sourceIndex == length || targetIndex == length) {
        return false;
      }

      char sourceChar = source.charAt(sourceIndex);
      char targetChar = target.charAt(targetIndex);

      // Characters must match
      if (sourceChar != targetChar) {
        return false;
      }

      // At this point both pointers are at non-'X' and same characters
      // but if sourceChar is 'R' and sourceIndex is greater than targetIndex, then
      // it means 'R' is trying to move left which is not allowed
      if (sourceChar == 'R' && sourceIndex > targetIndex) {
        return false;
      }

      // At this point both pointers are at non-'X' and same characters
      // but if sourceChar is 'L' and sourceIndex is less than targetIndex, then
      // it means 'L' is trying to move right which is not allowed
      if (sourceChar == 'L' && sourceIndex < targetIndex) {
        return false;
      }

      sourceIndex++;
      targetIndex++;
    }

    return true;
  }
}