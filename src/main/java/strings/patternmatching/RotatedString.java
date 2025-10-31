package strings.patternmatching;

/**
 * ✅ GFG Problem: Check if String is Rotated by Two Places
 * 🔗 https://practice.geeksforgeeks.org/problems/check-if-string-is-rotated-by-two-places-1587115620/1
 *
 * 🔹 Problem Statement:
 * Given two strings `str1` and `str2`, determine whether `str2` can be obtained by rotating `str1`
 * exactly two places either clockwise or anticlockwise.
 *
 * 🔸 Example:
 * Input: str1 = "amazon", str2 = "azonam"
 * Output: true
 * Explanation: Clockwise rotation of "amazon" by 2 → "azonam"
 *
 * 🔍 Follow-up Questions:
 * - Can this be done without creating new string objects? (Yes, using character comparison with modular indexing)
 * Answer: Yes, by comparing characters at calculated indices.
 * - How would you extend this to check for **k-place** rotations?
 *  Answer: Use modular arithmetic to check indices based on k.
 * - Can this logic be applied to circular buffer pattern matching?
 *  Answer: Yes, it can be adapted for circular buffer checks.
 * - What if the rotation is not fixed
 *  Answer: https://leetcode.com/problems/rotate-string/ solved in canBeRotated method.
 */
public class RotatedString {

  public static void main(String[] args) {
    System.out.println(isRotationByTwo("amazon", "azonam"));          // true
    System.out.println(isRotationByTwo("geeksforgeeks", "geeksgeeksfor")); // false
  }

  /**
   * Checks if `str2` is a rotation of `str1` by exactly two places.
   *
   * 🔹 Steps:
   * - Return false immediately if inputs are null or of unequal length.
   * - If length < 2, only identical strings can be valid.
   * - Generate:
   *    - Clockwise rotated string → rotate left by 2
   *    - Anticlockwise rotated string → rotate right by 2
   * - Check if either matches str2.
   *
   * ⏱ Time Complexity: O(N), due to substring and equals comparisons.
   * 🧠 Space Complexity: O(N), due to new strings created via substring concatenation.
   */
  private static boolean isRotationByTwo(String str1, String str2) {
    if (str1 == null || str2 == null || str1.length() != str2.length()) {
      return false;
    }

    int len = str1.length();

    // For strings shorter than 2, only exact match is allowed
    if (len < 2) {
      return str1.equals(str2);
    }

    // Clockwise rotation: rotate left by 2
    String clockwise = str1.substring(2) + str1.substring(0, 2);

    // Anticlockwise rotation: rotate right by 2
    String anticlockwise = str1.substring(len - 2) + str1.substring(0, len - 2);

    return str2.equals(clockwise) || str2.equals(anticlockwise);
  }

  /**
   * Determines if string `source` can be rotated to become string `target`.

   * 🔹 Steps:
   * - If lengths mismatch, rotation is impossible.
   * - Concatenate string `source` with itself → `source + source`
   * - If `target` is a substring of this concatenated string, it'source a valid rotation.
   *
   * ⏱ Time Complexity: O(N), where N = length of string `source`
   * 🧠 Space Complexity: O(N), for concatenated string.
   */
  public static boolean canBeRotated(String source, String target) {
    if (source == null || target == null || source.length() != target.length()) {
      return false;
    }

    String concatenatedSource = source + source;
    return concatenatedSource.contains(target);
  }
}
