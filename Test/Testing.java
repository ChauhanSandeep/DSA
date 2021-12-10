package Test;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Testing {
    public static void main(String[] args) {
        test();
    }

    public static void test() {
        int a = 2;
        String str = "0.123456";
        str = str.substring(0, a) + "(" + str.substring(a);
        System.out.println(str);
    }
}
