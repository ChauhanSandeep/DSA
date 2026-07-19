package hashing;

import java.util.*;

/**
 * Problem: Analyze User Website Visit Pattern
 *
 * Given users, timestamps, and websites, find the 3-website sequence visited by
 * the largest number of users. If multiple sequences have the same score, return
 * the lexicographically smallest sequence.
 *
 * Leetcode: https://leetcode.com/problems/analyze-user-website-visit-pattern/ (Medium)
 * Rating:   1851
 * Pattern:  Hashing | Sorting | Combination generation | Tie-breaking
 *
 * Example:
 *   Input:  joe visits home, about, career; mary visits home, about, career
 *   Output: [home, about, career]
 *   Why:    two distinct users have that 3-sequence, giving it the highest score.
 *
 * Follow-ups:
 *   1. How would you handle users with thousands of visits?
 *      Generate combinations carefully, deduplicate per user, and consider pruning or streaming counts.
 *   2. How would you support k-sequences instead of only length 3?
 *      Replace the triple loop with backtracking or iterative combination generation of size k.
 *   3. How would real-time updates be supported?
 *      Keep each user's ordered visits and update only new sequences formed by the latest visit.
 *   4. How would you break ties by earliest occurrence instead?
 *      Store the tie metadata alongside each pattern count and compare it before lexicographic order.
 *
 * Related: Top K Frequent Words (692), Design In-Memory File System (588).
 */
public class WebsiteVisit {

    public static void main(String[] args) {
        WebsiteVisit solver = new WebsiteVisit();
        String[][] usernames = {
            {"joe", "joe", "joe", "james", "james", "james", "james", "mary", "mary", "mary"},
            {"u1", "u1", "u1", "u2", "u2", "u2"}
        };
        int[][] timestamps = {
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
            {1, 2, 3, 4, 5, 6}
        };
        String[][] websites = {
            {"home", "about", "career", "home", "cart", "maps", "home", "home", "about", "career"},
            {"a", "b", "c", "a", "b", "d"}
        };
        String[] expected = { "[home, about, career]", "[a, b, c]" };

        for (int i = 0; i < usernames.length; i++) {
            List<String> got = solver.mostVisitedPattern(usernames[i], timestamps[i], websites[i]);
            System.out.printf("username=%s website=%s -> %s  expected=%s%n",
                Arrays.toString(usernames[i]), Arrays.toString(websites[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: a pattern's score counts users, not visits, so each user should
     * contribute at most once to the same 3-sequence. Sort each user's history by
     * time, generate that user's unique sequences, then count them globally.
     *
     * Algorithm:
     *   1. Group each visit by username with its timestamp and website.
     *   2. Sort every user's visits by timestamp.
     *   3. Generate all unique 3-sequences per user and add one global count for each.
     *   4. Return the highest-count sequence, breaking ties lexicographically.
     *
     * Time:  O(N log N + U * V^3) - sorting visits plus triples per user.
     * Space: O(N + P) - stores visits and generated pattern counts.
     *
     * @param username usernames for each visit
     * @param timestamp timestamps for each visit
     * @param website websites for each visit
     * @return most frequent 3-website pattern with lexicographic tie-break
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
