package Hashing;

import java.util.*;

/**
 * Problem: Task Scheduler
 * https://leetcode.com/problems/task-scheduler/
 *
 * Given a list of CPU tasks with cooldown periods, determine the minimum time required to complete all tasks.
 *
 * Approach 1: Max Heap (Priority Queue) + Cooldown Queue
 * - Always execute the most frequent task first.
 * - Use a cooldown queue to track tasks that need waiting time.
 *
 * Approach 2: Math-Based Formula (Optimized)
 * - Calculate the minimum intervals required using the formula:
 *   (maxFrequency - 1) * (cooldown + 1) + maxFrequencyCount
 *
 * Time Complexity:
 * - Priority Queue Approach: O(N log N) due to heap operations.
 * - Math-Based Approach: O(N) using simple counting and arithmetic.
 *
 * Space Complexity:
 * - Priority Queue Approach: O(N) due to additional storage.
 * - Math-Based Approach: O(1) (since we store only 26 task frequencies).
 */
public class TaskScheduler {
    
    public static void main(String[] args) {
        char[] tasks = {'A', 'A', 'A', 'A', 'A', 'B', 'B', 'C'};
        TaskScheduler scheduler = new TaskScheduler();

        int minTimeHeap = scheduler.leastIntervalUsingHeap(tasks, 2);
        System.out.println("\nTotal CPU time required (Heap-Based): " + minTimeHeap);

        int minTimeMath = scheduler.leastIntervalUsingMath(tasks, 2);
        System.out.println("Total CPU time required (Math-Based): " + minTimeMath);
    }

    /**
     * Approach 1: Max Heap + Cooldown Queue
     * Uses a max heap to always execute the most frequent task first.
     * Uses a queue to track cooldown tasks and reintroduces them when ready.
     *
     * @param tasks Array of task identifiers.
     * @param cooldown Minimum cooldown period between identical tasks.
     * @return The minimum time units required to complete all tasks.
     */
    public int leastIntervalUsingHeap(char[] tasks, int cooldown) {
        Map<Character, Integer> taskFrequencyMap = new HashMap<>();

        // Step 1: Count occurrences of each task
        for (char task : tasks) {
            taskFrequencyMap.put(task, taskFrequencyMap.getOrDefault(task, 0) + 1);
        }

        // Step 2: Use a max heap to always pick the task with the highest count first
        PriorityQueue<Task> taskQueue = new PriorityQueue<>((a, b) -> Integer.compare(b.remainingCount, a.remainingCount));
        Queue<Task> cooldownQueue = new LinkedList<>();

        // Populate taskQueue with initial tasks
        for (Map.Entry<Character, Integer> entry : taskFrequencyMap.entrySet()) {
            taskQueue.offer(new Task(entry.getKey(), 0, entry.getValue()));
        }

        int totalTimeUnits = 0;

        // Step 3: Process tasks while there are still pending tasks
        while (!taskQueue.isEmpty() || !cooldownQueue.isEmpty()) {
            totalTimeUnits++;

            // Move tasks from cooldown queue back to task queue when they become available
            if (!cooldownQueue.isEmpty() && cooldownQueue.peek().nextAvailableTime <= totalTimeUnits) {
                taskQueue.offer(cooldownQueue.poll());
            }

            // Execute the highest frequency available task
            if (!taskQueue.isEmpty()) {
                Task task = taskQueue.poll();
                System.out.print(task.name + "  "); // Print executed task

                if (task.remainingCount > 1) {
                    // Reduce task count and move to cooldown queue
                    cooldownQueue.offer(new Task(task.name, totalTimeUnits + cooldown + 1, task.remainingCount - 1));
                }
            } else {
                System.out.print("idle  "); // No available task, CPU idle
            }
        }

        return totalTimeUnits;
    }

    /**
     * Approach 2: Optimized Math-Based Solution
     * Uses a formula to directly compute the minimum execution time.
     *
     * @param tasks Array of task identifiers.
     * @param cooldown Minimum cooldown period between identical tasks.
     * @return The minimum time units required to complete all tasks.
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

        // Step 4: Compute minimum intervals needed using the formula
        int minTime = (maxFrequency - 1) * (cooldown + 1) + maxFrequencyCount;

        // The result must be at least the length of the original task array
        return Math.max(tasks.length, minTime);
    }

    /**
     * Helper class to store task metadata.
     * Uses Java 14+ record feature for brevity and immutability.
     */
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
