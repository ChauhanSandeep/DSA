package Hashing;

import java.util.*;

/**
 * https://leetcode.com/problems/analyze-user-website-visit-pattern/
 */
public class WebsiteVisit {
  public static void main(String[] args) {
    String[] username = {"u1", "u1", "u1", "u2", "u2", "u2"};
    String[] website = {"a", "b", "a", "a", "b", "c"};
    int[] timestamp = {1, 2, 3, 4, 5, 6};

    List<String> result = new WebsiteVisit().mostVisitedPattern(username, timestamp, website);
    System.out.println(result);
  }

  public List<String> mostVisitedPattern(String[] username, int[] timestamp, String[] website) {
    Map<String, List<Pair>> userVisits = new HashMap<>();
    int n = username.length;

    // Collect the website visit info for each user
    for (int i = 0; i < n; i++) {
      userVisits.computeIfAbsent(username[i], k -> new ArrayList<>()).add(new Pair(timestamp[i], website[i]));
    }

    // Map to store the frequency of each 3-sequence pattern
    Map<String, Integer> patternFrequency = new HashMap<>();
    String mostFrequentPattern = null;

    for (String user : userVisits.keySet()) {
      List<Pair> visits = userVisits.get(user);
      visits.sort(Comparator.comparingInt(p -> p.time)); // Sort by timestamp

      Set<String> uniquePatterns = new HashSet<>();

      // Generate all possible 3-sequence patterns for the user
      for (int i = 0; i < visits.size(); i++) {
        for (int j = i + 1; j < visits.size(); j++) {
          for (int k = j + 1; k < visits.size(); k++) {
            String pattern = visits.get(i).web + " " + visits.get(j).web + " " + visits.get(k).web;

            // Ensure each user contributes only once per pattern
            if (uniquePatterns.add(pattern)) {
              patternFrequency.put(pattern, patternFrequency.getOrDefault(pattern, 0) + 1);

              // Update most frequent pattern based on frequency and lexicographical order
              if (mostFrequentPattern == null ||
                  patternFrequency.get(pattern) > patternFrequency.get(mostFrequentPattern) ||
                  (patternFrequency.get(pattern).equals(patternFrequency.get(mostFrequentPattern)) && pattern.compareTo(mostFrequentPattern) < 0)) {
                mostFrequentPattern = pattern;
              }
            }
          }
        }
      }
    }

    return mostFrequentPattern != null ? Arrays.asList(mostFrequentPattern.split(" ")) : Collections.emptyList();
  }
}

class Pair {
  int time;
  String web;

  public Pair(int time, String web) {
    this.time = time;
    this.web = web;
  }
}