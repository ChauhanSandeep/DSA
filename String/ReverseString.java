package String;

public class ReverseString {
    public static void main(String[] args) {
        String str = "I like this code very much";
        String result = reverseString(str);
        System.out.println(result);
    }

    private static String reverseString(String str) {
        String[] strArr = str.split(" ");
        String result = "";
        for (String x: strArr) {
            result = x + " " + result;
        }
        return result.trim();
    }
}
