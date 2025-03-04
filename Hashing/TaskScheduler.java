package Hashing;

import java.util.*;

/**
 * https://leetcode.com/problems/task-scheduler/
 *
 * Given a set of CPU tasks with cooldown times, determine the minimum time required to complete them.
 */
public class TaskScheduler {
    public static void main(String[] args) {
        char[] tasks = {'A', 'A', 'A', 'A', 'A', 'B', 'B', 'C'};
        int totalTime = new TaskScheduler().leastInterval(tasks, 2);
        System.out.println("\nTotal CPU time required: " + totalTime);
    }

    /**
     * Approach 1: Using a Priority Queue and Cooldown Queue
     * Time Complexity: O(N log N) (due to priority queue operations)
     * Space Complexity: O(N) (for storing tasks in queues)
     */
    public int leastInterval(char[] tasks, int cooldown) {
        Map<Character, Integer> taskFrequencyMap = new HashMap<>();

        // Step 1: Count occurrences of each task
        for (char task : tasks) {
            taskFrequencyMap.put(task, taskFrequencyMap.getOrDefault(task, 0) + 1);
        }

        // Step 2: Use a max heap (priority queue) to execute the most frequent task first
        PriorityQueue<Task> taskQueue = new PriorityQueue<>((a, b) -> Integer.compare(b.remainingCount, a.remainingCount));
        Queue<Task> cooldownQueue = new LinkedList<>();

        // Populate taskQueue with tasks and their frequencies
        for (Map.Entry<Character, Integer> entry : taskFrequencyMap.entrySet()) {
            taskQueue.offer(new Task(entry.getKey(), 0, entry.getValue()));
        }

        int totalTimeUnits = 0;

        // Step 3: Process tasks while there are still pending tasks
        while (!taskQueue.isEmpty() || !cooldownQueue.isEmpty()) {
            totalTimeUnits++;

            // Move tasks from cooldown queue back to task queue when they can be executed
            if (!cooldownQueue.isEmpty() && cooldownQueue.peek().nextAvailableTime <= totalTimeUnits) {
                taskQueue.offer(cooldownQueue.poll());
            }

            // Execute the most frequent available task
            if (!taskQueue.isEmpty()) {
                Task task = taskQueue.poll();
                System.out.print(task.name + "  ");

                if (task.remainingCount > 1) {
                    // Reduce task count and move it to cooldown queue
                    cooldownQueue.offer(new Task(task.name, totalTimeUnits + cooldown + 1, task.remainingCount - 1));
                }
            } else {
                System.out.print("wait  "); // No task available, CPU idle
            }
        }

        return totalTimeUnits;
    }

    /**
     * Approach 2: Optimized Math-Based Solution
     * Time Complexity: O(N) (single pass to count frequencies)
     * Space Complexity: O(1) (since we store only 26 characters)
     */
    public int leastIntervalUsingMath(char[] tasks, int cooldown) {
        int[] frequencies = new int[26];

        // Step 1: Count occurrences of each task
        for (char task : tasks) {
            frequencies[task - 'A']++;
        }

        // Step 2: Find the task with the maximum frequency
        int maxFrequency = Arrays.stream(frequencies).max().orElse(0);

        // Step 3: Count how many tasks have this maximum frequency
        int maxFrequencyCount = 0;
        for (int frequency : frequencies) {
            if (frequency == maxFrequency) {
                maxFrequencyCount++;
            }
        }

        // Step 4: Calculate the minimum intervals required
        int minTime = (maxFrequency - 1) * (cooldown + 1) + maxFrequencyCount;
        return Math.max(tasks.length, minTime);
    }

    // Task class to store task metadata (Java 14+ record for simplicity)
static class Task {
        char name;
        int nextAvailableTime;
        int remainingCount;

        Task(char name, int nextAvailableTime, int remainingCount) {
            this.name = name;
            this.nextAvailableTime = nextAvailableTime;
            this.remainingCount = remainingCount;
        }
    }
}