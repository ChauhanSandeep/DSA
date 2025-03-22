package StackQueue;

import java.util.Arrays;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * Problem: Single-Threaded CPU - Process tasks based on availability and priority.
 *
 * Intuition:
 * - Sort tasks by **enqueue time**.
 * - Use a **min-heap (priority queue)** to process tasks based on:
 *   1. **Shortest processing time** first.
 *   2. If processing times are the same, process the **earlier indexed task**.
 * - If no tasks are available, move the CPU clock forward to the next task.
 *
 * Approach:
 * - Step 1: Sort tasks by `enqueueTime`.
 * - Step 2: Use a **min-heap** to always pick the most eligible task:
 *   - If multiple tasks are available at `currentTime`, pick the shortest job.
 *   - If still tied, pick the task with the smallest index.
 * - Step 3: Process tasks and store execution order.
 *
 * Time Complexity: **O(N log N)** (Sorting takes **O(N log N)**, each push/pop takes **O(log N)**)
 * Space Complexity: **O(N)** (Priority queue and result storage)
 *
 * Problem Link: https://leetcode.com/problems/single-threaded-cpu/
 */
public class SingleThreadCPU {

    public int[] getOrder(int[][] tasks) {
        int n = tasks.length;
        Task[] tasksSortedByStartTime = new Task[n];

        // Step 1: Create Task objects and sort them by enqueue time
        for (int i = 0; i < n; i++) {
            tasksSortedByStartTime[i] = new Task(i, tasks[i][0], tasks[i][1]);
        }
        Arrays.sort(tasksSortedByStartTime, (a, b) -> Integer.compare(a.enqueueTime, b.enqueueTime));

        // Step 2: Min-heap (priority queue) to process tasks based on shortest job first
        PriorityQueue<Task> taskQueue = new PriorityQueue<>(
                (a, b) -> a.processingTime == b.processingTime ? Integer.compare(a.index, b.index)
                        : Integer.compare(a.processingTime, b.processingTime));

        int[] executionOrder = new int[n];
        int taskIndex = 0, resultIndex = 0, currentTime = 0;

        while (resultIndex < n) {
            // Add all tasks that have arrived by `currentTime`
            while (taskIndex < n && tasksSortedByStartTime[taskIndex].enqueueTime <= currentTime) {
                taskQueue.offer(tasksSortedByStartTime[taskIndex++]);
            }

            // If no tasks are available, jump to the next available task time
            if (taskQueue.isEmpty()) {
                currentTime = tasksSortedByStartTime[taskIndex].enqueueTime;
                continue;
            }

            // Step 3: Process the shortest available task
            Task nextTask = taskQueue.poll();
            executionOrder[resultIndex++] = nextTask.index;
            currentTime += nextTask.processingTime;
        }

        return executionOrder;
    }

    /**
     * Represents a task with an index, enqueue time, and processing time.
     */
    static class Task {
        int index;
        int enqueueTime;
        int processingTime;

        Task(int index, int enqueueTime, int processingTime) {
            this.index = index;
            this.enqueueTime = enqueueTime;
            this.processingTime = processingTime;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Task)) return false;
            Task other = (Task) obj;
            return this.index == other.index && this.enqueueTime == other.enqueueTime && this.processingTime == other.processingTime;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, enqueueTime, processingTime);
        }
    }
}
