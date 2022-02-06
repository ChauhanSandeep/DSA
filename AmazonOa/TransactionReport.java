package AmazonOa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionReport {

    public static void main(String[] args) {
        List<String> transactions = Stream.of("notebook", "notebook", "mouse", "keyboard", "mouse").collect(Collectors.toList());
        List<String> report = new TransactionReport().groupTransactions(transactions);
        System.out.println(report);
    }

    public List<String> groupTransactions(List<String> transactions) {
        Map<String, Integer> freqMap = new HashMap<>();

        for(String t: transactions) {
            freqMap.put(t, freqMap.getOrDefault(t, 0) + 1);
        }
        List<String> items = new ArrayList<>(freqMap.keySet());
        Collections.sort(items, (a, b) -> {
            if(freqMap.get(a).intValue() == freqMap.get(b).intValue()) {
                return a.compareTo(b);
            }
            return freqMap.get(b) - freqMap.get(a);
        });

        for (int i=0; i<items.size(); i++) {
            items.set(i, items.get(i) + " " + freqMap.get(items.get(i)));
        }
        return items;
    }
}
