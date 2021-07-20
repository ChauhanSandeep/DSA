package Array;

public class BinarySearch {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        int k = 4;
        System.out.println(binarysearch(arr, k));
    }


    /*Find element using binary search*/
    static int binarysearch(int arr[], int k){
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = (left+right)/2;

            if (arr[mid] == k) return mid;

            if (arr[mid] < k) left = mid + 1;

            else right = mid - 1;
        }
        return -1;
    }


}
