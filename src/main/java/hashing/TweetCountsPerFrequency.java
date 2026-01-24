package hashing;

import java.util.*;

/**
 * Problem: Tweet Counts Per Frequency
 *
 * A social media company is trying to monitor activity on their site by analyzing the number of
 * tweets that occur in select periods of time. These periods can be partitioned into smaller
 * time chunks based on a certain frequency (every minute, hour, or day).
 *
 * Implement the TweetCounts class:
 * 1. TweetCounts() Initializes the TweetCounts object.
 * 2. void recordTweet(String tweetName, int time) Stores the tweetName at the recorded time (in seconds).
 * 3. List<Integer> getTweetCountsPerFrequency(String freq, String tweetName, int startTime, int endTime)
 *    Returns a list of integers representing the number of tweets with tweetName in each time chunk
 *    for the given period of time [startTime, endTime] (in seconds) and frequency freq.
 *    - freq is one of "minute", "hour", or "day" representing a frequency of every minute, hour, or day respectively.
 *
 * Example:
 * Input
 * ["TweetCounts","recordTweet","recordTweet","recordTweet","getTweetCountsPerFrequency",
 *  "getTweetCountsPerFrequency","recordTweet","getTweetCountsPerFrequency"]
 * [[],["tweet3",0],["tweet3",60],["tweet3",10],["minute","tweet3",0,59],
 *  ["minute","tweet3",0,60],["tweet3",120],["hour","tweet3",0,210]]
 *
 * Output
 * [null,null,null,null,[2],[2,1],null,[4]]
 *
 * LeetCode: https://leetcode.com/problems/tweet-counts-per-frequency
 *
 * Time Complexity:
 * - recordTweet: O(1) average time per operation
 * - getTweetCountsPerFrequency: O(n log n) where n is the number of tweets in the given time range
 * Space Complexity: O(n) where n is the total number of tweets recorded
 * LeetCode Contest Rating: 2037
 */
public class TweetCountsPerFrequency {
    private Map<String, TreeMap<Integer, Integer>> tweetMap;
    private Map<String, Integer> freqMap;

    public TweetCountsPerFrequency() {
        tweetMap = new HashMap<>();
        freqMap = new HashMap<>();
        freqMap.put("minute", 60);
        freqMap.put("hour", 3600);
        freqMap.put("day", 86400);
    }

    public void recordTweet(String tweetName, int time) {
        tweetMap.putIfAbsent(tweetName, new TreeMap<>());
        TreeMap<Integer, Integer> timeMap = tweetMap.get(tweetName);
        timeMap.put(time, timeMap.getOrDefault(time, 0) + 1);
    }

    public List<Integer> getTweetCountsPerFrequency(String freq, String tweetName, int startTime, int endTime) {
        List<Integer> result = new ArrayList<>();

        if (!tweetMap.containsKey(tweetName)) {
            return result;
        }

        int interval = freqMap.get(freq);
        int numIntervals = (endTime - startTime) / interval + 1;

        // Initialize result list with zeros
        for (int i = 0; i < numIntervals; i++) {
            result.add(0);
        }

        // Get all tweets in the time range [startTime, endTime]
        TreeMap<Integer, Integer> timeMap = tweetMap.get(tweetName);
        Map<Integer, Integer> subMap = timeMap.subMap(startTime, true, endTime, true);

        // Count tweets in each interval
        for (Map.Entry<Integer, Integer> entry : subMap.entrySet()) {
            int time = entry.getKey();
            int count = entry.getValue();
            int index = (time - startTime) / interval;

            if (index >= 0 && index < numIntervals) {
                result.set(index, result.get(index) + count);
            }
        }

        return result;
    }
}
