package binarysearch;

import java.util.ArrayList;
import java.util.List;


/**
 * SnapshotArray is a data structure that mimics versioned arrays.
 * It allows setting values, taking snapshots of the array, and retrieving values
 * from a specific snapshot efficiently.
 *
 * -------------------------------------
 * 🔍 Intuition:
 * -------------------------------------
 * Instead of storing the full array for every snapshot (which is memory inefficient),
 * we only store the changes made to each index along with the snapshot ID at which
 * that change occurred.
 *
 * For example:
 * - If you set index 5 to 10 in snapshot 3, we store (3, 10) in the list for index 5.
 * - When retrieving a value from a specific snapshot, we find the most recent change
 *   before or equal to that snapshot using binary search.
 *
 * -------------------------------------
 * 🧠 Approach:
 * -------------------------------------
 * - Maintain an array of lists (changeHistory), one for each index in the array.
 * - Each list contains pairs: [snapshotId, value]
 * For example:
 *  - changeHistory[0] = [[0, 0], [1, 5], [2, 10]] means:
 *  - At snapshot 0, index 0 had value 0.
 *  - At snapshot 1, index 0 was set to 5.
 *  - At snapshot 2, index 0 was set to 10.
 *
 * - When setting a value:
 *      → If the latest entry is for the current snapshot, update it.
 *      → Else, append a new pair.
 * - When taking a snapshot:
 *      → Increment the global snapshot counter and return the previous ID.
 * - When getting a value for a specific snapshot:
 *      → Perform a binary search to find the latest value ≤ given snapshotId.
 *
 * ⏱️ Time Complexity:
 * - set(index, val): O(1) amortized
 * - snap(): O(1)
 * - get(index, snapId): O(log m), where m = number of updates at that index
 *
 * 📦 Space Complexity:
 * - O(total set operations), as we only store actual changes
 *
 * This approach is clean, efficient, and interview-friendly.
 *
 * LeetCode Problem: https://leetcode.com/problems/snapshot-array/
 */
public class SnapshotArray {

  // Represents the snapshot ID counter (starts from 0)
  private int currentSnapshotId;

  // Stores change history for each index:
  // Each list contains pairs of [snapshotId, value]
  private List<int[]>[] changeHistory; // changeHistory[index] = List of values where index of value is the snapshotId

  // Constructor: Initialize the change history for each index
  public SnapshotArray(int length) {
    this.currentSnapshotId = 0;
    changeHistory = new ArrayList[length];

    for (int i = 0; i < length; i++) {
      changeHistory[i] = new ArrayList<>();
      // Initially, every index has value 0 at snapshot 0
      changeHistory[i].add(new int[]{0, 0});
    }
  }

  // Set the value at a specific index for the current snapshot version
  public void set(int index, int value) {
    List<int[]> historyAtIndex = changeHistory[index];
    int[] lastChange = historyAtIndex.get(historyAtIndex.size() - 1);

    // If the latest change is for the current snapshot, update it
    if (lastChange[0] == currentSnapshotId) {
      lastChange[1] = value;
    } else {
      // Otherwise, add a new record for current snapshotId
      historyAtIndex.add(new int[]{currentSnapshotId, value});
    }
  }

  // Get the value at index from the snapshot with given snapshotId
  public int get(int index, int targetSnapshotId) {
    List<int[]> historyAtIndex = changeHistory[index];
    // Binary search for the closest snapshotId which is less than or equal to targetSnapshotId
    return findClosestSnapshotValue(targetSnapshotId, historyAtIndex);
  }

  // Take a snapshot and return its ID
  public int snap() {
    return currentSnapshotId++;
  }

  /**
   * Finds the closest snapshot value for a given target snapshot ID using binary search.
   * This method assumes that the historyAtIndex is sorted by snapshotId.
   */
  private static int findClosestSnapshotValue(int targetSnapshotId, List<int[]> historyAtIndex) {
    int result = 0; // Default value if no valid snapshot found
    int low = 0, high = historyAtIndex.size() - 1;
    while (low <= high) {
      int mid = (low + high) / 2;
      int[] entry = historyAtIndex.get(mid);

      if (entry[0] <= targetSnapshotId) {
        result = entry[1];  // potential answer
        low = mid + 1;
      } else {
        high = mid - 1;
      }
    }
    return result;
  }

  // For quick testing
  public static void main(String[] args) {
    SnapshotArray array = new SnapshotArray(1);
    array.set(0, 15);
    array.snap();
    array.snap();
    array.snap();
    System.out.println(array.get(0, 2)); // Output should be 15
  }
}