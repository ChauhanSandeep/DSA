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
        String data = Stream.of("null", "data").filter(e -> e != null).findFirst().orElse(null);
        System.out.println(data);
    }
}
