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
     * @param size Number of functions (0 to size-1)
     * @param logs Array of log entries in format "id:start/end:timestamp"
     * @return Array of exclusive execution times for each function
     */
    public int[] exclusiveTime(int size, List<String> logs) {
        if (logs == null || logs.isEmpty()) {
            return new int[size];
        }

        int[] result = new int[size];
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
     * Algorithm:
     * 1. Use stack to track active function calls with their start times and accumulated exclusive times
     * 2. For each log entry, parse function ID, operation, and timestamp
     * 3. On function start: pause current function, push new function with start time
     * 4. On function end: finalize current function's time, pop from stack, resume previous function
     *
     * Time Complexity: O(m)
     * Space Complexity: O(n)
     */
    public int[] exclusiveTimeDetailed(int size, List<String> logs) {
        int[] result = new int[size];
        Stack<FunctionCall> callStack = new Stack<>();

        for (String log : logs) {
            LogEntry logEntry = parseLog(log);

            if (logEntry.operation.equals("start")) {
                // Pause the currently running function
                if (!callStack.isEmpty()) {
                    FunctionCall existingFunction = callStack.peek();
                    existingFunction.accumulatedTime += logEntry.timestamp - existingFunction.lastStartTime;
                }

                // Start new function
                callStack.push(new FunctionCall(logEntry.functionId, logEntry.timestamp));

            } else { // "end"
                // Complete the function call
                FunctionCall completedFunction = callStack.pop();
                completedFunction.accumulatedTime += logEntry.timestamp - completedFunction.lastStartTime + 1;
                result[completedFunction.functionId] += completedFunction.accumulatedTime;

                // Resume the previous function if any
                if (!callStack.isEmpty()) {
                    callStack.peek().lastStartTime = logEntry.timestamp + 1;
                }
            }
        }

        return result;
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

    // Represents a function call in the stack with its timing details
    static class FunctionCall {
        int functionId; // ID of the function
        int lastStartTime; // Last time this function started or resumed
        int accumulatedTime; // Total exclusive time accumulated so far

        FunctionCall(int functionId, int startTime) {
            this.functionId = functionId;
            this.lastStartTime = startTime;
            this.accumulatedTime = 0;
        }
    }
}