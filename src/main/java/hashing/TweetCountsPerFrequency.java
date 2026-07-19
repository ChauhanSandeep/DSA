package hashing;

import java.util.*;

/**
 * Problem: Tweet Counts Per Frequency
 *
 * Record tweet times and answer how many times a tweet name appears in each
 * minute, hour, or day-sized bucket over an inclusive time range.
 *
 * Leetcode: https://leetcode.com/problems/tweet-counts-per-frequency/ (Medium)
 * Rating:   2037
 * Pattern:  Hashing | TreeMap | Range query buckets
 *
 * Example:
 *   Input:  record tweet3 at 0, 60, and 10; query minute range [0, 60]
 *   Output: [2,1]
 *   Why:    times 0 and 10 fall in the first minute bucket, while time 60 starts
 *           the second bucket.
 *
 * Follow-ups:
 *   1. How would you reduce query time for repeated fixed-frequency queries?
 *      Maintain prefix counts per tweet and per bucket size.
 *   2. How would you support deleting a recorded tweet?
 *      Decrement the time count in the TreeMap and remove zero-count entries.
 *   3. How would you handle custom frequencies?
 *      Add the frequency-to-seconds mapping or accept the interval length directly.
 *   4. How would this scale across machines?
 *      Shard by tweet name and merge bucket counts from the relevant shards.
 *
 * Related: Time Based Key-Value Store (981), Design Log Storage System (635).
 */
public class TweetCountsPerFrequency {

    public static void main(String[] args) {
        TweetCountsPerFrequency tweetCounts = new TweetCountsPerFrequency();
        tweetCounts.recordTweet("tweet3", 0);
        tweetCounts.recordTweet("tweet3", 60);
        tweetCounts.recordTweet("tweet3", 10);

        List<Integer> minuteCounts = tweetCounts.getTweetCountsPerFrequency("minute", "tweet3", 0, 59);
        System.out.printf("input=%s -> %s  expected=%s%n",
            "tweet3 at [0, 60, 10], minute [0, 59]", minuteCounts, "[2]");

        tweetCounts.recordTweet("tweet3", 120);
        List<Integer> hourCounts = tweetCounts.getTweetCountsPerFrequency("hour", "tweet3", 0, 210);
        System.out.printf("input=%s -> %s  expected=%s%n",
            "tweet3 at [0, 60, 10, 120], hour [0, 210]", hourCounts, "[4]");
    }
    private Map<String, TreeMap<Integer, Integer>> tweetMap;
    private Map<String, Integer> freqMap;

    public TweetCountsPerFrequency() {
        tweetMap = new HashMap<>();
        freqMap = new HashMap<>();
        freqMap.put("minute", 60);
        freqMap.put("hour", 3600);
        freqMap.put("day", 86400);
    }

    /**
     * Intuition: each tweet name owns a sorted map from timestamp to how many
     * times it was recorded at that exact second. Recording is just incrementing
     * one timestamp bucket.
     *
     * Algorithm:
     *   1. Create the tweet's TreeMap if this is the first record for that name.
     *   2. Increment the count at the given timestamp.
     *
     * Time:  O(log n) - TreeMap insertion or update costs logarithmic time for that tweet.
     * Space: O(1) - one timestamp count is added or updated.
     *
     * @param tweetName name of the tweet being recorded
     * @param time timestamp in seconds
     */
    public void recordTweet(String tweetName, int time) {
        tweetMap.putIfAbsent(tweetName, new TreeMap<>());
        TreeMap<Integer, Integer> timeMap = tweetMap.get(tweetName);
        timeMap.put(time, timeMap.getOrDefault(time, 0) + 1);
    }

    /**
     * Intuition: the query range can be divided into equal-size buckets, and a
     * timestamp belongs to bucket (time - startTime) / interval. TreeMap narrows
     * the scan to only recorded times inside the requested inclusive range.
     *
     * Algorithm:
     *   1. Return an empty list if the tweet name has never been recorded.
     *   2. Create one zero-filled result bucket for each interval in the range.
     *   3. Scan recorded times in [startTime, endTime] and add each count to its bucket.
     *
     * Time:  O(b + m) - initializes b buckets and scans m recorded timestamps in range.
     * Space: O(b) - the returned list stores one count per bucket.
     *
     * @param freq one of minute, hour, or day
     * @param tweetName tweet name to query
     * @param startTime inclusive start time in seconds
     * @param endTime inclusive end time in seconds
     * @return counts per frequency bucket across the range
     */
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
