package graph;

import java.util.*;

/**
 * Currency Conversion Using Graphs (Best Exchange Rate Finder)
 *
 * Problem:
 * Given a set of currency conversion rates, determine the best exchange rate
 * between two given currencies.
 *
 * Approach:
 * 1. **Graph Representation**: Use an adjacency list where each currency is a node, and exchange rates are weighted edges.
 * 2. **Bidirectional Edges**: Store both direct and inverse rates (e.g., USD->EUR and EUR->USD).
 * 3. **Dijkstra’s Algorithm (Priority Queue - Max Heap)**: Find the highest possible conversion rate from source to target.
 *
 * Time Complexity: **O(E + V log V)** (Dijkstra’s Algorithm)
 * Space Complexity: **O(V + E)** (Graph storage)
 */
public class CurrencyConversion {

    /**
     * Represents a currency conversion rate.
     */
    private static class Rate {
        String from, to;
        double rate;

        public Rate(String from, String to, double rate) {
            this.from = from;
            this.to = to;
            this.rate = rate;
        }
    }

    /**
     * Represents a node in the priority queue for Dijkstra’s algorithm.
     */
    private static class ExchangeRate implements Comparable<ExchangeRate> {
        String currency;
        double rate; // Best conversion rate found so far

        public ExchangeRate(String currency, double rate) {
            this.currency = currency;
            this.rate = rate;
        }

        @Override
        public int compareTo(ExchangeRate other) {
            return Double.compare(other.rate, this.rate); // Max heap (higher rate has priority)
        }
    }

    // <From Currency, <To Currency, Rate>>
    private final Map<String, Map<String, Double>> exchangeGraph = new HashMap<>();

    /**
     * Builds the graph with given currency exchange rates.
     *
     * @param rates List of currency exchange rates.
     */
    public void addCurrencies(List<Rate> rates) {
        for (Rate rate : rates) {
            exchangeGraph.putIfAbsent(rate.from, new HashMap<>());
            exchangeGraph.putIfAbsent(rate.to, new HashMap<>());

            // Store forward and reverse conversion rates
            exchangeGraph.get(rate.from).put(rate.to, rate.rate);
            exchangeGraph.get(rate.to).put(rate.from, 1.0 / rate.rate);
        }
    }

    /**
     * Finds the best conversion rate from source to target currency using Dijkstra’s algorithm.
     *
     * @param source Source currency.
     * @param target Target currency.
     * @return Maximum exchange rate, or -1.0 if conversion is not possible.
     */
    public double findBestConversionRate(String source, String target) {
        if (!exchangeGraph.containsKey(source) || !exchangeGraph.containsKey(target)) return -1.0;
        if (source.equals(target)) return 1.0; // Self conversion is always 1:1

        // Max Heap to prioritize the best exchange rate
        PriorityQueue<ExchangeRate> queue = new PriorityQueue<>();
        Map<String, Double> bestRates = new HashMap<>();
        Set<String> visited = new HashSet<>();

        queue.add(new ExchangeRate(source, 1.0)); // Start with base currency at 1.0 conversion rate
        // bestRate contains the best conversion rate for each currency from the source currency
        bestRates.put(source, 1.0);

        while (!queue.isEmpty()) {
            // SELECT
            ExchangeRate current = queue.poll();
            // MARK(*)
            if (visited.contains(current.currency)) continue;
            visited.add(current.currency);

            for (Map.Entry<String, Double> neighbor : exchangeGraph.get(current.currency).entrySet()) {
                // WORK
                String nextCurrency = neighbor.getKey();
                double newRate = current.rate * neighbor.getValue();

                // ADD(*)
                if (!bestRates.containsKey(nextCurrency) || newRate > bestRates.get(nextCurrency)) {
                    bestRates.put(nextCurrency, newRate);
                    queue.add(new ExchangeRate(nextCurrency, newRate));
                }
            }
        }

        return bestRates.getOrDefault(target, -1.0);
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

        System.out.println("USD to EUR: " + converter.findBestConversionRate("USD", "EUR")); // Expected: 0.85
        System.out.println("USD to GBP: " + converter.findBestConversionRate("USD", "GBP")); // Expected: 0.85 * 0.75
        System.out.println("INR to JPY: " + converter.findBestConversionRate("INR", "JPY")); // Expected: -1 (No path)
    }
}
