package Array;

import java.util.Arrays;

public class SentenceReverse {

    public static void main(String[] args) {
        char[] arr = { 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        reverseWords(arr);
        System.out.println(Arrays.toString(arr)); // Expected: ['w', 'o', 'r', 'l', 'd', ' ', 'h', 'e', 'l', 'l', 'o']
    }

    static char[] reverseWords(char[] arr) {
        if (arr == null || arr.length == 0) return arr;

        // Step 1: Reverse entire sentence
        reverse(arr, 0, arr.length - 1);

        // Step 2: Reverse each word in-place
        int wordStart = 0;
        for (int i = 0; i <= arr.length; i++) {
            if (i == arr.length || arr[i] == ' ') {  // End of word
                reverse(arr, wordStart, i - 1);
                wordStart = i + 1;
            }
        }

        return arr;
    }

    private static void reverse(char[] arr, int start, int end) {
        while (start < end) {
            char temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }
}
