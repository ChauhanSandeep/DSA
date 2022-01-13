package design;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * https://leetcode.com/problems/logger-rate-limiter/
 */
class Logger {

    public static void main(String[] args) {
//        ["Logger","shouldPrintMessage","shouldPrintMessage","shouldPrintMessage","shouldPrintMessage","shouldPrintMessage","shouldPrintMessage"]
//[[],[1,"foo"],[2,"bar"],[3,"foo"],[8,"bar"],[10,"foo"],[11,"foo"]]

        Logger logger = new Logger();
        System.out.println(logger.shouldPrintMessage(1, "foo"));
        System.out.println(logger.shouldPrintMessage(2, "bar"));
        System.out.println(logger.shouldPrintMessage(3, "foo"));
        System.out.println(logger.shouldPrintMessage(8, "bar"));
        System.out.println(logger.shouldPrintMessage(10, "foo"));
        System.out.println(logger.shouldPrintMessage(11, "foo"));
    }

    Map<String, Integer> map;
    public Logger() {
        map = new TreeMap<>();
    }
    
    public boolean shouldPrintMessage(int timestamp, String message) {
        if(!map.containsKey(message)) {
            map.put(message, timestamp);
            return true;
        }else{
            int prevTimestamp = map.get(message);
            if(prevTimestamp + 10 > timestamp) {
                return false;
            }else{
                map.put(message, timestamp);
                return true;
            }
        }
        
    }
}