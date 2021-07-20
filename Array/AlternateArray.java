package Array;

import java.util.Arrays;

public class AlternateArray {

    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6};
        new AlternateArray().rearrange(arr, 6);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * Given an array, modify it such way that first element is largest, second is smallest,
     * third is second largest, fourth is second smallest and so on.
     */
    public void rearrange(int[] arr, int n){

        int[] result = new int[n];

        int first = 0;
        int last = n-1;
        int i=0;
        while(first <= last) {
            if(i%2==1) {
                result[i++] = arr[first++];
            }else{
                result[i++] = arr[last--];
            }
        }
        for(int j=0; j<result.length;j++) {
            arr[j] = result[j];
        }
    }
}
