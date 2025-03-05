package Array;

import java.util.Arrays;

public class UniqueFreqDelete {
    public static void main(String[] args) {
        String str = "aabbcc";
        int result = minDeletions(str);
        System.out.println("Minimum deletions: " + result);
    }

    public static int minDeletions(String str) {
        int[] freq = new int[26];
        
        // Count frequency of each character
        for (char c : str.toCharArray()) {
            freq[c - 'a']++;
        }

        // Sort frequencies in descending order
        Arrays.sort(freq);
        
        int deletions = 0;
        int maxAllowed = str.length(); // Start with max possible frequency

        // Iterate from the highest frequency to the lowest
        for (int i = 25; i >= 0 && freq[i] > 0; i--) {
            if (freq[i] > maxAllowed) {
                deletions += freq[i] - maxAllowed;
                freq[i] = maxAllowed; // Reduce frequency to be unique
            }
            maxAllowed = Math.max(0, freq[i] - 1); // Next max frequency must be smaller
        }
        
        return deletions;
    }
}
