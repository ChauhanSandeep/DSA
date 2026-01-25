package stacksandqueues;

import java.util.Arrays;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * Problem: Single-Threaded CPU
 *
 * You are given a list of tasks where each task is represented by an array [enqueueTime, processingTime].
 * The CPU can process only one task at a time, and tasks are processed in the following order:
 *   1. The task with the shortest processing time is selected.
 *   2. If multiple tasks have the same processing time, choose the task with the smallest index.
 * The CPU is initially idle. If no tasks are available at the current time, it fast-forwards to the next task’s enqueue time.
 *
 * Return the order in which the CPU will process the tasks.
 *
 * Example:
 * Input: tasks = [[1,2],[2,4],[3,2],[4,1]]
 * Output: [0,2,3,1]
 *
 * Explanation:
 * - Task 0 arrives at t=1 and is processed first (2 units).
 * - Task 2 and 3 arrive during this time; task 2 has smaller processing time and is picked next.
 * - Then task 3.
 * - Finally, task 1 is processed.
 *
 * Leetcode Link: https://leetcode.com/problems/single-threaded-cpu/
 *
 * Follow-up Questions:
 * 1. What if tasks can have priority levels?
 *    - Use a custom comparator to include priority in heap logic.
 * 2. What if tasks can be interrupted (preemption)?
 *    - Requires simulation using a priority queue with current job tracking.
 * LeetCode Contest Rating: 1798
 */
public class SingleThreadCPU {

    /**
     * Returns the order in which the CPU will process the tasks.
     *
     * Algorithm:
     * 1. Sort all tasks by enqueue time.
     * 2. Use a min-heap (PriorityQueue) to simulate task picking based on:
     *    - Shortest processing time.
     *    - Then, smallest index in case of tie.
     * 3. Track current CPU time and push all eligible tasks into the heap.
     * 4. Process from heap, updating current time and result list.
     *
     * Time Complexity: O(N log N) — sorting and heap operations
     * Space Complexity: O(N) — heap + result array
     */
    public int[] getOrder(int[][] tasks) {
        int taskCount = tasks.length;
        int[] result = new int[taskCount];

        // Step 1: Wrap input tasks into Task objects with index info
        Task[] allTasks = new Task[taskCount];
        for (int i = 0; i < taskCount; i++) {
            allTasks[i] = new Task(i, tasks[i][0], tasks[i][1]);
        }

        // Step 2: Sort tasks by enqueue time
        Arrays.sort(allTasks, (a, b) -> Integer.compare(a.enqueueTime, b.enqueueTime));

        // Step 3: Min-heap for processing tasks by shortest processing time, then index
        PriorityQueue<Task> availableTasks = new PriorityQueue<>(
            (a, b) -> a.processingTime == b.processingTime
                ? Integer.compare(a.index, b.index)
                : Integer.compare(a.processingTime, b.processingTime)
        );

        int taskIndex = 0;         // Index in sorted task list
        int currentTime = 0;       // Current CPU time
        int resultIndex = 0;       // Index in result array

        while (resultIndex < taskCount) {
            // Step 4: Load all tasks that are available by current time
            while (taskIndex < taskCount && allTasks[taskIndex].enqueueTime <= currentTime) {
                availableTasks.offer(allTasks[taskIndex++]);
            }

            // Step 5: If no tasks are available, jump to the next task’s enqueue time
            if (availableTasks.isEmpty()) {
                currentTime = allTasks[taskIndex].enqueueTime;
                continue;
            }

            // Step 6: Process the task from the heap
            Task nextTask = availableTasks.poll();
            currentTime += nextTask.processingTime;
            result[resultIndex++] = nextTask.index;
        }

        return result;
    }

    /**
     * Represents a single CPU task.
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
            return index == other.index &&
                enqueueTime == other.enqueueTime &&
                processingTime == other.processingTime;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, enqueueTime, processingTime);
        }
    }
}