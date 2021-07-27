package Hashing;

import java.util.*;

/**
 * https://leetcode.com/problems/analyze-user-website-visit-pattern/
 */
public class WebsiteVisit {
    public static void main(String[] args) {
        String[] username = {"u1","u1","u1","u2","u2","u2"};
        String[] website =  {"a", "b", "a", "a", "b", "c"};
        int[] timestamp = {1,2,3,4,5,6};

        List<String> result = new WebsiteVisit().mostVisitedPattern(username, timestamp, website);
        System.out.println(result);
    }

    public List<String> mostVisitedPattern(String[] username, int[] timestamp, String[] website) {
        Map<String, List<Pair>> userPairMap = new HashMap<>();
        int length = username.length;
        // collect the website info for every user, key: username, value: (timestamp, website)
        for (int i = 0; i < length; i++) {
            userPairMap.putIfAbsent(username[i], new ArrayList<>());
            userPairMap.get(username[i]).add(new Pair(timestamp[i], website[i]));
        }
        // patternFreqMap userPairMap to record every 3 combination occuring time for the different user.
        Map<String, Integer> patternFreqMap = new HashMap<>();
        String res = "";
        for (String user : userPairMap.keySet()) {
            Set<String> set = new HashSet<>();
            // this set is to avoid visit the same 3-seq in one user
            List<Pair> pairList = userPairMap.get(user);
            Collections.sort(pairList, (a, b) -> (a.time - b.time)); // sort by time
            // brutal force O(N ^ 3)
            for (int i = 0; i < pairList.size(); i++) {
                for (int j = i + 1; j < pairList.size(); j++) {
                    for (int k = j + 1; k < pairList.size(); k++) {
                        String str = pairList.get(i).web + " " + pairList.get(j).web + " " + pairList.get(k).web;
                        if (!set.contains(str)) {
                            patternFreqMap.put(str, patternFreqMap.getOrDefault(str, 0) + 1);
                            set.add(str);
                        }
                        if (res.equals("") || patternFreqMap.get(res) < patternFreqMap.get(str) || (patternFreqMap.get(res) == patternFreqMap.get(str) && res.compareTo(str) > 0)) {
                            // make sure the right lexi order
                            res = str;
                        }
                    }
                }
            }
        }
        // grab the right answer
        String[] splitArr = res.split(" ");
        List<String> result = new ArrayList<>();
        Collections.addAll(result, splitArr);
        return result;
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
