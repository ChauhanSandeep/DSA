package Hashing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * https://leetcode.com/problems/task-scheduler/
 */
public class TaskScheduler {
    public static void main(String[] args) {
        char[] tasks = {'A', 'A', 'A', 'A', 'A', 'B', 'B', 'C'};
        int cpuTime = new TaskScheduler().leastInterval(tasks, 2);
        System.out.println(cpuTime);
    }

    /**
     * Using queue. Good approach to begin
     */
    public int leastInterval(char[] tasks, int waitPeriod) {
        Map<Character, Integer> map = new HashMap<>();

        for (char c : tasks) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        PriorityQueue<Task> runnable = new PriorityQueue<>((a, b) -> b.count - a.count);
        Queue<Task> waiting = new LinkedList<>();

        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            runnable.offer(new Task(entry.getKey(), 0, entry.getValue()));
        }
        int cpuTime = 0;
        while (!waiting.isEmpty() || !runnable.isEmpty()) {
            cpuTime++;
            if (!waiting.isEmpty() && waiting.peek().nextRun <= cpuTime) {
                runnable.offer(waiting.poll());
            }

            if (!runnable.isEmpty()) {
                Task task = runnable.poll();
                System.out.print(task.c + "  ");
                if (task.count > 1) {
                    task.nextRun = cpuTime + waitPeriod + 1;
                    task.count--;
                    waiting.offer(task);
                }
                continue;
            }
            System.out.print("wait  ");
        }
        return cpuTime;
    }

    /**
     * This is more optimum approach using Math
     */
    public int leastIntervalUsingMath(char[] tasks, int waitPeriod) {
        // frequencies of the tasks
        int[] frequencies = new int[26];
        for (int t : tasks) {
            frequencies[t - 'A']++;
        }

        // max frequency
        int maxFreq = 0;
        for (int f : frequencies) {
            maxFreq = Math.max(maxFreq, f);
        }

        // count the most frequent tasks
        int maxFreqChars = 0;
        for (int f : frequencies) {
            if (f == maxFreq) maxFreqChars++;
        }

        return Math.max(tasks.length, (maxFreq - 1) * (waitPeriod + 1) + maxFreqChars);
    }
}

class Task {
    public char c;
    public int nextRun;
    public int count;

    public Task(char c, int nextRun, int count) {
        this.c = c;
        this.nextRun = nextRun;
        this.count = count;
    }
}


