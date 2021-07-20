package Array;

public class KthElement {
    public static void main(String[] args) {
        int[] arr1 = {9, 11, 19, 26, 32, 34, 45, 50, 56, 58, 61, 88};
        int[] arr2 = {1, 5, 5, 7, 9, 9, 11, 13, 13, 15, 18, 19, 19, 20, 21, 28, 28, 28, 29, 29, 30, 31, 39, 40, 44, 47, 47, 50, 52, 56, 57, 61, 61, 61, 66, 68, 69, 70, 70, 74, 75, 75, 77, 78, 79, 80, 82, 85, 87, 89, 90, 90, 90, 92, 93, 95, 97, 98, 98, 100};
        int k = 64;
        System.out.println(kthElement(arr1, arr2, k));
    }

    /*Find kth element in 2 sorted lists*/
    public static long kthElement( int arr1[], int arr2[], int k) {
        int i =0;
        int j=0;
        int count = 1;
        while(count < k && i<arr1.length && j < arr2.length) {
            if(arr1[i] < arr2[j]) {
                i++;
            }else{
                j++;
            }
            count++;
        }
        while(count < k && i<arr1.length) {
            i++;
            count++;
        }
        while(count < k && j<arr2.length) {
            j++;
            count++;
        }
         System.out.println(i + "   " + j);
        if(i<arr1.length && j<arr2.length) return Math.min(arr1[i], arr2[j]);
        if(i<arr1.length) return arr1[i];
        if(j<arr2.length) return arr2[j];

        return -1;

    }
}
