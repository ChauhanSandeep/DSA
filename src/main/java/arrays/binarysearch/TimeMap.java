package arrays.binarysearch;

import java.util.*;

/**
 * Problem: Time Based Key-Value Store
 *
 * Design a key-value store with timestamped writes. A query returns the value written at the greatest timestamp less than or equal to the requested time.
 *
 * Leetcode: https://leetcode.com/problems/time-based-key-value-store/ (Medium)
 * Rating:   zerotrac 1575 (Q2, weekly-121)
 * Pattern:  Design | Per-key history | Binary search upper bound
 *
 * Example:
 *   Input:  set("foo","bar",1), get("foo",3)
 *   Output: "bar"
 *   Why:    timestamp 1 is the latest foo write at or before timestamp 3.
 *
 * Follow-ups:
 *   1. Out-of-order writes? Use a TreeMap per key and floorEntry.
 *   2. Delete by timestamp? Store tombstones in the history.
 *   3. Range query? Binary search both timestamp boundaries.
 *   4. Persistent store? Append writes to a log and index by key.
 *
 * Related: Snapshot Array (1146), Design Log Storage System (635).
 */
public class TimeMap {

    public static void main(String[] args) {
        TimeMap timeMap = new TimeMap();
        timeMap.set("foo", "bar", 1);
        String first = timeMap.get("foo", 1);
        System.out.printf("ops=[set(foo,bar,1), get(foo,1)] -> \"%s\"  expected=\"bar\"%n", first);
        String second = timeMap.get("foo", 3);
        System.out.printf("ops=[get(foo,3)] -> \"%s\"  expected=\"bar\"%n", second);
        timeMap.set("foo", "bar2", 4);
        String third = timeMap.get("foo", 4);
        System.out.printf("ops=[set(foo,bar2,4), get(foo,4)] -> \"%s\"  expected=\"bar2\"%n", third);
        String missing = timeMap.get("foo", 0);
        System.out.printf("ops=[get(foo,0)] -> \"%s\"  expected=\"\"%n", missing);
    }

    private Map<String, List<Pair>> map;
    private Map<String, TreeMap<Integer, String>> treeMapStorage;

    public TimeMap() {
        map = new HashMap<>();
        treeMapStorage = new HashMap<>();
    }

        /**
     * Intuition: The original problem inserts timestamps in increasing order for each key. Appending to that key's history preserves sorted order for later binary search.
     *
     * Algorithm:
     *   1. Create the history list if key is new.
     *   2. Append the timestamp/value pair.
     *
     * Time:  O(1) - one map access and append.
     * Space: O(1) - one new pair is stored.
     *
     * @param key lookup key
     * @param value value to store
     * @param timestamp write timestamp
     */
    public void set(String key, String value, int timestamp) {
        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<>());
        }
        map.get(key).add(new Pair(timestamp, value));
    }

        /**
     * Intuition: For one key, history is sorted by timestamp. The needed value is the rightmost timestamp <= query time, found by upper-bound binary search.
     *
     * Algorithm:
     *   1. Return empty string if key has no history.
     *   2. Binary search first timestamp greater than the query.
     *   3. Return empty if no timestamp is <= query.
     *   4. Otherwise return the value at left - 1.
     *
     * Time:  O(log n) - binary search over one key's history.
     * Space: O(1) - only indexes are stored.
     *
     * @param key lookup key
     * @param timestamp query timestamp
     * @return latest value at or before timestamp, or empty string
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
