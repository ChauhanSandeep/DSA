package Graph;

import java.util.*;

public class CurrencyConversion {

    private static class Rate {
        String fromCurrency, toCurrency;
        double amount;

        public Rate(String fromCurrency, String toCurrency, double amount) {
            this.fromCurrency = fromCurrency;
            this.toCurrency = toCurrency;
            this.amount = amount;
        }
    }

    private static class Pair implements Comparable<Pair> {
        String currency;
        double cost;

        public Pair(String currency, double cost) {
            this.currency = currency;
            this.cost = cost;
        }

        @Override
        public int compareTo(Pair o) {
            return Double.compare(o.cost, this.cost); // Max heap for best conversion rate
        }
    }

    private final Map<String, Map<String, Double>> graph = new HashMap<>();

    /**
     * Add all currency conversion rates to the graph.
     */
    public void addCurrencies(List<Rate> rates) {
        for (Rate rate : rates) {
            graph.putIfAbsent(rate.fromCurrency, new HashMap<>());
            graph.putIfAbsent(rate.toCurrency, new HashMap<>());

            // Store forward and reverse conversion
            graph.get(rate.fromCurrency).put(rate.toCurrency, rate.amount);
            graph.get(rate.toCurrency).put(rate.fromCurrency, 1.0 / rate.amount);
        }
    }

    /**
     * Find the best conversion rate from source to target using Dijkstra's Algorithm.
     */
    public double findConversionRate(String source, String target) {
        if (!graph.containsKey(source) || !graph.containsKey(target)) return -1;
        if (source.equals(target)) return 1.0;

        PriorityQueue<Pair> queue = new PriorityQueue<>();
        Map<String, Double> maxRates = new HashMap<>();
        Set<String> visited = new HashSet<>();

        queue.add(new Pair(source, 1.0));
        maxRates.put(source, 1.0);

        while (!queue.isEmpty()) {
            Pair curr = queue.poll();
            if (visited.contains(curr.currency)) continue;
            visited.add(curr.currency);

            for (Map.Entry<String, Double> neighbor : graph.get(curr.currency).entrySet()) {
                String nextCurrency = neighbor.getKey();
                double newRate = curr.cost * neighbor.getValue();

                if (!maxRates.containsKey(nextCurrency) || newRate > maxRates.get(nextCurrency)) {
                    maxRates.put(nextCurrency, newRate);
                    queue.add(new Pair(nextCurrency, newRate));
                }
            }
        }

        return maxRates.getOrDefault(target, -1.0);
    }

    public static void main(String[] args) {
        CurrencyConversion converter = new CurrencyConversion();
        List<Rate> rates = Arrays.asList(
            new Rate("USD", "EUR", 0.85),
            new Rate("EUR", "GBP", 0.75),
            new Rate("USD", "INR", 74.0),
            new Rate("GBP", "JPY", 150.0)
        );

        converter.addCurrencies(rates);

        System.out.println("USD to EUR: " + converter.findConversionRate("USD", "EUR"));
        System.out.println("USD to GBP: " + converter.findConversionRate("USD", "GBP"));
        System.out.println("INR to JPY: " + converter.findConversionRate("INR", "JPY"));
    }
}
