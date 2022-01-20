package BinarySearch;

import java.util.TreeMap;

/**
 * https://leetcode.com/problems/snapshot-array/
 */
public class SnapshotArray {

    public static void main(String[] args) {
        SnapshotArray snapshotArray = new SnapshotArray(1);
        snapshotArray.set(0, 15);
        snapshotArray.snap();
        snapshotArray.snap();
        snapshotArray.snap();
        System.out.println(snapshotArray.get(0, 2));
    }

    int snapId = 0;
    TreeMap<Integer, Integer>[] arr; // <index, <snapId, value>>

    public SnapshotArray(int length) {
        this.snapId = 0;
        this.arr = new TreeMap[length];
        for(int i=0; i<arr.length; i++) {
            arr[i] = new TreeMap<>();
        }
    }

    public void set(int index, int val) {
        TreeMap<Integer, Integer> map = arr[index];
        map.put(this.snapId, val);
    }

    public int snap() {
        this.snapId++;
        return this.snapId - 1;
    }

    public int get(int index, int snap_id) {
        System.out.println(index + " " + snap_id);
        TreeMap<Integer, Integer> map = arr[index];
        if(map == null) return 0;
        if(null != map.floorKey(snap_id)) {
            System.out.println(map.floorKey(snap_id));
            return map.get(map.floorKey(snap_id));
        }
        return 0;
    }
}
