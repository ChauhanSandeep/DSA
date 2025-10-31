## Aditya Verma String Matching Playlis


**Core Idea:** String matching algorithms aim to find occurrences of a pattern (short string) within a larger text (long string).

**I. Naive Approach**

*   **Concept:** The simplest string matching approach. It slides the pattern over the text, one character at a time, and compares each character of the pattern with the corresponding characters in the text.
*   **Intuition:**  Straightforward, but inefficient. It re-checks characters in the text multiple times.
*   **Time Complexity:** O(m\*n) where 'n' is the length of the text and 'm' is the length of the pattern. In the worst-case scenario (e.g., text = "AAAAAAA", pattern = "AAAAB"), the algorithm performs redundant comparisons.
*   **Java Code:**

```java
public class NaiveStringMatching {

    public static void naiveSearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i <= n - m; i++) { // Iterate through possible starting positions
            int j;
            for (j = 0; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break; // Mismatch, move to the next starting position
                }
            }

            if (j == m) {
                System.out.println("Pattern found at index " + i); // All characters matched
            }
        }
    }

    public static void main(String[] args) {
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        naiveSearch(text, pattern);
    }
}
```

*   **Explanation:**

    *   The outer loop iterates from `i = 0` to `n - m` because if we start any later than that, the pattern wouldn't fit within the text.
    *   The inner loop compares `pattern.charAt(j)` with `text.charAt(i + j)`.  `i + j` calculates the index in the text that corresponds to the current character being compared in the pattern.
    *   If the inner loop completes without finding a mismatch (i.e., `j == m`), it means the pattern has been found at index `i`.

**II. Rabin-Karp Algorithm**

*   **Concept:** Uses hashing to find potential matches quickly.  It calculates a hash value for the pattern and then calculates a hash value for a sliding window of the same size in the text. If the hash values match, it performs a character-by-character comparison to verify.
*   **Intuition:**  Avoids redundant character comparisons by comparing hash values first.  Hash collisions can occur, requiring the final verification step.
*   **Time Complexity:** Average: O(n + m), Worst: O(m\*n) (when there are many hash collisions)
*   **Key Ideas:**

    *   **Rolling Hash:** Efficiently recalculates the hash value of the next window based on the previous window, avoiding recalculating the entire hash.
    *   **Prime Number:** Used as the base in the hash function to reduce the number of collisions. A large prime number is generally a good choice.
    *   **Modulo Operator:** Used to keep the hash values within a manageable range and prevent integer overflow.
*   **Java Code:**

```java
public class RabinKarp {

    private static final int PRIME = 101; // A prime number for hashing

    public static void rabinKarpSearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        long patternHash = createHash(pattern, m);
        long textHash = createHash(text, m);

        for (int i = 0; i <= n - m; i++) {
            if (patternHash == textHash && check(text, pattern, i, i + m - 1)) {
                System.out.println("Pattern found at index " + i);
            }
            if (i < n - m) {
                textHash = recalculateHash(text, i, i + m, textHash, m);
            }
        }
    }

    private static long createHash(String str, int length) {
        long hash = 0;
        for (int i = 0; i < length; i++) {
            hash += str.charAt(i) * Math.pow(PRIME, length - 1 - i);
        }
        return hash;
    }

    private static long recalculateHash(String text, int oldIndex, int newIndex, long oldHash, int patternLength) {
        long newHash = oldHash - text.charAt(oldIndex) * (long) Math.pow(PRIME, patternLength - 1);
        newHash = newHash * PRIME + text.charAt(newIndex);
        return newHash;
    }

    private static boolean check(String text, String pattern, int start, int end) {
        int j = 0;
        for (int i = start; i <= end; i++) {
            if (text.charAt(i) != pattern.charAt(j)) {
                return false;
            }
            j++;
        }
        return true;
    }

    public static void main(String[] args) {
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        rabinKarpSearch(text, pattern);
    }
}
```

*   **Explanation:**

    *   `createHash(String str, int length)`: Calculates the initial hash value of the pattern and the first window of the text.  The hash function used here weights each character based on its position.
    *   `recalculateHash(String text, int oldIndex, int newIndex, long oldHash, int patternLength)`: Efficiently recalculates the hash value for the next window. It removes the contribution of the character leaving the window (`text.charAt(oldIndex)`) and adds the contribution of the character entering the window (`text.charAt(newIndex)`).  Crucially, it uses the previous hash value to do this efficiently.
    *   `check(String text, String pattern, int start, int end)`: Verifies the match by comparing the actual characters when hash values match.  This is essential to handle hash collisions.
    *   The main loop iterates through the text. It compares the hash values, and if they match, it calls `check()` to verify the actual characters. Then, it updates the text hash to the hash of the next substring.
*   **Key Improvements over Naive:**  Uses hashing to quickly eliminate many potential matches without character-by-character comparisons.  The rolling hash technique is a significant optimization.

**III. Knuth-Morris-Pratt (KMP) Algorithm**

*   **Concept:**  Builds a "longest proper prefix suffix" (LPS) array for the pattern. This array helps to avoid unnecessary comparisons by indicating how much to shift the pattern when a mismatch occurs.
*   **Intuition:**  When a mismatch occurs, the KMP algorithm "knows" how much of the text has already been matched with the pattern. The LPS array tells it the longest prefix of the pattern that is also a suffix of the portion matched so far. This lets the algorithm shift the pattern more intelligently than the naive approach, potentially skipping over sections of the text it knows won't match.
*   **Time Complexity:** O(n + m), where 'n' is the length of the text and 'm' is the length of the pattern.
*   **Key Ideas:**

    *   **LPS Array:** The heart of the KMP algorithm. `LPS[i]` stores the length of the longest proper prefix of `pattern[0...i]` which is also a suffix of `pattern[0...i]`. "Proper" means the prefix/suffix cannot be the entire substring itself.
    *   **Prefix and Suffix Overlap:** The LPS array efficiently encodes information about the overlap between prefixes and suffixes of the pattern.
*   **Java Code:**

```java
public class KMP {

    public static void kmpSearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        int[] lps = computeLPSArray(pattern);

        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < n) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }

            if (j == m) {
                System.out.println("Pattern found at index " + (i - j));
                j = lps[j - 1];  // prepare for the next match
            }

            // mismatch after j matches
            else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                // Do not match lps[0..lps[j-1]] characters,
                // they will match anyway
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
    }

    private static int[] computeLPSArray(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];

        int length = 0; // length of the previous longest prefix suffix
        int i = 1;

        lps[0] = 0; // lps[0] is always 0

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                    // Also, note that we do not increment i here.
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    public static void main(String[] args) {
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        kmpSearch(text, pattern);
    }
}
```

*   **Explanation:**

    *   `computeLPSArray(String pattern)`: Calculates the LPS array for the given pattern. This is a crucial pre-processing step.
        *   `lps[0]` is always 0.
        *   The `while` loop iterates through the pattern, building the LPS array.
        *   If `pattern.charAt(i) == pattern.charAt(length)`, it means we've found a longer proper prefix that's also a suffix. We increment `length` and set `lps[i] = length`.
        *   If there's a mismatch and `length != 0`, we set `length = lps[length - 1]`. This is the key optimization: we don't reset `length` to 0, but instead use the LPS array to find the next possible shorter matching prefix/suffix.
        *   If `length == 0`, it means there's no proper prefix that's also a suffix, so we set `lps[i] = 0`.
    *   `kmpSearch(String text, String pattern)`: Performs the actual search using the LPS array.
        *   `i` is the index for the text, and `j` is the index for the pattern.
        *   The `while` loop iterates through the text.
        *   If `pattern.charAt(j) == text.charAt(i)`, we increment both `i` and `j`.
        *   If `j == m`, it means we've found a match. We print the index and reset `j = lps[j - 1]` to find potential overlapping matches.
        *   If there's a mismatch, we use the LPS array to shift the pattern intelligently: `j = lps[j - 1]`. If `j` is already 0, we simply increment `i`.
*   **Key Improvements over Naive and Rabin-Karp:** The KMP algorithm avoids redundant comparisons by using the LPS array to "remember" previous matches and shift the pattern accordingly. This guarantees O(n + m) time complexity.  Rabin-Karp can have worst-case O(m\*n) due to collisions.

**V. Z Algorithm**

*   **Concept:** The Z algorithm calculates the Z array for a given string. The Z array, `Z[i]`, stores the length of the longest substring starting at index `i` that matches a prefix of the string.  It can be used for string matching by concatenating the pattern and text with a delimiter.
*   **Intuition:**  Similar to KMP, the Z algorithm preprocesses the input string (pattern + delimiter + text) to find information about repeated prefixes. This information is then used to efficiently locate occurrences of the pattern within the text. It avoids redundant comparisons by reusing previously calculated Z values.
*   **Time Complexity:** O(n + m), where 'n' is the length of the text and 'm' is the length of the pattern. The construction of the Z array takes linear time.
*   **Key Ideas:**

    *   **Z Array:**  `Z[i]` stores the length of the longest substring starting from index `i` that matches a prefix of the entire string.
    *   **Sliding Window:**  The algorithm maintains a sliding window [L, R] that represents the rightmost interval [L, R] such that s[L...R] is a prefix of s.  This window is updated as the algorithm progresses.
*   **Java Code:**

```java
public class ZAlgorithm {

    public static void zAlgorithmSearch(String text, String pattern) {
        String combinedString = pattern + "$" + text; // Concatenate pattern and text with a delimiter
        int n = combinedString.length();
        int m = pattern.length();
        int[] z = computeZArray(combinedString);

        for (int i = m + 1; i < n; i++) { // Start from m+1 because that is the beginning of the text
            if (z[i] == m) {
                System.out.println("Pattern found at index " + (i - m - 1));
            }
        }
    }

    private static int[] computeZArray(String str) {
        int n = str.length();
        int[] z = new int[n];
        int L = 0, R = 0;

        for (int i = 1; i < n; i++) {
            if (i > R) {
                // Outside the current window
                L = R = i;
                while (R < n && str.charAt(R - L) == str.charAt(R)) {
                    R++;
                }
                z[i] = R - L;
                R--;
            } else {
                // Inside the current window
                int k = i - L; // Relative position to the beginning of the window

                if (z[k] < R - i + 1) {
                    // Z[k] is smaller than the remaining window size
                    z[i] = z[k];
                } else {
                    // Z[k] is greater than or equal to the remaining window size.  Need to extend.
                    L = i;
                    while (R < n && str.charAt(R - L) == str.charAt(R)) {
                        R++;
                    }
                    z[i] = R - L;
                    R--;
                }
            }
        }
        return z;
    }

    public static void main(String[] args) {
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        zAlgorithmSearch(text, pattern);
    }
}
```

*   **Explanation:**

    *   `computeZArray(String str)`: Calculates the Z array for the given string.
        *   `L` and `R` define the boundaries of the current "window". The window [L, R] represents the substring str[L...R] which is also a prefix of the input `str`.
        *   If `i > R`, it means we're outside the current window. We reset the window, and compare the substring starting at `i` with the prefix of `str` to find Z[i].
        *   If `i <= R`, it means we're inside the current window. We calculate `k = i - L`, which is the index relative to the prefix, then look at `z[k]`.
            *   If `z[k] < R - i + 1`, then `z[i] = z[k]`. That means the value is already known (it's smaller than the remaining part of the window).
            *   If `z[k] >= R - i + 1`, it means we need to extend the window and recalculate Z[i].
    *   `zAlgorithmSearch(String text, String pattern)`:  Concatenates the pattern and text, calculates the Z array, and then iterates through the Z array to find matches.  If `z[i] == m`, it means we've found a match at index `i - m - 1` in the original text.
*   **Key Ideas**

    *   The main advantage is that calculating the Z array reuses information about previous matches. This leads to O(n+m) runtime.

**VII. Boyer-Moore Algorithm**

*   **Concept:** The Boyer-Moore algorithm is a string matching algorithm that is often faster than KMP in practice. Unlike the algorithms we've seen so far, it compares the pattern with the text *from right to left*. It uses two heuristics: the "bad character heuristic" and the "good suffix heuristic" to skip over portions of the text.
*   **Intuition:** The Boyer-Moore algorithm gains efficiency by using information gathered during mismatches to shift the pattern more aggressively than KMP or the Z algorithm. The right-to-left comparison allows it to quickly identify mismatches and utilize the heuristics effectively.
*   **Time Complexity:**
    *   **Best Case:** O(n/m) (where the pattern doesn't appear in the text, and the heuristics allow large skips).
    *   **Average Case:** Sublinear (better than O(n) in many practical scenarios).
    *   **Worst Case:** O(m\*n) (can occur with certain patterns, but less common than in the naive algorithm).
*   **Key Ideas:**

    *   **Right-to-Left Comparison:** Compares the pattern with the text from the last character to the first.
    *   **Bad Character Heuristic:** When a mismatch occurs, this heuristic looks at the mismatched character in the text.  It determines how far to shift the pattern to the right so that the rightmost occurrence of that character in the pattern aligns with the mismatched character in the text. If the mismatched character doesn't appear in the pattern, the pattern is shifted completely past the mismatched character.
    *   **Good Suffix Heuristic:** When a mismatch occurs, this heuristic considers the portion of the pattern that *did* match (the "good suffix").  It determines how far to shift the pattern to align the good suffix with another occurrence of the same suffix in the pattern (or, if there is no other occurrence, with a prefix of the pattern).
    *   **Combining Heuristics:** The algorithm shifts the pattern by the *maximum* of the shifts suggested by the bad character and good suffix heuristics.

*   **Java Code:**

```java
import java.util.Arrays;

public class BoyerMoore {

    public static void boyerMooreSearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        int[] badChar = badCharacterHeuristic(pattern);
        int[] goodSuffix = goodSuffixHeuristic(pattern);

        int s = 0; // s is shift of the pattern with respect to text
        while (s <= (n - m)) {
            int j = m - 1;

            /* Keep reducing index j of pattern while characters of
               pattern and text are matching at this shift s */
            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j))
                j--;

            /* If the pattern is present at current shift, then
               index j will become -1 after the above loop */
            if (j < 0) {
                System.out.println("Pattern occurs at shift = " + s);

                /* Shift the pattern so that the next possible shift does not give a false match */
                s += (s + m < n) ? m - badChar[text.charAt(s + m)] : 1;
            } else {
                /* Shift the pattern by max of the shifts suggested by the bad
                   character and good suffix rules */
                int badCharShift = Math.max(1, j - badChar[text.charAt(s + j)]);
                int goodSuffixShift = goodSuffix[j];
                s += Math.max(badCharShift, goodSuffixShift);
            }
        }
    }

    private static int[] badCharacterHeuristic(String pattern) {
        final int ALPHABET_SIZE = 256; // Assuming ASCII characters
        int[] badChar = new int[ALPHABET_SIZE];
        Arrays.fill(badChar, -1);

        for (int i = 0; i < pattern.length(); i++) {
            badChar[pattern.charAt(i)] = i; // Last occurrence of each character
        }
        return badChar;
    }

    private static int[] goodSuffixHeuristic(String pattern) {
        int m = pattern.length();
        int[] goodSuffix = new int[m];

        // Precompute the border array
        int[] border = new int[m + 1];
        border[m] = borderValue(pattern);

        for (int i = m - 1; i >= 0; i--) {
            String suffix = pattern.substring(i, m);
            border[i] = borderValue(suffix);
        }

        // Use the border array to compute the goodSuffix array
        for (int i = 0; i < m; i++) {
            goodSuffix[i] = m - border[i + 1];
        }

        return goodSuffix;
    }

    private static int borderValue(String str) {
        int n = str.length();
        for (int len = n - 1; len > 0; len--) {
            if (str.substring(0, len).equals(str.substring(n - len))) {
                return len;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        boyerMooreSearch(text, pattern);
    }
}
```

*   **Explanation:**

    *   `badCharacterHeuristic(String pattern)`: Creates the bad character table.  For each character in the alphabet (assuming ASCII), it stores the index of its *rightmost* occurrence in the pattern. If a character isn't in the pattern, its entry in the table is -1.
    *   `goodSuffixHeuristic(String pattern)`: Creates the good suffix table. This is more complex and involves finding the longest border (prefix that is also a suffix) of each suffix of the pattern.  The computation involves border array calculation.
    *   `boyerMooreSearch(String text, String pattern)`: The main search function.
        *   It compares the pattern and text from right to left.
        *   If a mismatch occurs, it calculates the shift based on both the bad character and good suffix heuristics and takes the maximum.
        *   If the pattern matches completely, it prints the index and handles the shift to find the next possible match.

*   **Key Advantages:**

    *   **Sublinear Average Time Complexity:**  Often significantly faster than KMP in practice due to the aggressive shifts.
    *   **Effective Heuristics:** The bad character and good suffix heuristics are well-designed to skip over large portions of the text.

*   **Disadvantages:**

    *   **More Complex Implementation:**  The good suffix heuristic is relatively complex to implement correctly.
    *   **Worst-Case Scenario:**  While less common than in the naive algorithm, there are still cases where the Boyer-Moore algorithm can have O(m\*n) time complexity.

**VIII. Updated Comparison and When to Use**

Here's an updated comparison table, incorporating Boyer-Moore:

| Algorithm     | Time Complexity (Best) | Time Complexity (Avg) | Time Complexity (Worst) | Space Complexity | Implementation Complexity | Use Cases                                                                                                            |
|---------------|-------------------------|--------------------------|--------------------------|-------------------|---------------------------|----------------------------------------------------------------------------------------------------------------------|
| Naive         | O(n)                    | O(m\*n)                  | O(m\*n)                  | O(1)              | Very Simple               | Small patterns/texts, educational purposes.                                                                        |
| Rabin-Karp    | O(n)                    | O(n + m)                 | O(m\*n)                  | O(1)              | Moderate                  | Cases where rolling hash is suitable.                                                                                  |
| KMP           | O(n)                    | O(n + m)                 | O(n + m)                 | O(m)              | Moderate                  | Guaranteed linear time, patterns with repeating structures.                                                             |
| Z Algorithm   | O(n)                    | O(n + m)                 | O(n + m)                 | O(n + m)            | Moderate                  | Patterns with repeating structures.                                                                                           |
| Boyer-Moore   | O(n/m)                   | Sublinear                | O(m\*n)                  | O(1) / O(m)       | Complex                   | Generally the fastest in practice, especially for larger alphabets and patterns, but can have worst-case behavior.        |

**Key Points to Remember**

*   **Understand the Heuristics:**  Focus on understanding the bad character and good suffix heuristics in Boyer-Moore. This is key to grasping why it's often so efficient.
*   **Implementation Details:** The good suffix heuristic is tricky to implement correctly. Pay close attention to the border array calculation.
*   **Consider the Alphabet Size:** Boyer-Moore tends to perform better with larger alphabets (e.g., standard ASCII) because the bad character heuristic is more likely to be effective.
*   **Trade-offs:** As always, consider the trade-offs between implementation complexity, memory usage, and expected performance when choosing an algorithm.



