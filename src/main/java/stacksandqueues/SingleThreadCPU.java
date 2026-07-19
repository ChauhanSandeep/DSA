package stacksandqueues;

import java.util.Arrays;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * Problem: Single-Threaded CPU
 *
 * A CPU receives tasks [enqueueTime, processingTime] and can run one task at a
 * time. Whenever it is free, it chooses the available task with the shortest
 * processing time, breaking ties by original index. Return the processing order.
 *
 * Leetcode: https://leetcode.com/problems/single-threaded-cpu/ (Medium)
 * Rating:   zerotrac 1798
 * Pattern:  Sorting | Priority queue | Event simulation
 *
 * Example:
 *   Input:  tasks = [[1,2],[2,4],[3,2],[4,1]]
 *   Output: [0,2,3,1]
 *   Why:    after task 0, the CPU always picks the shortest available task, breaking ties by index.
 *
 * Follow-ups:
 *   1. Tasks are preemptible when a shorter task arrives?
 *      Simulate shortest-remaining-time-first with remaining durations and arrival events.
 *   2. Tasks have priority levels before duration?
 *      Put priority before processing time in the heap comparator.
 *   3. Multiple identical CPUs are available?
 *      Use one heap for available tasks and another for machine availability times.
 *   4. Need average waiting time too?
 *      Record each task's start time when popped and accumulate start - enqueueTime.
 *
 * Related: Task Scheduler (621), Meeting Rooms II (253).
 */
public class SingleThreadCPU {

        /**
     * Intuition: the CPU can choose only among tasks that have arrived. Sorting
     * by enqueue time reveals when tasks become available, and the priority queue
     * holds currently available tasks by shortest processing time and then index.
     * If the queue is empty, the CPU jumps to the next enqueue time.
     *
     * Algorithm:
     *   1. Wrap each task with its original index.
     *   2. Sort wrapped tasks by enqueue time.
     *   3. Add all tasks available at `currentTime` to the heap.
     *   4. If the heap is empty, jump `currentTime` to the next task.
     *   5. Poll the best task, record its index, and advance time.
     *
     * Time:  O(n log n) - sorting and heap operations dominate.
     * Space: O(n) - wrapped tasks, heap, and result array.
     *
     * @param tasks tasks[i] = [enqueueTime, processingTime]
     * @return original task indices in processing order
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

    public static void main(String[] args) {
        SingleThreadCPU solver = new SingleThreadCPU();
        int[][][] inputs = { {}, { {1, 2}, {2, 4}, {3, 2}, {4, 1} }, { {7, 10}, {7, 12}, {7, 5}, {7, 4}, {7, 2} } };
        String[] expected = {"[]", "[0, 2, 3, 1]", "[4, 3, 2, 0, 1]"};
        for (int i = 0; i < inputs.length; i++) {
            int[] got = solver.getOrder(inputs[i]);
            System.out.printf("tasks=%s -> %s  expected=%s%n", Arrays.deepToString(inputs[i]), Arrays.toString(got), expected[i]);
        }
    }
}
