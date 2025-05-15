package string;

/**
 * Problem: Longest Possible Chunked Palindrome
 * https://www.geeksforgeeks.org/longest-possible-chunked-palindrome/
 *
 * Given a string, determine the maximum number of palindromic chunks it can be split into.
 *
 * Approach:
 * - The algorithm works by finding the longest matching prefix and suffix in the string.
 * - If a match is found, the string is split into chunks and the process is repeated recursively on the remaining substring.
 * - The base case occurs when no matching prefix and suffix are found, in which case the string is considered as a single chunk.
 *
 * Time Complexity: O(n) (Each character is processed once)
 * Space Complexity: O(n) (Recursive stack depth)
 */
public class PallindromeChunks {

    public static void main(String[] args) {
        System.out.println("Max chunks (Recursive): " + maxChunksRecursive("apple bus machine bus apple"));
        System.out.println("Max chunks (Recursive): " + maxChunksRecursive("volvo"));
    }

    /**
     * Recursively finds the maximum number of palindromic chunks in the given string.
     *
     * @param inputString The input string to be processed.
     * @return The maximum number of palindromic chunks.
     */
    public static int maxChunksRecursive(String inputString) {
        // Handle edge case where string is null or empty
        if (inputString == null || inputString.isEmpty()) {
            return 0;
        }

        return findChunks(inputString, 0, inputString.length());
    }

    /**
     * Helper method to recursively split the string into palindromic chunks.
     *
     * @param str   The string to be processed.
     * @param left  The current left index of the substring.
     * @param right The current right index of the substring.
     * @return The number of palindromic chunks in the substring.
     */
    private static int findChunks(String str, int left, int right) {
        int currentLength = right - left;

        // Base cases: if string is empty or consists of a single character
        if (currentLength == 0) {
            return 0; // Empty string, no chunks
        }
        if (currentLength == 1) {
            return 1; // Single character string, is considered one chunk
        }

        // Try to find a matching prefix and suffix
        for (int len = 1; len <= currentLength / 2; len++) {
            // Extract potential prefix and suffix
            String prefix = str.substring(left, left + len);
            String suffix = str.substring(right - len, right);

            if (prefix.equals(suffix)) {
                // Recursively process the remaining part of the string
                return 2 + findChunks(str, left + len, right - len);
            }
        }

        // If no matching chunks are found, treat the entire string as one chunk
        return 1;
    }
}