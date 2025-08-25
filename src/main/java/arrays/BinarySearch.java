package arrays;

/**
 * ✅ Problem: Binary Search
 *
 * Given a sorted array and a target value `k`, return the index of `k` if it exists, otherwise return -1.
 * This is one of the most fundamental divide-and-conquer algorithms.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/binary-search/
 *
 * 🧠 Example:
 * Input: arr = [1, 2, 3, 4, 5], k = 4
 * Output: 3
 *
 * 🔍 Follow-up Questions:
 * 1. Can you write it recursively? ✅ See {@link #binarySearchRecursive}
 * 2. How do you handle duplicates? ➤ First or last occurrence: use modified binary search.
 * 3. What if the array is rotated? ➤ Apply binary search on rotated sorted array (Leetcode 33).
 */
public class BinarySearch {

    public static void main(String[] args) {
        int[] sortedArray = {1, 2, 3, 4, 5};
        int target = 4;

        System.out.println("Iterative Binary Search Output: " + binarySearch(sortedArray, target));
        System.out.println("Recursive Binary Search Output: " + binarySearchRecursive(sortedArray, target));
    }

    /**
     * Performs an iterative binary search on a sorted array.
     *
     * Steps:
     * 1. Set left and right pointers to the ends of the array.
     * 2. Compute mid index.
     * 3. Compare mid element with target:
     *    a. If equal ➤ return mid.
     *    b. If target > mid ➤ move left to mid + 1.
     *    c. If target < mid ➤ move right to mid - 1.
     * 4. Repeat until left > right.
     *
     * Time Complexity: O(log N)
     * Space Complexity: O(1)
     *
     * @param arr Sorted input array
     * @param target Target value to search
     * @return Index of target if found, else -1
     */
    public static int binarySearch(int[] arr, int target) {
        if (arr == null || arr.length == 0) return -1;

        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1; // Move to right half
            } else {
                right = mid - 1; // Move to left half
            }
        }

        return -1; // Not found
    }

    /**
     * Performs recursive binary search.
     *
     * Time Complexity: O(log N)
     * Space Complexity: O(log N) due to recursive call stack
     */
    public static int binarySearchRecursive(int[] arr, int target) {
        return searchHelper(arr, target, 0, arr.length - 1);
    }

    /**
     * Recursive helper for binary search.
     *
     * @param arr Sorted input array
     * @param target Target to find
     * @param left Current left boundary
     * @param right Current right boundary
     * @return Index of target, or -1 if not found
     */
    private static int searchHelper(int[] arr, int target, int left, int right) {
        if (left > right) return -1;

        int mid = left + (right - left) / 2;

        if (arr[mid] == target) return mid;
        if (arr[mid] < target) return searchHelper(arr, target, mid + 1, right);
        return searchHelper(arr, target, left, mid - 1);
    }
}