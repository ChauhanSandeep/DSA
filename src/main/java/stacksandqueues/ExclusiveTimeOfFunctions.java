package stacksandqueues;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/**
 * Problem: Exclusive Time of Functions
 * 
 * On a single-threaded CPU, we execute a program containing n functions. Each function has a unique ID
 * between 0 and n-1. Function calls are stored in a call stack: when a function starts, its ID is pushed
 * onto the stack, and when a function ends, its ID is popped off the stack.
 * The function currently running is always the one at the top of the stack. Each time a function starts
 * or ends, we write a log with the format "{function_id}:{"start" | "end"}:{timestamp}".
 * You are given a list logs, where logs[i] represents the ith log message formatted as a string.
 * For each function, return the total amount of time that the function spends running.
 * 
 * Example:
 * Input: n = 2, logs = ["0:start:0","1:start:2","1:end:5","0:end:6"]
 * Output: [3,4]
 * Explanation: Function 0 runs from 0 to 6 (exclusive time = 3), Function 1 runs from 2 to 5 (exclusive time = 4)
 * 
 * LeetCode: https://leetcode.com/problems/exclusive-time-of-functions
 * 
 * Follow-up Questions:
 * 1. How would you handle recursive function calls?
 *    Answer: Current stack-based approach naturally handles recursion as each call is tracked separately.
 * 
 * 2. What if we need to track memory usage instead of time?
 *    Answer: Similar approach but track memory allocation/deallocation events instead of time intervals.
 * 
 * 3. How would you optimize for real-time profiling with millions of function calls?
 *    Answer: Use sampling-based profiling or compress adjacent calls of the same function.
 *    Related: https://leetcode.com/problems/design-log-storage-system/
 * 
 * @author Sandeep
 */
public class ExclusiveTimeOfFunctions {
    
    /**
     * Calculates exclusive execution time for each function using stack simulation.
     * 
     * Algorithm:
     * 1. Use stack to track active function calls
     * 2. For each log entry, parse function ID, operation, and timestamp
     * 3. On function start: update previous function's time, push to stack
     * 4. On function end: calculate and add exclusive time, pop from stack
     * 5. Handle time intervals carefully to avoid double counting
     * 
     * Time Complexity: O(m) where m is number of log entries
     * Space Complexity: O(n) where n is maximum call stack depth
     * 
     * @param n Number of functions (0 to n-1)
     * @param logs Array of log entries in format "id:start/end:timestamp"
     * @return Array of exclusive execution times for each function
     */
    public int[] exclusiveTime(int n, List<String> logs) {
        if (logs == null || logs.isEmpty()) {
            return new int[n];
        }
        
        int[] result = new int[n];
        Stack<Integer> stack = new Stack<>(); // Stack of function IDs
        int previousTime = 0;
        
        for (String log : logs) {
            LogEntry entry = parseLog(log);
            
            if (entry.operation.equals("start")) {
                // If there's a function running, add its exclusive time
                if (!stack.isEmpty()) {
                    result[stack.peek()] += entry.timestamp - previousTime;
                }
                
                // Push new function to stack and update time
                stack.push(entry.functionId);
                previousTime = entry.timestamp;
                
            } else { // "end"
                // Add exclusive time for the ending function
                result[stack.peek()] += entry.timestamp - previousTime + 1;
                stack.pop();
                
                // Update previous time for next calculation
                previousTime = entry.timestamp + 1;
            }
        }
        
        return result;
    }
    
    /**
     * Alternative implementation with explicit time interval tracking.
     * More detailed bookkeeping but same overall approach.
     * 
     * Time Complexity: O(m)
     * Space Complexity: O(n)
     */
    public int[] exclusiveTimeDetailed(int n, List<String> logs) {
        int[] result = new int[n];
        Stack<FunctionCall> callStack = new Stack<>();
        
        for (String log : logs) {
            LogEntry entry = parseLog(log);
            
            if (entry.operation.equals("start")) {
                // Pause the currently running function
                if (!callStack.isEmpty()) {
                    FunctionCall current = callStack.peek();
                    current.accumulatedTime += entry.timestamp - current.lastStartTime;
                }
                
                // Start new function
                callStack.push(new FunctionCall(entry.functionId, entry.timestamp));
                
            } else { // "end"
                // Complete the function call
                FunctionCall completed = callStack.pop();
                completed.accumulatedTime += entry.timestamp - completed.lastStartTime + 1;
                result[completed.functionId] += completed.accumulatedTime;
                
                // Resume the previous function if any
                if (!callStack.isEmpty()) {
                    callStack.peek().lastStartTime = entry.timestamp + 1;
                }
            }
        }
        
        return result;
    }
    
    /**
     * Simplified approach using just function IDs and timestamps.
     * Cleaner code with same logic.
     */
    public int[] exclusiveTimeSimple(int n, List<String> logs) {
        int[] result = new int[n];
        Stack<Integer> stack = new Stack<>();
        int prevTime = 0;
        
        for (String log : logs) {
            String[] parts = log.split(":");
            int id = Integer.parseInt(parts[0]);
            String op = parts[1];
            int time = Integer.parseInt(parts[2]);
            
            if ("start".equals(op)) {
                if (!stack.isEmpty()) {
                    result[stack.peek()] += time - prevTime;
                }
                stack.push(id);
                prevTime = time;
            } else {
                result[stack.pop()] += time - prevTime + 1;
                prevTime = time + 1;
            }
        }
        
        return result;
    }
    
    /**
     * Advanced version that handles nested calls and provides call statistics.
     * Useful for detailed profiling and debugging.
     */
    public ProfileResult exclusiveTimeWithStats(int n, List<String> logs) {
        int[] exclusiveTimes = new int[n];
        int[] callCounts = new int[n];
        int[] maxDepths = new int[n];
        
        Stack<CallFrame> callStack = new Stack<>();
        
        for (String log : logs) {
            LogEntry entry = parseLog(log);
            
            if (entry.operation.equals("start")) {
                // Update currently running function
                if (!callStack.isEmpty()) {
                    CallFrame current = callStack.peek();
                    current.exclusiveTime += entry.timestamp - current.resumeTime;
                }
                
                // Track call depth
                maxDepths[entry.functionId] = Math.max(maxDepths[entry.functionId], callStack.size() + 1);
                callCounts[entry.functionId]++;
                
                // Push new call frame
                callStack.push(new CallFrame(entry.functionId, entry.timestamp));
                
            } else { // "end"
                CallFrame completed = callStack.pop();
                completed.exclusiveTime += entry.timestamp - completed.resumeTime + 1;
                exclusiveTimes[completed.functionId] += completed.exclusiveTime;
                
                // Resume previous function
                if (!callStack.isEmpty()) {
                    callStack.peek().resumeTime = entry.timestamp + 1;
                }
            }
        }
        
        return new ProfileResult(exclusiveTimes, callCounts, maxDepths);
    }
    
    /**
     * Validation method to check if logs are properly formatted and valid.
     * 
     * @param logs List of log entries
     * @return true if all logs are valid
     */
    public boolean validateLogs(List<String> logs) {
        Stack<Integer> stack = new Stack<>();
        
        for (String log : logs) {
            try {
                LogEntry entry = parseLog(log);
                
                if (entry.operation.equals("start")) {
                    stack.push(entry.functionId);
                } else if (entry.operation.equals("end")) {
                    if (stack.isEmpty() || stack.pop() != entry.functionId) {
                        return false; // Mismatched end
                    }
                } else {
                    return false; // Invalid operation
                }
            } catch (Exception e) {
                return false; // Invalid format
            }
        }
        
        return stack.isEmpty(); // All calls should be closed
    }
    
    // Helper method to parse log entry
    private LogEntry parseLog(String log) {
        String[] parts = log.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid log format: " + log);
        }
        
        return new LogEntry(
            Integer.parseInt(parts[0]),
            parts[1],
            Integer.parseInt(parts[2])
        );
    }
    
    // Helper classes for detailed tracking
    static class LogEntry {
        int functionId;
        String operation;
        int timestamp;
        
        LogEntry(int functionId, String operation, int timestamp) {
            this.functionId = functionId;
            this.operation = operation;
            this.timestamp = timestamp;
        }
    }
    
    static class FunctionCall {
        int functionId;
        int lastStartTime;
        int accumulatedTime;
        
        FunctionCall(int functionId, int startTime) {
            this.functionId = functionId;
            this.lastStartTime = startTime;
            this.accumulatedTime = 0;
        }
    }
    
    static class CallFrame {
        int functionId;
        int resumeTime;
        int exclusiveTime;
        
        CallFrame(int functionId, int startTime) {
            this.functionId = functionId;
            this.resumeTime = startTime;
            this.exclusiveTime = 0;
        }
    }
    
    static class ProfileResult {
        int[] exclusiveTimes;
        int[] callCounts;
        int[] maxDepths;
        
        ProfileResult(int[] exclusiveTimes, int[] callCounts, int[] maxDepths) {
            this.exclusiveTimes = exclusiveTimes;
            this.callCounts = callCounts;
            this.maxDepths = maxDepths;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Profile Results:\\n");
            
            for (int i = 0; i < exclusiveTimes.length; i++) {
                sb.append(String.format("Function %d: time=%d, calls=%d, maxDepth=%d\\n",
                                      i, exclusiveTimes[i], callCounts[i], maxDepths[i]));
            }
            
            return sb.toString();
        }
    }
    
    /**
     * Creates sample logs for testing purposes.
     * 
     * @param pattern Test pattern to generate
     * @return List of sample logs
     */
    public List<String> generateSampleLogs(String pattern) {
        List<String> logs = new ArrayList<>();
        
        switch (pattern) {
            case "simple":
                logs.add("0:start:0");
                logs.add("1:start:2");
                logs.add("1:end:5");
                logs.add("0:end:6");
                break;
                
            case "recursive":
                logs.add("0:start:0");
                logs.add("0:start:2");
                logs.add("0:end:5");
                logs.add("0:end:6");
                break;
                
            case "nested":
                logs.add("0:start:0");
                logs.add("1:start:1");
                logs.add("2:start:2");
                logs.add("2:end:3");
                logs.add("1:end:4");
                logs.add("0:end:5");
                break;
        }
        
        return logs;
    }
}