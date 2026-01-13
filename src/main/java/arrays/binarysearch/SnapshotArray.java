package arrays.binarysearch;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * Problem Statement:
 * Implement a SnapshotArray that supports the following interface:
 * - SnapshotArray(int length): Initializes an array-like data structure with the given length. Initially, each element equals 0.
 * - void set(index, val): Sets the element at the given index to be equal to val.
 * - int snap(): Takes a snapshot of the array and returns the snap_id (the snapshot ID, starting from 0).
 * - int get(index, snap_id): Returns the value at the given index at the time we took the snapshot with the given snap_id.
 *
 * Example:
 * Input: ["SnapshotArray","set","snap","set","get"]
 *        [[3],[0,5],[],[0,6],[0,0]]
 * Output: [null,null,0,null,5]
 * Explanation:
 * SnapshotArray snapshotArr = new SnapshotArray(3);
 * snapshotArr.set(0,5);  // Set array[0] = 5
 * snapshotArr.snap();    // Take snapshot, return snap_id = 0
 * snapshotArr.set(0,6);  // Set array[0] = 6
 * snapshotArr.get(0,0);  // Get value at index 0 with snap_id 0, returns 5
 *
 * LeetCode link: https://leetcode.com/problems/snapshot-array/
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 *  - What if we need to support rollback to a previous snapshot?
 *    → Maintain reference to old snapshots and restore state from them.
 *  - How would you optimize for arrays where most values don't change between snapshots?
 *    → Current solution already handles this - only stores changes, not entire array.
 *  - Can you support range queries (get values for index range in a snapshot)?
 *    → Extend to store range data structures like segment trees per snapshot.
 *  - What if snapshots need to be persisted to disk?
 *    → Serialize TreeMaps to disk, use lazy loading for get operations.
 */
public class SnapshotArray {
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

    public SnapshotArray(int length) {
        snapshotValues = new HashMap<>();
        snapId = 0;
    }

    /**
     * Sets value at index for current snapshot.
     * Time: O(log m) where m is number of snapshots with changes at this index.
     */
    public void set(int index, int val) {
        // Initialize TreeMap for this index if not exists
        snapshotValues.putIfAbsent(index, new TreeMap<>());
        
        // Store value with current snapId
        snapshotValues.get(index).put(snapId, val);
    }

    /**
     * Takes snapshot and returns snapshot ID.
     * Time: O(1), just increments counter.
     */
    public int snap() {
        return snapId++;
    }

    /**
     * Gets value at index for given snapshot ID.
     * Time: O(log m) where m is number of snapshots with changes at this index.
     */
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