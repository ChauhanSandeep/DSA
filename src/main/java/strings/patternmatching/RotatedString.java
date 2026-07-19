package strings.patternmatching;

/**
 * Problem: Check if String Is Rotated by Two Places
 *
 * Given two strings, decide whether the second can be obtained by rotating the
 * first exactly two places in either direction. Both strings must have equal
 * length, and short strings are only valid when they are already equal.
 *
 * Source: https://practice.geeksforgeeks.org/problems/check-if-string-is-rotated-by-two-places-1587115620/1
 * Rating: contest Elo 1167
 * Pattern: Strings | Rotation | Substring construction
 *
 * Example:
 *   Input:  str1 = "amazon", str2 = "azonam"
 *   Output: true
 *   Why:    moving the first two characters of "amazon" to the end gives "azonam".
 *
 * Follow-ups:
 *   1. How would you check rotation by exactly k places?
 *      Compare against the left-k and right-k rotations using k modulo n.
 *   2. How can you avoid allocating rotated strings?
 *      Compare characters with modular indices for both directions.
 *   3. How do you check any rotation, not just two places?
 *      Test whether target is contained in source + source.
 *
 * Related: Rotate String (796).
 */
public class RotatedString {

  public static void main(String[] args) {
    String[] sources = {"amazon", "geeksforgeeks", "a"};
    String[] targets = {"azonam", "geeksgeeksfor", "a"};
    boolean[] expected = {true, false, true};

    for (int i = 0; i < sources.length; i++) {
      boolean got = isRotationByTwo(sources[i], targets[i]);
      System.out.printf("str1=%s str2=%s -> %b  expected=%b%n",
          sources[i], targets[i], got, expected[i]);
    }
  }


    /**
   * Intuition: exactly two-place rotation has only two possible results: move the
   * first two characters to the end, or move the last two characters to the front.
   * Build those candidates and compare them with str2.
   *
   * Algorithm:
   *   1. Reject null or unequal-length inputs.
   *   2. For length below 2, only identical strings can match.
   *   3. Build the left-two and right-two rotations of str1.
   *   4. Return whether str2 equals either rotation.
   *
   * Time:  O(n) - substring creation and equality checks scan the strings.
   * Space: O(n) - the two rotated strings are allocated.
   *
   * @param str1 Original string.
   * @param str2 Candidate rotated string.
   * @return true when str2 is a two-place rotation of str1.
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
