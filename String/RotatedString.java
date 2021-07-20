package String;

/**
 * https://practice.geeksforgeeks.org/problems/check-if-string-is-rotated-by-two-places-1587115620/1
 * GIven two strings find if one string can be created by rotating second string 2 times.
 */
public class RotatedString {
    public static void main(String[] args) {
        System.out.println(isRotation("amazon", "azonam"));
        System.out.println(isRotation("geeksforgeeks", "geeksgeeksfor"));
    }

    private static boolean isRotation(String str1, String str2) {

        if(str1== null || str2 == null || str1.length() <=2 || str2.length() == 2) return false;

        return (str1.substring(2) + str1.substring(0, 2)).equals(str2)||
                (str1.substring(str1.length() - 2) + str1.substring(0, str1.length() - 2)).equals(str2);
    }
}
