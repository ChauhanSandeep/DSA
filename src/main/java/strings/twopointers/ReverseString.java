package strings.twopointers;

/**
 * Problem: Reverse String
 *
 * Reverse a character array in place. The input is mutable, so the method should
 * swap characters inside the same array and use only constant extra memory.
 *
 * Leetcode: https://leetcode.com/problems/reverse-string/ (Easy)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Strings | Two pointers | In-place swap
 *
 * Example:
 *   Input:  s = ['h','e','l','l','o']
 *   Output: ['o','l','l','e','h']
 *   Why:    each character is swapped with its mirror position from the other end.
 *
 * Follow-ups:
 *   1. How would you reverse only vowels or letters?
 *      Move pointers until both sides point to eligible characters, then swap.
 *   2. Can this be implemented recursively?
 *      Recurse on left + 1 and right - 1 after swapping the ends.
 *   3. What changes for Unicode code points?
 *      Work over code points or grapheme clusters rather than Java char units.
 */
public class ReverseString {

    public static void main(String[] args) {
        ReverseString solver = new ReverseString();

        char[][] inputs = { {'h', 'e', 'l', 'l', 'o'}, {'a'}, {} };
        String[] expected = {"[o, l, l, e, h]", "[a]", "[]"};

        for (int i = 0; i < inputs.length; i++) {
            char[] current = inputs[i].clone();
            solver.reverseString(current);
            System.out.printf("s=%s -> %s  expected=%s%n",
                java.util.Arrays.toString(inputs[i]), java.util.Arrays.toString(current), expected[i]);
        }
    }

        /**
     * Intuition: the first character belongs where the last character is, the
     * second belongs where the second-last is, and so on. Swapping mirrored pairs
     * from the outside inward reverses the array in place.
     *
     * Algorithm:
     *   1. Return for null, empty, or single-character arrays.
     *   2. Place left at the start and right at the end.
     *   3. Swap input[left] with input[right].
     *   4. Move both pointers inward until they meet.
     *
     * Time:  O(n) - each character is swapped at most once.
     * Space: O(1) - the reversal uses one temporary character.
     *
     * @param input Character array mutated in place.
     */
    public void reverseString(char[] input) {
        if (input == null || input.length <= 1) {
            return;
        }

        int left = 0;
        int right = input.length - 1;

        while (left < right) {
            // Swap characters at left and right positions
            char temp = input[left];
            input[left] = input[right];
            input[right] = temp;

            // Move pointers toward center
            left++;
            right--;
        }
    }

    /**
     * XOR swap approach without temporary variable.
     */
    public void reverseStringXOR(char[] input) {
        if (input == null || input.length <= 1) {
            return;
        }

        int left = 0;
        int right = input.length - 1;

        while (left < right) {
            // XOR swap without temporary variable
            input[left] ^= input[right];
            input[right] ^= input[left];
            input[left] ^= input[right];

            left++;
            right--;
        }
    }
}
