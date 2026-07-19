package hashing;

import java.util.*;


/**
 * Problem: Task Scheduler
 *
 * Given CPU tasks represented by capital letters and a cooldown n, return the
 * minimum number of time units needed to run all tasks so equal letters are at
 * least n time units apart.
 *
 * Leetcode: https://leetcode.com/problems/task-scheduler/ (Medium)
 * Rating:   not available (not a contest problem)
 * Pattern:  Hashing | Greedy | Max heap | Counting formula
 *
 * Example:
 *   Input:  tasks = [A,A,A,B,B,B], n = 2
 *   Output: 8
 *   Why:    one optimal schedule is A B idle A B idle A B, which uses two idle slots.
 *
 * Follow-ups:
 *   1. How would you support arbitrary task names?
 *      Count frequencies in a map instead of a 26-slot array.
 *   2. How would different cooldowns per task change the simulation?
 *      Store each task's own next available time in the cooldown queue.
 *   3. How would multiple CPUs change the answer?
 *      Simulate up to c available executions per time unit while respecting cooldowns.
 *   4. How would you output one valid schedule?
 *      Use the heap simulation and append either the chosen task or idle at each time step.
 *
 * Related: Reorganize String (767), Rearrange String k Distance Apart (358).
 */
public class TaskScheduler {

  public static void main(String[] args) {
    TaskScheduler scheduler = new TaskScheduler();
    char[][] tasks = { {'A', 'A', 'A', 'B', 'B', 'B'}, {'A', 'B', 'C'} };
    int[] cooldowns = { 2, 0 };
    int[] expected = { 8, 3 };

    for (int i = 0; i < tasks.length; i++) {
      int gotHeap = scheduler.leastIntervalUsingHeap(tasks[i], cooldowns[i]);
      System.out.printf("%ntasks=%s n=%d method=heap -> %d  expected=%d%n",
          Arrays.toString(tasks[i]), cooldowns[i], gotHeap, expected[i]);

      int gotMath = scheduler.leastIntervalUsingMath(tasks[i], cooldowns[i]);
      System.out.printf("tasks=%s n=%d method=math -> %d  expected=%d%n",
          Arrays.toString(tasks[i]), cooldowns[i], gotMath, expected[i]);
    }
  }

    /**
   * Intuition: when simulating time, always run the task type with the most
   * remaining work that is not cooling down. A max heap selects that task, and a
   * queue holds tasks until their next valid time.
   *
   * Algorithm:
   *   1. Count task frequencies and push each task into a max heap.
   *   2. At each time unit, move ready cooldown tasks back to the heap.
   *   3. Execute the highest-frequency available task or idle if none is ready.
   *   4. Requeue unfinished tasks with their next available time.
   *
   * Time:  O(N log U) - each execution can update the heap of U task types.
   * Space: O(U) - the heap, queue, and frequency map store task types.
   *
   * @param tasks array of task identifiers
   * @param cooldown required gap between equal tasks
   * @return minimum time units required by the simulation
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
   * Intuition: the most frequent task creates the tightest spacing constraint.
   * Place its copies first, leaving cooldown-sized gaps, then fill those gaps
   * with other tasks. If there are enough other tasks, the raw task count wins.
   *
   * Algorithm:
   *   1. Count how often each capital-letter task appears.
   *   2. Find the maximum frequency and how many task types share it.
   *   3. Compute the frame forced by the most frequent tasks.
   *   4. Return the larger of that frame and the total number of tasks.
   *
   * Time:  O(N) - one pass counts tasks and the 26-slot frequency array is scanned.
   * Space: O(1) - the frequency array has fixed size.
   *
   * @param tasks array of capital-letter task identifiers
   * @param cooldown required gap between equal tasks
   * @return minimum time units required by the counting formula
   */
  public int leastIntervalUsingMath(char[] tasks, int cooldown) {
    int[] taskFreqArr = new int[26];

    // Step 1: Count each task
    for (char task : tasks) {
      taskFreqArr[task - 'A']++;
    }

    // Step 2: Find the max frequency
    int maxFreq = Arrays.stream(taskFreqArr).max().orElse(0);

    // Step 3: Count how many tasks have that max frequency
    int tasksWithMaxFreq = 0;
    for (int freq : taskFreqArr) {
      if (freq == maxFreq) {
        tasksWithMaxFreq++;
      }
    }

    // Step 4: Apply the scheduling formula
    int frameSize = (maxFreq - 1) * (cooldown + 1) + tasksWithMaxFreq;

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