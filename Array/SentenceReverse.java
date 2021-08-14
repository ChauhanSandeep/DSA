package Array;

import java.util.Arrays;

public class SentenceReverse {

    public static void main(String[] args) {
        //{'a', ' ', 'b'} => {'b', ' ', 'a'}
        char[] arr = { 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        reverseWords(arr);
        System.out.println(Arrays.toString(arr));
    }

    static char[] reverseWords(char[] arr) {
        // your code goes here
        if(arr == null) return null;
        if(arr.length == 0) return arr;

        reverse(arr, 0, arr.length-1);

        int startIndex = 0;
        int endIndex = 0;
        while(endIndex < arr.length) {
            if(arr[endIndex] == ' ') {
                reverse(arr, startIndex, endIndex-1);
                startIndex = endIndex+1;
            }else if(endIndex == arr.length - 1) {
                reverse(arr, startIndex, endIndex);
            }
            endIndex++;
        }
        return arr;
    }

    private static void reverse(char[] arr, int start, int end) {
        while(start < end) {
            char temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }
}
