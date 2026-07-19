package strings.twopointers;

/**
 * Problem: Next Mirrored Time
 *
 * Given a time in HH:MM format, return the next time later on the same day where
 * the minute equals the reverse of the hour. If no such time exists before the
 * day ends, this implementation returns "-1".
 *
 * Source: https://www.geeksforgeeks.org/dsa/given-a-number-find-next-smallest-palindrome-larger-than-this-number/
 * Pattern: Strings | Time parsing | Digit reversal
 *
 * Example:
 *   Input:  time = "05:49"
 *   Output: "05:50"
 *   Why:    reversing hour 05 gives minute 50, which is later than 05:49.
 *
 * Follow-ups:
 *   1. How would you wrap to the next day?
 *      Continue from 00:00 after 23:59 instead of returning "-1".
 *   2. How is this different from a full HH:MM palindrome?
 *      It only requires MM to be reverse(HH), not a character palindrome including ':'.
 *   3. How would you list all mirrored times in a day?
 *      Check each hour, reverse its digits, and keep minutes below 60.
 */

public class TimePalindrome {

  public static void main(String[] args) {
    String[] inputs = {"21:12", "23:59", "05:49"};
    String[] expected = {"22:22", "-1", "05:50"};

    for (int i = 0; i < inputs.length; i++) {
      String got = getNextMirroredTime(inputs[i]);
      System.out.printf("time=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
    }
  }


    /**
   * Intuition: for any hour, the only mirrored minute is the hour's digit
   * reversal. Check the current hour first, then later hours, and accept the
   * first reversed minute that is valid and later than the input time.
   *
   * Algorithm:
   *   1. Parse hour and minute from HH:MM.
   *   2. Reverse the hour digits to form the candidate minute.
   *   3. Return the candidate when it is below 60 and later than the input.
   *   4. Otherwise increment the hour until the same-day range is exhausted.
   *
   * Time:  O(1) - at most 24 hours are checked.
   * Space: O(1) - only parsed integers are stored.
   *
   * @param time Current time in HH:MM format.
   * @return Next same-day mirrored time, or "-1" if none exists.
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

    /** Reverses the two decimal digits of an hour value. */
  private static int reverseDigits(int num) {
    int tens = num / 10;
    int units = num % 10;
    return units * 10 + tens;
  }
}
