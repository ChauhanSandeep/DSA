package String;

/**
 * Time Palindrome Finder
 * 
 * Given a time in "HH:MM" format, determine how many minutes it will take 
 * until the time becomes a palindrome (i.e., "HH:MM" reads the same forward and backward).
 * 
 * **Approach & Explanation:**
 * - Convert input time into hours and minutes.
 * - Increment time minute by minute until a palindrome is found.
 * - Ensure time wraps correctly (i.e., 23:59 → 00:00).
 * 
 * **Time Complexity:** O(1) - At most 1440 iterations (24 hours).
 * **Space Complexity:** O(1) - Constant space usage.
 */
public class TimePalindrome {

    public static void main(String[] args) {
        String inputTime = "21:00";
        int minutesUntilPalindrome = findMinutesToPalindrome(inputTime);
        System.out.println(minutesUntilPalindrome);
    }

    /**
     * Finds the number of minutes required until the time becomes a palindrome.
     *
     * @param time The input time in "HH:MM" format.
     * @return The number of minutes required to reach the next palindrome time.
     */
    public static int findMinutesToPalindrome(String time) {
        String[] timeParts = time.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);

        int minutesElapsed = 0;

        // Keep incrementing the time until we find a palindrome
        while (!isPalindromeTime(hours, minutes)) {
            minutes++;
            if (minutes == 60) { // Wrap around for minutes
                minutes = 0;
                hours++;
                if (hours == 24) { // Wrap around for hours
                    hours = 0;
                }
            }
            minutesElapsed++;
        }
        return minutesElapsed;
    }

    /**
     * Checks if the given hour and minute form a palindrome when written in "HH:MM" format.
     *
     * @param hours   The hour value.
     * @param minutes The minute value.
     * @return True if the time is a palindrome, false otherwise.
     */
    private static boolean isPalindromeTime(int hours, int minutes) {
        String hourStr = String.format("%02d", hours);
        String minuteStr = String.format("%02d", minutes);
        return new StringBuilder(hourStr).reverse().toString().equals(minuteStr);
    }
}
