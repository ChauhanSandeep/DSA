package arrays.binarysearch;

import java.util.*;

/**
 * Problem: Design TimeMap with set(key, value, timestamp) and get(key,
 * timestamp)
 * Returns value with largest timestamp_prev <= timestamp, or "" if none exists.
 * Timestamps strictly increasing.
 * 
 * Leetcode: https://leetcode.com/problems/time-based-key-value-store/
 *
 * Key Requirements:
 * - Multiple values per key over time
 * - get(t) returns most recent value where timestamp_prev ≤ t
 * - O(log n) per operation, O(n) space
 * 
 * Two Approaches:
 * 1. ArrayList-based: Requires timestamps in strictly increasing order
 * (original problem constraint)
 * 2. TreeMap-based: Handles arbitrary timestamp insertion order
 * LeetCode Contest Rating: 1575
 **/
public class TimeMap {
    private Map<String, List<Pair>> map;
    private Map<String, TreeMap<Integer, String>> treeMapStorage;

    public TimeMap() {
        map = new HashMap<>();
        treeMapStorage = new HashMap<>();
    }

    /**
     * Store the key with value at given timestamp.
     * 
     * Time complexity: O(1)
     * Space complexity: O(1)
     * 
     * @param key
     * @param value
     * @param timestamp
     */
    public void set(String key, String value, int timestamp) {
        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<>());
        }
        map.get(key).add(new Pair(timestamp, value));
    }

    /**
     * Retrieve value for key at given timestamp.
     * Uses binary search to find rightmost timestamp <= target
     * Note :This works only because timestamps are strictly
     * increasing order while inserting.
     * 
     * Time complexity: O(log n)
     * Space complexity: O(1)
     */
    public String get(String key, int timestamp) {
        List<Pair> history = map.get(key);
        if (history == null)
            return "";

        // Binary search: find rightmost timestamp <= target
        int left = 0;
        int right = history.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (history.get(mid).timestamp <= timestamp) {
                left = mid + 1; // Try to find larger timestamp
            } else {
                right = mid;
            }
        }
        // left-1 is the last valid index (if exists)
        return left == 0 ? "" : history.get(left - 1).value;
    }

    private class Pair {
        int timestamp;
        String value;

        Pair(int t, String v) {
            timestamp = t;
            value = v;
        }

        // ============== TreeMap-based Approach ==============
        // Use this when timestamps are NOT guaranteed to be in increasing order

        /**
         * Store the key with value at given timestamp using TreeMap.
         * This approach handles arbitrary insertion order of timestamps.
         * TreeMap automatically maintains sorted order by timestamp.
         * 
         * Time complexity: O(log n) - TreeMap insertion
         * Space complexity: O(1) per operation
         * 
         * @param key
         * @param value
         * @param timestamp
         */
        public void setWithTreeMap(String key, String value, int timestamp) {
            if (!treeMapStorage.containsKey(key)) {
                treeMapStorage.put(key, new TreeMap<>());
            }
            treeMapStorage.get(key).put(timestamp, value);
        }

        /**
         * Retrieve value for key at given timestamp using TreeMap.
         * Uses TreeMap's floorEntry() to efficiently find the largest timestamp <=
         * target.
         * 
         * Advantages over ArrayList approach:
         * - Handles out-of-order timestamp insertions
         * - No need to maintain sorted order manually
         * - Built-in efficient floor lookup
         * 
         * Time complexity: O(log n) - TreeMap floorEntry lookup
         * Space complexity: O(1)
         * 
         * @param key
         * @param timestamp
         * @return value with largest timestamp <= given timestamp, or "" if none exists
         */
        public String getWithTreeMap(String key, int timestamp) {
            TreeMap<Integer, String> history = treeMapStorage.get(key);
            if (history == null)
                return "";

            // floorEntry returns entry with greatest key <= timestamp
            Map.Entry<Integer, String> entry = history.floorEntry(timestamp);
            return entry == null ? "" : entry.getValue();
        }
    }
}
