package design;

import java.util.*;

/**
 * 1396. Design Underground System
 * 
 * Problem: Implement an underground system that calculates the average time it takes
 * to travel from one station to another.
 * 
 * Example:
 * UndergroundSystem undergroundSystem = new UndergroundSystem();
 * undergroundSystem.checkIn(45, "Leyton", 3);
 * undergroundSystem.checkOut(45, "Waterloo", 15);
 * undergroundSystem.getAverageTime("Leyton", "Waterloo"); // 12.0
 * 
 * LeetCode: https://leetcode.com/problems/design-underground-system
 * 
 * Follow-up questions:
 * Q: How to handle peak hours vs off-peak analysis?
 * A: Store timestamps and segment averages by time ranges.
 * 
 * Q: What if we need percentile statistics instead of just averages?
 * A: Maintain sorted lists or histogram buckets for each route.
 * 
 * Q: How to optimize for very frequent queries on same routes?
 * A: Cache computed averages, update incrementally on new data.
 * LeetCode Contest Rating: 1465
 **/
public class DesignUndergroundSystem {
    
    /**
     * Underground system tracking check-ins/check-outs and computing averages.
     * 
     * Algorithm:
     * - Store active journeys: customerId -> (station, time)
     * - Store route statistics: (start,end) -> (totalTime, count)
     * - Compute average as totalTime/count for each route
     * 
     * Time Complexity: O(1) for all operations
     * Space Complexity: O(P + S^2) where P is passengers, S is stations
     */
    public static class UndergroundSystem {
        private Map<Integer, CheckIn> activeJourneys;
        private Map<String, RouteStats> routeStatistics;
        
        private static class CheckIn {
            String station;
            int time;
            
            CheckIn(String station, int time) {
                this.station = station;
                this.time = time;
            }
        }
        
        private static class RouteStats {
            double totalTime;
            int count;
            
            RouteStats() {
                this.totalTime = 0.0;
                this.count = 0;
            }
            
            void addJourney(int duration) {
                totalTime += duration;
                count++;
            }
            
            double getAverage() {
                return totalTime / count;
            }
        }
        
        public UndergroundSystem() {
            activeJourneys = new HashMap<>();
            routeStatistics = new HashMap<>();
        }
        
        public void checkIn(int id, String stationName, int t) {
            activeJourneys.put(id, new CheckIn(stationName, t));
        }
        
        public void checkOut(int id, String stationName, int t) {
            CheckIn checkIn = activeJourneys.remove(id);
            if (checkIn != null) {
                String route = checkIn.station + "->" + stationName;
                int duration = t - checkIn.time;
                
                routeStatistics.computeIfAbsent(route, k -> new RouteStats()).addJourney(duration);
            }
        }
        
        public double getAverageTime(String startStation, String endStation) {
            String route = startStation + "->" + endStation;
            return routeStatistics.get(route).getAverage();
        }
    }
    
    /**
     * Extended version that tracks historical data and supports time-based queries.
     * Useful for analyzing travel patterns over different time periods.
     */
    public static class UndergroundSystemExtended {
        private Map<Integer, CheckIn> activeJourneys;
        private Map<String, List<Journey>> routeHistory;
        
        private static class CheckIn {
            String station;
            int time;
            
            CheckIn(String station, int time) {
                this.station = station;
                this.time = time;
            }
        }
        
        private static class Journey {
            int startTime;
            int endTime;
            int duration;
            
            Journey(int startTime, int endTime) {
                this.startTime = startTime;
                this.endTime = endTime;
                this.duration = endTime - startTime;
            }
        }
        
        public UndergroundSystemExtended() {
            activeJourneys = new HashMap<>();
            routeHistory = new HashMap<>();
        }
        
        public void checkIn(int id, String stationName, int t) {
            activeJourneys.put(id, new CheckIn(stationName, t));
        }
        
        public void checkOut(int id, String stationName, int t) {
            CheckIn checkIn = activeJourneys.remove(id);
            if (checkIn != null) {
                String route = checkIn.station + "->" + stationName;
                Journey journey = new Journey(checkIn.time, t);
                
                routeHistory.computeIfAbsent(route, k -> new ArrayList<>()).add(journey);
            }
        }
        
        public double getAverageTime(String startStation, String endStation) {
            String route = startStation + "->" + endStation;
            List<Journey> journeys = routeHistory.get(route);
            
            if (journeys == null || journeys.isEmpty()) {
                return 0.0;
            }
            
            double totalTime = 0;
            for (Journey journey : journeys) {
                totalTime += journey.duration;
            }
            
            return totalTime / journeys.size();
        }
        
        // Additional method: get average time for journeys within time range
        public double getAverageTimeInRange(String startStation, String endStation, int timeStart, int timeEnd) {
            String route = startStation + "->" + endStation;
            List<Journey> journeys = routeHistory.get(route);
            
            if (journeys == null || journeys.isEmpty()) {
                return 0.0;
            }
            
            double totalTime = 0;
            int count = 0;
            
            for (Journey journey : journeys) {
                if (journey.startTime >= timeStart && journey.endTime <= timeEnd) {
                    totalTime += journey.duration;
                    count++;
                }
            }
            
            return count > 0 ? totalTime / count : 0.0;
        }
        
        // Get travel time percentiles
        public double getPercentile(String startStation, String endStation, double percentile) {
            String route = startStation + "->" + endStation;
            List<Journey> journeys = routeHistory.get(route);
            
            if (journeys == null || journeys.isEmpty()) {
                return 0.0;
            }
            
            List<Integer> durations = new ArrayList<>();
            for (Journey journey : journeys) {
                durations.add(journey.duration);
            }
            
            Collections.sort(durations);
            int index = (int) Math.ceil(percentile * durations.size() / 100) - 1;
            index = Math.max(0, Math.min(durations.size() - 1, index));
            
            return durations.get(index);
        }
    }
    
    /**
     * Memory-optimized version using running statistics instead of storing all journeys.
     * Uses Welford's algorithm for stable online computation of variance.
     */
    public static class UndergroundSystemOptimized {
        private Map<Integer, CheckIn> activeJourneys;
        private Map<String, RunningStats> routeStats;
        
        private static class CheckIn {
            String station;
            int time;
            
            CheckIn(String station, int time) {
                this.station = station;
                this.time = time;
            }
        }
        
        private static class RunningStats {
            private int count;
            private double mean;
            private double m2; // For variance calculation
            
            RunningStats() {
                this.count = 0;
                this.mean = 0.0;
                this.m2 = 0.0;
            }
            
            void addValue(double value) {
                count++;
                double delta = value - mean;
                mean += delta / count;
                double delta2 = value - mean;
                m2 += delta * delta2;
            }
            
            double getAverage() {
                return mean;
            }
            
            double getVariance() {
                return count > 1 ? m2 / (count - 1) : 0.0;
            }
            
            double getStandardDeviation() {
                return Math.sqrt(getVariance());
            }
            
            int getCount() {
                return count;
            }
        }
        
        public UndergroundSystemOptimized() {
            activeJourneys = new HashMap<>();
            routeStats = new HashMap<>();
        }
        
        public void checkIn(int id, String stationName, int t) {
            activeJourneys.put(id, new CheckIn(stationName, t));
        }
        
        public void checkOut(int id, String stationName, int t) {
            CheckIn checkIn = activeJourneys.remove(id);
            if (checkIn != null) {
                String route = checkIn.station + "->" + stationName;
                int duration = t - checkIn.time;
                
                routeStats.computeIfAbsent(route, k -> new RunningStats()).addValue(duration);
            }
        }
        
        public double getAverageTime(String startStation, String endStation) {
            String route = startStation + "->" + endStation;
            return routeStats.get(route).getAverage();
        }
        
        // Additional statistical methods
        public double getStandardDeviation(String startStation, String endStation) {
            String route = startStation + "->" + endStation;
            return routeStats.get(route).getStandardDeviation();
        }
        
        public int getJourneyCount(String startStation, String endStation) {
            String route = startStation + "->" + endStation;
            return routeStats.get(route).getCount();
        }
    }
}