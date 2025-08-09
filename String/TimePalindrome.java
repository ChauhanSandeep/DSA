package String;

import java.util.*;


/**
 * Next Palindromic Time Generator (Mathematical Reverse-Based Approach)
 *
 * Problem Statement:
 * Given a time in 24-hour "HH:MM" format, return the next time (same day or next)
 * such that the hour and minute mirror each other. In other words, MM == reverse(HH).
 *
 * This is slightly different from generic palindrome (e.g., 12:21), instead it uses the
 * property: reverse(HH) == MM.
 *
 * Example:
 * Input: "21:12"
 * Output: "22:00"
 * Explanation: Reverse of 22 is 22, which gives 22:22, but it exceeds MM range (59), so 22:00 is chosen.
 *
 * GeeksforGeeks Link:
 * https://www.geeksforgeeks.org/dsa/given-a-number-find-next-smallest-palindrome-larger-than-this-number/
 *
 * Follow-Up Questions:
 * 1. How is this different from full palindromic time check?
 *    - This only ensures `reverse(HH) == MM`, not full "HH:MM" string being a palindrome.
 * 2. Can this be used to generate mirrored display times?
 *    - Yes, this is useful in digital clocks with symmetry logic.
 */

public class TimePalindrome {

  public static void main(String[] args) {
    String currentTime = "21:12";
    String nextMirrorTime = getNextMirroredTime(currentTime);
    System.out.println(nextMirrorTime);
  }

  /**
   * Returns the next valid mirrored time where MM = reverse(HH).
   *
   * Steps:
   * 1. Extract hour and minute from input.
   * 2. Check if reverse(hour) is greater than current minute → same hour is valid.
   * 3. Else, increment hour and compute reverse of new hour.
   * 4. Return HH:MM if mirror is valid; else continue to next valid hour.
   *
   * Time Complexity: O(1) – maximum 24 checks (bounded by 24 hours)
   * Space Complexity: O(1)
   *
   * @param time Current time in "HH:MM" format
   * @return Next mirror time as "HH:MM" string or "-1" if no such time exists today
   */
  public static String getNextMirroredTime(String time) {
    int hour = Integer.parseInt(time.substring(0, 2));
    int minute = Integer.parseInt(time.substring(3, 5));

    while (hour < 24) {
      int reversedHour = reverseDigits(hour);
      // if reversedHour is valid minute (0-59) and either:
      //     1. reversedHour < 60 and current hour is less than or equal to input hour
      //     2. reversedHour < 60 and current minute is less than reversedHour
      if (reversedHour < 60 && (hour > Integer.parseInt(time.substring(0, 2)) || minute < reversedHour)) {
        return String.format("%02d:%02d", hour, reversedHour);
      }
      hour++;
    }

    return "-1"; // No mirrored time available today
  }

  /**
   * Utility to reverse a 2-digit number.
   * For example, 12 → 21, 08 → 80
   */
  private static int reverseDigits(int num) {
    int tens = num / 10;
    int units = num % 10;
    return units * 10 + tens;
  }
}
