package Array;

/**
 * Implementation of the Binary Search algorithm.
 *
 * Binary Search is an efficient algorithm for finding an element in a sorted array.
 * The approach follows a divide-and-conquer strategy by repeatedly dividing the search interval in half.
 *
 * Time Complexity: O(log N), where N is the number of elements in the array.
 * Space Complexity: O(1), as the search is performed iteratively without additional space.
 */
public class BinarySearch {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        int k = 4;
        System.out.println(binarySearch(arr, k));
    }

    /**
     * Performs Binary Search to find the index of a given key in a sorted array.
     *
     * @param arr The sorted input array.
     * @param k The element to search for.
     * @return The index of the element if found, otherwise -1.
     */
    static int binarySearch(int arr[], int k) {
        int left = 0, right = arr.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2; // Prevents integer overflow

            if (arr[mid] == k) {
                return mid;
            } else if (arr[mid] < k) {
                left = mid + 1; // Search right half
            } else {
                right = mid - 1; // Search left half
            }
        }
        return -1; // Element not found
    }
}
