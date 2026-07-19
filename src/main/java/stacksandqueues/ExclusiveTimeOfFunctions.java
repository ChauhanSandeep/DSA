package stacksandqueues;

import java.util.Stack;
import java.util.List;
import java.util.Arrays;

/**
 * Problem: Exclusive Time of Functions
 *
 * On a single-threaded CPU, function calls are stored in a call stack. A log
 * has the format "function_id:start/end:timestamp", and the function currently
 * running is always the one on top of the stack. Return each function's
 * exclusive running time; end timestamps are inclusive.
 *
 * Leetcode: https://leetcode.com/problems/exclusive-time-of-functions/ (Medium)
 * Rating:   acceptance 66.2% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Stack | Timeline simulation | Inclusive end timestamp
 *
 * Example:
 *   Input:  n = 2, logs = ["0:start:0", "1:start:2", "1:end:5", "0:end:6"]
 *   Output: [3, 4]
 *   Why:    function 0 runs during [0,1] and [6,6], while function 1 runs during [2,5].
 *
 * Follow-ups:
 *   1. Logs arrive as a stream and answers are queried online?
 *      Maintain the same call stack and partial totals; finalize only completed intervals.
 *   2. Need wall-clock time plus exclusive CPU time across threads?
 *      Keep one stack per thread and aggregate by function id.
 *   3. Logs can be malformed or missing end events?
 *      Validate stack transitions and report unmatched starts/ends before computing totals.
 *   4. Need memory profiling instead of time?
 *      Replace timestamp deltas with allocation deltas and keep the same nested ownership model.
 *
 * Related: Design Log Storage System (635).
 */
public class ExclusiveTimeOfFunctions {

        /**
     * Intuition: the stack top owns every timestamp until the next log changes
     * the active function. `previousTime` marks the first uncharged timestamp.
     * Start logs charge the old top before pausing it; end logs charge through
     * the inclusive end timestamp before resuming the parent.
     *
     * Algorithm:
     *   1. Keep result times, an active function stack, and `previousTime`.
     *   2. Parse each log into id, operation, and timestamp.
     *   3. On start, charge the previous stack top and push the new id.
     *   4. On end, charge and pop the top, then set `previousTime` to timestamp + 1.
     *
     * Time:  O(m) - each log is parsed and processed once.
     * Space: O(d) - stack depth plus the output array.
     *
     * @param size number of functions with ids 0 through size - 1
     * @param logs log entries in "id:start/end:timestamp" format
     * @return exclusive time for each function id
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
                    int existingFunctionId = stack.peek();
                    result[existingFunctionId] += entry.timestamp - previousTime;
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

    /** Parses one log line into typed fields. */
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

    public static void main(String[] args) {
        ExclusiveTimeOfFunctions solver = new ExclusiveTimeOfFunctions();
        List<List<String>> logCases = Arrays.asList(Arrays.asList("0:start:0", "1:start:2", "1:end:5", "0:end:6"), Arrays.asList("0:start:0", "0:end:0"), Arrays.asList("0:start:0", "0:start:2", "0:end:5", "0:end:6"));
        int[] sizes = {2, 1, 1};
        String[] expected = {"[3, 4]", "[1]", "[7]"};
        for (int i = 0; i < logCases.size(); i++) {
            int[] got = solver.exclusiveTime(sizes[i], logCases.get(i));
            System.out.printf("n=%d logs=%s -> %s  expected=%s%n", sizes[i], logCases.get(i), Arrays.toString(got), expected[i]);
        }
    }
}
