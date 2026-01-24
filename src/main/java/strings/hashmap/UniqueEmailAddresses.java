package strings.hashmap;

import java.util.HashSet;
import java.util.Set;

/**
 * Problem: Unique Email Addresses
 *
 * Every valid email consists of a local name and a domain name, separated by the '@' sign.
 * Besides lowercase letters, the email may contain one or more '.' or '+'.
 *
 * If you add periods '.' between some characters in the local name part of an email address,
 * mail sent there will be forwarded to the same address without dots in the local name.
 * Note that this rule does not apply to domain names.
 *
 * If you add a plus '+' in the local name, everything after the first plus sign will be ignored.
 * This allows certain emails to be filtered. Note that this rule does not apply to domain names.
 *
 * It is possible to use both of these rules at the same time.
 *
 * Example:
 * Input: emails = ["test.email+alex@leetcode.com","test.e.mail+bob.cathy@leetcode.com","testemail+david@lee.tcode.com"]
 * Output: 2
 * Explanation: "testemail@leetcode.com" and "testemail@lee.tcode.com" actually receive mails.
 * The first two emails normalize to the same address "testemail@leetcode.com".
 *
 * LeetCode Link: https://leetcode.com/problems/unique-email-addresses/
 *
 * 1. What if domain names also have special rules like case-insensitivity?
 *    Answer: We would normalize the domain name by converting it to lowercase before
 *    concatenating with the local name. The algorithm structure remains the same.
 *
 * 2. How would you handle invalid email formats in the input?
 *    Answer: Add validation to check for '@' presence, non-empty local and domain parts,
 *    and proper character sets. Return early or skip invalid emails based on requirements.
 *
 * 3. Can you optimize for space if the email list is extremely large?
 *    Answer: Instead of storing full normalized emails, we could use a hash of the normalized
 *    email or implement a streaming approach that processes emails in batches.
 *
 * 4. How would you extend this to support additional filtering rules?
 *    Answer: Refactor the normalization logic into a separate method that accepts rule
 *    configurations. Use strategy pattern to apply different rule sets dynamically.
 *
 * 5. What if we need to group emails by their normalized form?
 *    Answer: Use HashMap<String, List<String>> instead of HashSet, where key is the normalized
 *    email and value is the list of original emails that map to it.
 * LeetCode Contest Rating: 1199
 **/
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

            local = local.replaceAll(".", "");

            uniqueEmails.add(local + domain);
        }

        return uniqueEmails.size();
    }
}
