package strings.hashmap;

import java.util.HashSet;
import java.util.Set;

/**
 * Problem: Unique Email Addresses
 *
 * Normalize each email by applying Gmail-style local-name rules: dots in the
 * local part are ignored, and everything after the first plus sign is ignored.
 * Domains remain unchanged. Return the number of unique normalized addresses.
 *
 * Leetcode: https://leetcode.com/problems/unique-email-addresses/ (Easy)
 * Rating:   acceptance 67.7% (Easy), contest rating 1199
 * Pattern:  Hash set | String normalization | Local/domain split
 *
 * Example:
 *   Input:  emails = ["test.email+alex@leetcode.com","test.e.mail+bob.cathy@leetcode.com","testemail+david@lee.tcode.com"]
 *   Output: 2
 *   Why:    the first two normalize to the same leetcode.com address; the third has a different domain.
 *
 * Follow-ups:
 *   1. Domain names are case-insensitive?
 *      Normalize the domain with lowercase before inserting into the set.
 *   2. Need to group originals by normalized address?
 *      Use Map<String, List<String>> instead of a HashSet.
 *   3. Process invalid email formats?
 *      Decide whether to reject, skip, or report invalid entries during normalization.
 *
 * Related: Valid Email Address, Group Anagrams (49).
 */
public class UniqueEmailAddresses {

    public static void main(String[] args) {
        UniqueEmailAddresses solver = new UniqueEmailAddresses();
        String[][] inputs = {
            {"test.email+alex@leetcode.com", "test.e.mail+bob.cathy@leetcode.com", "testemail+david@lee.tcode.com"},
            {"a@leetcode.com", "a+b@leetcode.com", "a.b@leetcode.com"}
        };
        int[] expected = {2, 2};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.numUniqueEmails(inputs[i]);
            System.out.printf("emails=%s -> %d  expected=%d%n",
                java.util.Arrays.toString(inputs[i]), got, expected[i]);
            int gotStringMethods = solver.numUniqueEmailsStringMethods(inputs[i]);
            System.out.printf("stringMethods emails=%s -> %d  expected=%d%n",
                java.util.Arrays.toString(inputs[i]), gotStringMethods, expected[i]);
        }
    }


    /**
     * Intuition: after normalization, two emails reach the same inbox exactly when
     * their normalized strings are equal. A set naturally removes duplicates while
     * preserving only the number of distinct inboxes.
     *
     * Algorithm:
     *   1. Return 0 for null or empty email input.
     *   2. Normalize each email by splitting local and domain parts.
     *   3. In the local part, ignore dots and stop at the first plus sign.
     *   4. Add valid normalized addresses to a set and return its size.
     *
     * Time:  O(n * m) - n emails with average length m are scanned.
     * Space: O(n * m) - the set may store every normalized email.
     *
     * @param emails email addresses to normalize
     * @return number of unique normalized email addresses
     */
    public int numUniqueEmails(String[] emails) {
        if (emails == null || emails.length == 0) {
            return 0;
        }

        Set<String> uniqueEmails = new HashSet<>();

        for (String email : emails) {
            String normalizedEmail = normalizeEmail(email);
            if (normalizedEmail != null) {
                uniqueEmails.add(normalizedEmail);
            }
        }

        return uniqueEmails.size();
    }

    /** Normalizes one email address according to local-name dot and plus rules. */
    private String normalizeEmail(String email) {
        if (email == null || !email.contains("@")) {
            return null;
        }

        String[] parts = email.split("@");
        if (parts.length != 2) {
            return null;
        }

        String localName = parts[0];
        String domain = parts[1];

        // Process local name
        String processedLocal = processLocalName(localName);

        return processedLocal + "@" + domain;
    }

    /** Applies dot removal and plus truncation to the local name. */
    private String processLocalName(String localName) {
        StringBuilder result = new StringBuilder();

        for (char c : localName.toCharArray()) {
            if (c == '+') {
                // Ignore everything after first '+'
                break;
            } else if (c != '.') {
                // Add character if it's not a dot
                result.append(c);
            }
            // Ignore dots - they are not added to result
        }

        return result.toString();
    }

    /**
     * Alternative approach using string manipulation methods.
     * For interviews this approach is more concise and easier to implement quickly.
     *
     * Time complexity: O(n * m) where n is number of emails, m is average email length
     * Space complexity: O(n * m) for storing normalized emails in set
     */
    public int numUniqueEmailsStringMethods(String[] emails) {
        Set<String> uniqueEmails = new HashSet<>();

        for (String email : emails) {
            int atIndex = email.indexOf('@');
            if (atIndex == -1) continue;

            String local = email.substring(0, atIndex);
            String domain = email.substring(atIndex);

            // Process local part
            int plusIndex = local.indexOf('+');
            if (plusIndex != -1) {
                local = local.substring(0, plusIndex);
            }

            local = local.replace(".", "");

            uniqueEmails.add(local + domain);
        }

        return uniqueEmails.size();
    }
}
