package strings;

import java.util.HashSet;
import java.util.Set;

/**
 * LeetCode 929. Unique Email Addresses
 *
 * Every valid email consists of a local name and a domain name, separated by the '@' sign.
 * Besides lowercase letters, the email may contain one or more '.' or '+'.
 *
 * For local names:
 * - If you add periods '.' between some characters in the local name, mail will still be forwarded to the same address.
 * - If you add a plus '+' in the local name, everything after the first '+' sign will be ignored.
 *
 * Example 1:
 * Input: emails = ["test.email+alex@leetcode.com","test.e.mail+bob.cathy@leetcode.com","testemail+david@lee.tcode.com"]
 * Output: 2
 * Explanation: "testemail@leetcode.com" and "testemail@lee.tcode.com" actually receive mails.
 *
 * LeetCode Link: https://leetcode.com/problems/unique-email-addresses/
 *
 * Follow-up Questions:
 * - How would you handle case-insensitive domains? (Convert domain to lowercase)
 * - Can you optimize for very large email lists? (Use more efficient string processing)
 * - How would you extend to support different email providers with different rules? (Provider-specific processing)
 * - What if we need to return actual unique emails instead of count? (Store normalized emails in set)
 */
public class UniqueEmailAddresses {

    /**
     * Counts unique email addresses after applying local name rules.
     *
     * Algorithm:
     * 1. For each email, split into local and domain parts at '@'
     * 2. Process local part: remove dots, ignore everything after first '+'
     * 3. Keep domain part unchanged
     * 4. Combine processed local and domain parts
     * 5. Use HashSet to track unique normalized emails
     * 6. Return count of unique emails
     *
     * Time Complexity: O(n * m) where n is number of emails, m is average email length
     * Space Complexity: O(n * m) for storing normalized emails in set
     *
     * @param emails Array of email addresses to normalize and count
     * @return Number of unique email addresses after normalization
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

    // Helper method to normalize email according to rules
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

    // Helper method to process local name according to rules
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
     * Optimized single-pass approach without helper methods.
     */
    public int numUniqueEmailsOptimized(String[] emails) {
        Set<String> uniqueEmails = new HashSet<>();

        for (String email : emails) {
            StringBuilder normalizedEmail = new StringBuilder();
            boolean reachedDomain = false;
            boolean ignoringAfterPlus = false;

            for (char c : email.toCharArray()) {
                if (c == '@') {
                    reachedDomain = true;
                    normalizedEmail.append(c);
                } else if (reachedDomain) {
                    // In domain part - keep everything as is
                    normalizedEmail.append(c);
                } else {
                    // In local part - apply rules
                    if (c == '+') {
                        ignoringAfterPlus = true;
                    } else if (!ignoringAfterPlus && c != '.') {
                        normalizedEmail.append(c);
                    }
                }
            }

            uniqueEmails.add(normalizedEmail.toString());
        }

        return uniqueEmails.size();
    }

    /**
     * Alternative approach using string manipulation methods.
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
