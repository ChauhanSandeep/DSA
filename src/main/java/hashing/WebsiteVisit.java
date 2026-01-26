package hashing;

import java.util.*;

/**
 * Problem: Analyze User Website Visit Pattern
 * https://leetcode.com/problems/analyze-user-website-visit-pattern/
 *
 * Given the visit history of multiple users, find the most frequently visited 3-sequence pattern.
 * If multiple sequences have the same frequency, return the lexicographically smallest one.
 * Input:
 * username = ["joe", "joe", "joe", "james", "james", "james", "james", "mary", "mary", "mary"],
 * timestamp = [1,2,3,4,5,6,7,8,9,10],
 * website = ["home", "about", "career", "home", "cart", "maps", "home", "home" ,"about" , "career"]
 * Output: ["home", "about", "career"]
 *
 * Explanation: The tuples in this example are:
 * ["joe", "home", 1], ["joe", "about" ,2], ["joe", "career", 31, ["james", "home", 4], ["james", "cart", 5],
 * ["james", "maps", 6], ["james", "home", 7], ["mary", "home" ,8], ["mary", "about", 9], and ["mary", "career", 10].
 *
 * The pattern ("home", "about", "career") has score 2 (joe and mary) .
 * The pattern ("home", "cart", "maps") has score 1 (james).
 * The pattern ("home", "cart", "home") has score 1 (james).
 * The pattern ("home", "maps", "home") has score 1 (james).
 * The pattern ("cart", "maps", "home") has score 1 (james).
 * The pattern ("home", "home", "home") has score 0 (no user visited home 3 times)
 *
 * Follow-ups:
 * Question: How would you handle large datasets where the number of users and visits is very high?
 * Answer: We could use a more memory-efficient data structure, such as a Trie, to store patterns and their frequencies.
 *
 * Question: How would you optimize the solution for real-time updates?
 * Answer: We could maintain a sliding window of recent visits and update the pattern counts incrementally.
 *
 * LeetCode Contest Rating: 1851
 */
public class WebsiteVisit {

    public static void main(String[] args) {
        String[] username = {"u1", "u1", "u1", "u2", "u2", "u2"};
        String[] website = {"a", "b", "a", "a", "b", "c"};
        int[] timestamp = {1, 2, 3, 4, 5, 6};

        List<String> result = new WebsiteVisit().mostVisitedPattern(username, timestamp, website);
        System.out.println(result); // Expected Output: [a, b, a]
    }

    /**
     * Steps:
     * 1: Store visits in a Map where key = username, value = list of (timestamp, website).
     * 2: Sort each user's visit list by timestamp.
     * 3: Generate all unique 3-sequence patterns per user.
     * 4: Count the occurrences of each pattern across all users.
     * 5: Find the most frequent pattern (break ties lexicographically).
     *
     *  Time Complexity: O(N log N) (sorting) + O(U * V^3) (pattern generation), where N is size of input, U is number of users and V is average number of visits per user
     *  Approx ≈ O(N log N) for typical cases
     *  Space Complexity: O(N) for storing user visits.
     */
    public List<String> mostVisitedPattern(String[] username, int[] timestamp, String[] website) {
        Map<String, List<Pair>> userVisits = new HashMap<>(); // Map of username to list of (timestamp, website) pairs
        int n = username.length;

        // Step 1: Collect user visits (store timestamps and website names)
        for (int i = 0; i < n; i++) {
            userVisits.computeIfAbsent(username[i], k -> new ArrayList<>()).add(new Pair(timestamp[i], website[i]));
        }

        // Step 2: Sort visits per user by timestamp
        for (List<Pair> visits : userVisits.values()) {
            visits.sort(Comparator.comparingInt(pair -> pair.time));
        }

        // Step 3: Generate unique 3-sequence patterns per user
        Map<String, Integer> patternFrequency = new HashMap<>();
        for (String user : userVisits.keySet()) {
            List<Pair> visits = userVisits.get(user);
            Set<String> uniquePatterns = generateUniquePatterns(visits);

            // Step 4: Update pattern frequency map
            for (String pattern : uniquePatterns) {
                patternFrequency.put(pattern, patternFrequency.getOrDefault(pattern, 0) + 1);
            }
        }

        // Step 5: Find the most frequent pattern (break ties lexicographically)
        return findMostFrequentPattern(patternFrequency);
    }

    /**
     * Generates all unique 3-sequence patterns for a user's visit history.
     * @param visits Sorted list of website visits by timestamp.
     * @return A set of unique 3-sequence patterns.
     */
    private Set<String> generateUniquePatterns(List<Pair> visits) {
        Set<String> uniquePatterns = new HashSet<>();
        int size = visits.size();

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                for (int k = j + 1; k < size; k++) {
                    String pattern = visits.get(i).web + " " + visits.get(j).web + " " + visits.get(k).web;
                    uniquePatterns.add(pattern);
                }
            }
        }
        return uniquePatterns;
    }

    /**
     * Finds the most frequent pattern from the pattern frequency map.
     * If multiple patterns have the same frequency, the lexicographically smallest one is returned.
     * @param patternFrequency Map of 3-sequence patterns and their frequencies.
     * @return List representing the most frequent 3-sequence pattern.
     */
    private List<String> findMostFrequentPattern(Map<String, Integer> patternFrequency) {
        String mostFrequentPattern = null;
        int maxFrequency = 0;

        for (Map.Entry<String, Integer> entry : patternFrequency.entrySet()) {
            String pattern = entry.getKey();
            int frequency = entry.getValue();

            if (frequency > maxFrequency || (frequency == maxFrequency && pattern.compareTo(mostFrequentPattern) < 0)) {
                mostFrequentPattern = pattern;
                maxFrequency = frequency;
            }
        }

        return mostFrequentPattern != null ? Arrays.asList(mostFrequentPattern.split(" ")) : Collections.emptyList();
    }

    /**
     * Helper class to store visit information (timestamp, website).
     */
    static class Pair {
        int time;
        String web;

        public Pair(int time, String web) {
            this.time = time;
            this.web = web;
        }
    }
}
