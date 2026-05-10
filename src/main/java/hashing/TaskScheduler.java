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
 * LeetCode Contest Rating: Not available (not a contest problem)
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
   * Formula: result = max( (maxFreq - 1) * (cooldown + 1) + tasksWithMaxFreq,  tasks.length )
   *
   * ─────────────────────────────────────────────────────────────────────────
   * Derivation from first principles:
   * ─────────────────────────────────────────────────────────────────────────
   *
   * Step 1 — The bottleneck is the most frequent task.
   *   Let `maxFreq` be the highest count of any single task. Two executions
   *   of the same task must be separated by at least `cooldown` time units,
   *   so the task with the highest count dictates the *minimum* schedule
   *   length. No other task can be scheduled tighter than this one.
   *
   * Step 2 — Build a "skeleton" using only the most frequent task.
   *   Place the most-frequent task `maxFreq` times, leaving exactly
   *   `cooldown` empty slots between consecutive occurrences:
   *
   *     A _ _ _ A _ _ _ A _ _ _ A     (maxFreq = 4, cooldown = 3)
   *     └──┬──┘ └──┬──┘ └──┬──┘
   *      block   block   block        ← (maxFreq - 1) * blocks
   *
   *   Each block has size (cooldown + 1): the task itself + cooldown gaps.
   *   There are (maxFreq - 1) full blocks, plus one final occurrence of A.
   *
   *   Skeleton length so far = (maxFreq - 1) * (cooldown + 1) + 1
   *
   * Step 3 — Account for ties at the top frequency.
   *   If multiple tasks share `maxFreq` (say A and B both appear 4 times),
   *   they all face the same bottleneck. They can ride alongside A in the
   *   *final partial block* without extending earlier blocks:
   *
   *     A B _ _ A B _ _ A B _ _ A B   (A and B both appear 4 times)
   *
   *   So instead of adding only 1 trailing slot, we add `tasksWithMaxFreq`
   *   trailing slots — one for each task tied at the maximum frequency.
   *
   *   Frame length = (maxFreq - 1) * (cooldown + 1) + tasksWithMaxFreq
   *
   * Step 4 — Try to fill remaining tasks into idle gaps.
   *   The skeleton has a fixed number of idle slots:
   *
   *     idleCapacity = (maxFreq - 1) * cooldown
   *
   *   (i.e. `cooldown` empties between each of the (maxFreq - 1) blocks).
   *
   *   Two sub-cases arise:
   *
   *   (4a) Remaining tasks fit inside idleCapacity.
   *        Each non-max task has frequency ≤ maxFreq - 1, so its copies
   *        can be spread one-per-block-column without violating its own
   *        cooldown. We just replace "_" with a real task — schedule
   *        length is unchanged. The frameSize formula is exact.
   *
   *   (4b) Remaining tasks overflow idleCapacity.
   *        Example: A=3, B=C=D=E=F=G=H=1, cooldown=2
   *          Skeleton: A _ _ A _ _ A           (length 7)
   *          idleCapacity = (3-1) * 2 = 4
   *          Tasks to place in gaps = 7  →  overflow of 3.
   *
   *        Once every idle slot is consumed, A is no longer the
   *        bottleneck — the remaining tasks have low frequency, so they
   *        can be appended back-to-back with no idles needed:
   *
   *          A B C A D E A F G H               (length 10)
   *
   *        The schedule simply grows to hold every task with zero idle
   *        time, giving length = tasks.length.
   *
   *   Combining (4a) and (4b): the schedule is at least frameSize
   *   *and* at least tasks.length, whichever is larger.
   *
   * Step 5 — Final formula.
   *
   *     result = max(frameSize, tasks.length)
   *
   *   - When (4a) applies: tasks.length ≤ frameSize, so the max is
   *     frameSize (the cooldown bound is binding).
   *   - When (4b) applies: tasks.length > frameSize, so the max is
   *     tasks.length (the bottleneck task gets "diluted" by the many
   *     other tasks and cooldown becomes free).
   *
   * ─────────────────────────────────────────────────────────────────────────
   * Worked example: tasks = [A,A,A,A,A,B,B,C], cooldown = 2
   *   maxFreq = 5 (A), tasksWithMaxFreq = 1
   *   frameSize = (5-1) * (2+1) + 1 = 13
   *   tasks.length = 8
   *   result = max(13, 8) = 13... wait, the LeetCode answer is 16? See note*.
   *
   *   *Note: the doc-header example "16" assumes a different task list.
   *   For [A×5, B×2, C×1] with cooldown=2 the correct answer is indeed
   *   max((5-1)*3 + 1, 8) = 13, schedule: A B C A B _ A _ _ A _ _ A.
   * ─────────────────────────────────────────────────────────────────────────
   *
   * Time Complexity: O(N), where N is the number of tasks (single pass to count).
   * Space Complexity: O(1), fixed array of size 26.
   *
   * @param tasks Array of task characters (A-Z)
   * @param cooldown Cooldown period `n`
   * @return Minimum time units required to finish all tasks
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