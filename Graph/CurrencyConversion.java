package Graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class CurrencyConversion {

    private class Rate {
        String fromCurrency;
        String toCurrency;
        double amount;

        public Rate(String fromCurrency, String toCurrency, double amount) {
            this.fromCurrency = fromCurrency;
            this.toCurrency = toCurrency;
            this.amount = amount;
        }
    }

    private class Pair implements Comparable<Pair> {
        String currency;
        String parent;
        double cost;

        public Pair(String parent, String currency, double cost) {
            this.currency = currency;
            this.parent = parent;
            this.cost = cost;
        }

        @Override
        public int compareTo(Pair o) {
            if (this.cost == o.cost) {
                return 1;
            } else {
                return Double.compare(this.cost, o.cost);
            }
        }
    }

    HashMap<String, HashMap<String, Double>> graph = new HashMap<>();

    /**
     * add all the currency conversion rate
     * @param rates
     */
    private void addCurrencies(List<Rate> rates) {

        for (Rate rate : rates) {
            if (!graph.containsKey(rate.fromCurrency)) {
                graph.put(rate.fromCurrency, new HashMap<>());
            }

            graph.get(rate.fromCurrency).put(rate.fromCurrency, 1.0);

            graph.get(rate.fromCurrency).put(rate.toCurrency, rate.amount);
        }

        for (Rate rate : rates) {
            for (Map.Entry<String, Double> val : graph.get(rate.fromCurrency).entrySet()) {
                if (!graph.containsKey(val.getKey())) {
                    graph.put(val.getKey(), new HashMap<>());
                }

                graph.get(val.getKey()).put(val.getKey(), 1.0);

                if (!graph.get(val.getKey()).containsKey(rate.fromCurrency)) {
                    graph.get(val.getKey()).put(rate.fromCurrency, 1.0 / val.getValue());
                }
            }
        }
    }

    /**
     * Find conversion rate source currency to target currency
     * @param source
     * @param target
     * @return
     */
    public double findConversionRate(String source, String target) {
        if (!graph.containsKey(source)) {
            return -1;
        }

        Queue<Pair> queue = new PriorityQueue<>();
        queue.add(new Pair(null, source, 1.0));

        Map<String, Double> dp = new HashMap<>();
        Map<String, String> parentMap = new HashMap<>();
        parentMap.put(source, source);

        while (!queue.isEmpty()) {
            Pair currPair = queue.poll();
            String top = currPair.currency;

            if (dp.containsKey(top) && dp.get(top) < currPair.cost) {
                continue;
            }

            dp.put(top, currPair.cost);
            parentMap.put(top, currPair.parent);
            graph.get(source).put(top, currPair.cost);

            for (Map.Entry<String, Double> dest : graph.get(top).entrySet()) {
                double rate = graph.get(source).get(top) * dest.getValue();

                if (dp.containsKey(dest.getKey()) && dp.get(dest.getKey()) >= rate || dest.getKey().equals(source)) {
                    continue;
                }
                queue.add(new Pair(top, dest.getKey(), rate));
            }
        }


        if (!graph.get(source).containsKey(target)) {
            return -1;
        }

        System.out.println(getPath(parentMap, source, target));
        return graph.get(source).get(target);
    }

    private String getPath(Map<String, String> parent, String from, String to) {
        String current = to;
        System.out.println(current);
        Stack<String> res = new Stack<>();
        res.add(to);
        while (!parent.get(current).equals(from)) {
            current = parent.get(current);
            res.add(current);
        }

        StringBuilder stringBuilder = new StringBuilder();
        res.add(from);

        while (res.size() != 0) {

            stringBuilder.append(res.pop());
            if (res.size() > 0) {
                stringBuilder.append(" -> ");
            }
        }
        return stringBuilder.toString();
    }


}
