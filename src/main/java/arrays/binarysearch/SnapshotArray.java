package arrays.binarysearch;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * Problem: Snapshot Array
 *
 * Design an array-like structure supporting set, snap, and get. Each snapshot freezes logical values so later get calls can retrieve an index's historical value.
 *
 * Leetcode: https://leetcode.com/problems/snapshot-array/ (Medium)
 * Rating:   zerotrac 1771 (Q3, weekly-148)
 * Pattern:  Design | Sparse histories | TreeMap floor lookup
 *
 * Example:
 *   Input:  set(0,5), snap(), set(0,6), get(0,0)
 *   Output: 5
 *   Why:    snapshot 0 was taken before index 0 changed to 6.
 *
 * Follow-ups:
 *   1. Skip duplicate writes? Check the current snap entry before inserting.
 *   2. Rollback? Reset active state from stored histories.
 *   3. Range queries? Use persistent segment trees or range-indexed histories.
 *   4. Persist to disk? Store append-only logs and lazy indexes.
 *
 * Related: Time Based Key-Value Store (981).
 */
public class SnapshotArray {

    public static void main(String[] args) {
        SnapshotArray snapshotArray = new SnapshotArray(3);
        snapshotArray.set(0, 5);
        int firstSnap = snapshotArray.snap();
        snapshotArray.set(0, 6);
        int valueAtFirstSnap = snapshotArray.get(0, firstSnap);
        System.out.printf("ops=[set(0,5), snap(), set(0,6), get(0,%d)] -> %d  expected=5%n", firstSnap, valueAtFirstSnap);
        SnapshotArray edgeCase = new SnapshotArray(1);
        int defaultSnap = edgeCase.snap();
        int defaultValue = edgeCase.get(0, defaultSnap);
        System.out.printf("ops=[snap(), get(0,%d)] -> %d  expected=0%n", defaultSnap, defaultValue);
    }

    /**
     * Main solution: HashMap + TreeMap for efficient snapshot storage.
     * 
     * Data Structure:
     *  - array: Map<Integer, TreeMap<Integer, Integer>>
     *    - Outer map: index → TreeMap of changes
     *    - Inner TreeMap: snap_id → value at that snapshot
     *  - snapId: Current snapshot ID counter
     *
     * Key Insight:
     * Instead of copying entire array for each snapshot (O(n) space per snap),
     * only store changes per index. TreeMap maintains sorted order of snap_ids,
     * enabling binary search to find most recent value ≤ requested snap_id.
     *
     * Operations:
     *  - set(index, val): Store value with current snapId, O(log m) where m is changes at index
     *  - snap(): Increment snapId counter, O(1)
     *  - get(index, snap_id): Binary search for largest snap_id ≤ requested, O(log m)
     *
     * Algorithm: TreeMap with binary search via floorEntry.
     * Space Complexity: O(total_set_calls), only stores actual changes.
     */
    
    private Map<Integer, TreeMap<Integer, Integer>> snapshotValues;
    private int snapId;

    /** Initializes empty per-index histories and starts snapshot ids at 0. */
    public SnapshotArray(int length) {
        snapshotValues = new HashMap<>();
        snapId = 0;
    }

        /** Records val for index in the current snapshot id. */
    public void set(int index, int val) {
        // Initialize TreeMap for this index if not exists
        snapshotValues.putIfAbsent(index, new TreeMap<>());
        
        // Store value with current snapId
        snapshotValues.get(index).put(snapId, val);
    }

        /** Returns the current snapshot id, then advances to the next one. */
    public int snap() {
        return snapId++;
    }

        /** Returns the latest value for index whose stored snapshot id is <= snap_id. */
    public int get(int index, int snap_id) {
        // If no changes at this index, return default 0
        if (!snapshotValues.containsKey(index)) {
            return 0;
        }
        
        TreeMap<Integer, Integer> snapshots = snapshotValues.get(index);
        
        // Find largest snap_id which is less than or equal to requested snap_id
        // floorEntry returns entry with largest key ≤ given key
        Map.Entry<Integer, Integer> entry = snapshots.floorEntry(snap_id);
        
        // If no entry found, means no changes before this snapshot, return 0
        return entry == null ? 0 : entry.getValue();
    }

}