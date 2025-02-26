package BinarySearch;

import java.util.TreeMap;

/**
 * This class represents a Snapshot Array which supports taking snapshots and retrieving
 * values at a given index and snapshot ID.
 * 
 * Algorithm:
 * - Use a TreeMap to store values at each index with their corresponding snapshot IDs.
 * - Time Complexity: O(log n) for set and get operations.
 * - Space Complexity: O(n) where n is the number of set operations.
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/snapshot-array/
 */
public class SnapshotArray {

    private int currentSnapId;
    private TreeMap<Integer, Integer>[] snapshotArray;

    public static void main(String[] args) {
        SnapshotArray snapshotArray = new SnapshotArray(1);
        snapshotArray.set(0, 15);
        snapshotArray.snap();
        snapshotArray.snap();
        snapshotArray.snap();
        System.out.println(snapshotArray.get(0, 2));
    }

    public SnapshotArray(int length) {
        this.currentSnapId = 0;
        this.snapshotArray = new TreeMap[length];
        for (int i = 0; i < length; i++) {
            snapshotArray[i] = new TreeMap<>();
        }
    }

    public void set(int index, int value) {
        snapshotArray[index].put(currentSnapId, value);
    }

    public int snap() {
        currentSnapId++;
        return currentSnapId - 1;
    }

    public int get(int index, int snapId) {
        TreeMap<Integer, Integer> snapshots = snapshotArray[index];
        if (snapshots == null) {
            return 0;
        }
        Integer closestSnapId = snapshots.floorKey(snapId);
        return closestSnapId == null ? 0 : snapshots.get(closestSnapId);
    }
}
