package String;

/**
 * Find in how many minutes, hour and minutes of time will become palindrome.
 */
public class TimePalindrome {

    public static void main(String[] args) {
        int res = new TimePalindrome().solve("21:00");
        System.out.println(res);
    }

    public int solve(String time) {
        String[] arr = time.split(":");
        int hour = Integer.parseInt(arr[0]);
        int min = Integer.parseInt(arr[1]);

        int diff = 0;
        while(!isPalindrome(hour, min)) {
            if(min == 59) {
                hour++;
                min = 0;
            }else{
                min++;
            }
            if(hour == 24) hour = 0;
            diff++;
        }
        return diff;

    }

    private boolean isPalindrome(int first, int second) {
        String str1 = String.valueOf(first);
        if(str1.length() == 1) str1 = "0" + str1;

        String str2 = String.valueOf(second);
        if(str2.length() == 1) str2 = "0" + str2;

        return  new StringBuilder(str1).reverse().toString().equals(str2);
    }
}
