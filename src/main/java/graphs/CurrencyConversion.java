package graphs;

import java.util.*;

/**
 * Problem: Best Currency Conversion Rate
 *
 * Given direct exchange rates between pairs of currencies, find the best rate
 * obtainable from one currency to another by chaining conversions. A direct rate
 * also implies the reverse conversion with reciprocal value.
 *
 * Pattern:  Graph | Maximum-product path | Priority queue search
 *
 * Example:
 *   Input:  USD/EUR=0.85, EUR/GBP=0.75, USD/INR=74.0; query USD to GBP
 *   Output: 0.6375
 *   Why:    USD can convert to EUR and then GBP, so the chained rate is
 *           0.85 * 0.75 = 0.6375.
 *
 * Follow-ups:
 *   1. Detect arbitrage cycles?
 *      Take negative logs of rates and run Bellman-Ford to find negative cycles.
 *   2. Answer many repeated queries quickly?
 *      Precompute all-pairs best products per connected component when updates are rare.
 *   3. Support live rate updates?
 *      Re-run shortest-path search from affected sources or keep a dynamic graph index.
 */
public class CurrencyConversion {


    public static void main(String[] args) {
        CurrencyConversion converter = new CurrencyConversion();
        converter.addCurrencies(Arrays.asList(
            new Rate("USD", "EUR", 0.85),
            new Rate("EUR", "GBP", 0.75),
            new Rate("USD", "INR", 74.0),
            new Rate("GBP", "JPY", 150.0)
        ));
        String[][] queries = {{"USD", "GBP"}, {"INR", "JPY"}, {"USD", "USD"}};
        double[] expected = {0.6375, 1.2922297297297298, 1.0};

        for (int i = 0; i < queries.length; i++) {
            double output = converter.findBestConversionRate(queries[i][0], queries[i][1]);
            System.out.printf("%s->%s  ->  %.10f  expected=%.10f%n",
                queries[i][0], queries[i][1], output, expected[i]);
        }
    }
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
     * Intuition: each currency is a node and each exchange rate is a weighted edge.
     * The best conversion is the path with the largest product of rates, which is
     * Dijkstra-like when the priority queue always expands the currently best known
     * product first.
     *
     * Algorithm:
     *   1. Return 1.0 when source and target are the same currency.
     *   2. Push the source with conversion rate 1.0 into the max-priority queue.
     *   3. Pop the best current currency and stop when it is the target.
     *   4. Relax outgoing exchange rates by multiplying the current rate by each edge rate.
     *
     * Time:  O((V+E) log V) - priority-queue operations dominate the graph traversal.
     * Space: O(V+E) - adjacency lists plus best-rate and visited tracking.
     *
     * @param source currency to convert from
     * @param target currency to convert to
     * @return best conversion rate, or -1.0 when target is unreachable
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

}
