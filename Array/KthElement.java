package Array;

public class KthElement {
    public static void main(String[] args) {
        int[] arr1 = {9, 11, 19, 26, 32, 34, 45, 50, 56, 58, 61, 88};
        int[] arr2 = {1, 5, 5, 7, 9, 9, 11, 13, 13, 15, 18, 19, 19, 20, 21, 28, 28, 28, 29, 29, 30, 31, 39, 40, 44, 47, 47, 50, 52, 56, 57, 61, 61, 61, 66, 68, 69, 70, 70, 74, 75, 75, 77, 78, 79, 80, 82, 85, 87, 89, 90, 90, 90, 92, 93, 95, 97, 98, 98, 100};
        int k = 64;
        System.out.println("The " + k + "th element is: " + kthElement(arr1, arr2, k));
    }

    public static int kthElement(int[] arr1, int[] arr2, int k) {
        if (arr1.length > arr2.length) {
            return kthElement(arr2, arr1, k); // Ensure binary search is on the smaller array
        }

        int n = arr1.length, m = arr2.length;
        int low = Math.max(0, k - m), high = Math.min(k, n);

        while (low <= high) {
            int cut1 = (low + high) / 2;
            int cut2 = k - cut1;

            int left1 = (cut1 == 0) ? Integer.MIN_VALUE : arr1[cut1 - 1];
            int left2 = (cut2 == 0) ? Integer.MIN_VALUE : arr2[cut2 - 1];
            int right1 = (cut1 == n) ? Integer.MAX_VALUE : arr1[cut1];
            int right2 = (cut2 == m) ? Integer.MAX_VALUE : arr2[cut2];

            if (left1 <= right2 && left2 <= right1) {
                return Math.max(left1, left2);
            } else if (left1 > right2) {
                high = cut1 - 1;
            } else {
                low = cut1 + 1;
            }
        }
        return -1;
    }
}
