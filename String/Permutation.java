package string;

import java.util.HashSet;
import java.util.Set;

/**
 * Problem: Generate all permutations of a given string.
 * 
 * Intuition:
 * - Use recursion and backtracking to generate permutations.
 * - Swap characters to create different orders.
 * - Avoid duplicate permutations using a HashSet.
 * 
 * Algorithm:
 * - Swap each character with every other character.
 * - Recur with a smaller portion of the string.
 * - Use a HashSet to store unique permutations.
 * 
 * Time Complexity: O(N * N!) - N! permutations and O(N) swaps per permutation.
 * Space Complexity: O(N!) - To store all permutations.
 * 
 * Reference: https://www.youtube.com/watch?v=TnZHaH9i6-0
 */
public class PermutationGenerator {

    public static void main(String[] args) {
        String input = "ABC";
        System.out.println(generatePermutations(input));
    }

    /**
     * Generates all unique permutations of the input string.
     *
     * @param input the string to permute
     * @return a set containing all unique permutations
     */
    public static Set<String> generatePermutations(String input) {
        Set<String> permutations = new HashSet<>();
        generatePermutationsHelper(input, 0, input.length() - 1, permutations);
        return permutations;
    }

    /**
     * Helper function to generate permutations using backtracking.
     *
     * @param str        the current string
     * @param leftIndex  the left boundary for swapping
     * @param rightIndex the right boundary for swapping
     * @param result     the set to store unique permutations
     */
    private static void generatePermutationsHelper(String str, int leftIndex, int rightIndex, Set<String> result) {
        if (leftIndex == rightIndex) {
            result.add(str);
            return;
        }
        
        for (int i = leftIndex; i <= rightIndex; i++) {
            str = swapCharacters(str, leftIndex, i);
            generatePermutationsHelper(str, leftIndex + 1, rightIndex, result);
            str = swapCharacters(str, leftIndex, i); // Backtrack
        }
    }

    /**
     * Swaps two characters in a given string.
     *
     * @param str the input string
     * @param i   first index
     * @param j   second index
     * @return new string with swapped characters
     */
    private static String swapCharacters(String str, int i, int j) {
        char[] charArray = str.toCharArray();
        char temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return new String(charArray);
    }

    /**
     * Alternative approach using prefix-based recursion.
     * This approach does not require swapping.
     * 
     * Time Complexity: O(N * N!)
     * Space Complexity: O(N!) for storing results.
     */
    public static Set<String> generatePermutationsAlt(String str) {
        Set<String> result = new HashSet<>();
        generatePermutationsAltHelper("", str, result);
        return result;
    }

    /**
     * Recursive function for generating permutations using prefix and remaining string.
     *
     * @param prefix the prefix built so far
     * @param remaining the remaining characters
     * @param result the set to store permutations
     */
    private static void generatePermutationsAltHelper(String prefix, String remaining, Set<String> result) {
        if (remaining.isEmpty()) {
            result.add(prefix);
            return;
        }
        
        for (int i = 0; i < remaining.length(); i++) {
            generatePermutationsAltHelper(prefix + remaining.charAt(i), 
                                          remaining.substring(0, i) + remaining.substring(i + 1), 
                                          result);
        }
    }
}
