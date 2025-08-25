package hashing;

import java.util.*;


/**
 * Problem: Task Scheduler
 *
 * LeetCode Link: https://leetcode.com/problems/task-scheduler/
 *
 * Given a list of CPU tasks (denoted by capital letters A-Z), and a cooldown period 'n',
 * return the least number of time units the CPU will take to finish all the given tasks
 * such that the same task type has at least `n` time units between two executions.
 *
 * Example:
 * Input: tasks = ['A','A','A','A','A','B','B','C'], n = 2
 * Output: 16
 * Explanation:
 * - Schedule: A B idle A B idle A C idle A
 *
 * Follow-up Questions:
 * - What if tasks are not all capital English letters? (Need a Map instead of an array)
 * - What if multiple CPUs are available? (Extend to multi-threaded version)
 * - What if tasks have different execution times or cooldown periods?
 */
public class TaskScheduler {

  public static void main(String[] args) {
    char[] tasks = {'A', 'A', 'A', 'A', 'A', 'B', 'B', 'C'};
    TaskScheduler scheduler = new TaskScheduler();

    int timeUsingHeap = scheduler.leastIntervalUsingHeap(tasks, 2);
    System.out.println("\nTotal CPU time required (Heap-Based): " + timeUsingHeap);

    int timeUsingMath = scheduler.leastIntervalUsingMath(tasks, 2);
    System.out.println("Total CPU time required (Math-Based): " + timeUsingMath);
  }

  /**
   * Approach 1: Max Heap + Cooldown Queue
   *
   * Strategy:
   * - Use a max heap to always select the task with the highest remaining frequency.
   * - Use a cooldown queue to track tasks that are on cooldown and reintroduce them when ready.
   * - Simulate task execution one time unit at a time.
   *
   * Time Complexity: O(N log N), where N is the number of tasks (heap operations dominate).
   * Space Complexity: O(N) for task frequencies, heap, and cooldown queue.
   *
   * @param tasks Array of task characters (A-Z)
   * @param cooldown Cooldown period `n` between two same tasks
   * @return Minimum time units required to finish all tasks
   */
  public int leastIntervalUsingHeap(char[] tasks, int cooldown) {
    Map<Character, Integer> frequencyMap = new HashMap<>(); // Mapping of task to its frequency

    // Step 1: Count frequency of each task
    for (char task : tasks) {
      frequencyMap.put(task, frequencyMap.getOrDefault(task, 0) + 1);
    }

    // Step 2: Max Heap to process task with highest remaining count
    PriorityQueue<Task> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b.remainingCount, a.remainingCount));

    // Step 3: Cooldown queue to hold tasks with their next valid execution time
    Queue<Task> cooldownQueue = new LinkedList<>();

    // Initialize heap with all tasks
    for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
      maxHeap.offer(new Task(entry.getKey(), 0, entry.getValue()));
    }

    int currentTime = 0;

    // Step 4: Simulate each time unit
    while (!maxHeap.isEmpty() || !cooldownQueue.isEmpty()) {
      currentTime++;

      // Reintroduce tasks from cooldown queue back into heap
      if (!cooldownQueue.isEmpty() && cooldownQueue.peek().nextAvailableTime <= currentTime) {
        maxHeap.offer(cooldownQueue.poll());
      }

      if (!maxHeap.isEmpty()) {
        Task task = maxHeap.poll();
        System.out.print(task.name + "  ");  // Visualize execution

        if (task.remainingCount > 1) {
          // Reduce remaining count and put into cooldown
          cooldownQueue.offer(new Task(task.name, currentTime + cooldown + 1, task.remainingCount - 1));
        }
      } else {
        System.out.print("idle  ");
      }
    }

    return currentTime;
  }

  /**
   * Approach 2: Optimized Math Formula
   *
   * Strategy:
   * - Find the task(s) with the maximum frequency.
   * - Use the formula: (maxFreq - 1) * (n + 1) + numberOfMaxFreqTasks
   * - The formula builds the frame with idle slots first and fills in tasks.
   *
   * Time Complexity: O(N), where N is the number of tasks (simple counting)
   * Space Complexity: O(1), fixed array of size 26
   *
   * @param tasks Array of task characters (A-Z)
   * @param cooldown Cooldown period `n`
   * @return Minimum time units required to finish all tasks
   */
  public int leastIntervalUsingMath(char[] tasks, int cooldown) {
    int[] taskCounts = new int[26];

    // Step 1: Count each task
    for (char task : tasks) {
      taskCounts[task - 'A']++;
    }

    // Step 2: Find the max frequency
    int maxFreq = Arrays.stream(taskCounts).max().orElse(0);

    // Step 3: Count how many tasks have that max frequency
    int maxFreqCount = 0;
    for (int freq : taskCounts) {
      if (freq == maxFreq) {
        maxFreqCount++;
      }
    }

    // Step 4: Apply the scheduling formula
    int frameSize = (maxFreq - 1) * (cooldown + 1) + maxFreqCount;

    // Final result must be at least as long as task array
    return Math.max(frameSize, tasks.length);
  }

  /**
   * Helper class to represent a task with metadata for scheduling.
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