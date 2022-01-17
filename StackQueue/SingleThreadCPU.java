package StackQueue;

import java.util.Arrays;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * https://leetcode.com/problems/single-threaded-cpu/
 */
public class SingleThreadCPU {

    public int[] getOrder(int[][] tasks) {
        int len = tasks.length;
        Task[] taskArr = new Task[len];
        for (int i = 0; i < len; i++) {
            taskArr[i] = new Task(i, tasks[i][0], tasks[i][1]);
        }
        Arrays.sort(taskArr, (task1, task2) -> {
            return task1.startTime - task2.startTime;
        });


        PriorityQueue<Task> minHeap = new PriorityQueue<Task>((task1, task2) -> {
            if (task1.processingTime == task2.processingTime) {
                return task1.index - task2.index;
            }
            return task1.processingTime - task2.processingTime;
        });

        int nextTaskIndex = 0;
        int[] resultArr = new int[len];
        int resIndex = 0;
        int currTime = 0;

        while (resIndex < len) {
            // Add all the tasks that came in while previous was getting processed
            while (nextTaskIndex < taskArr.length && taskArr[nextTaskIndex].startTime <= currTime) {
                minHeap.add(taskArr[nextTaskIndex]);
                nextTaskIndex++;
            }

            // No tasks came in while previous was getting processed
            if (minHeap.isEmpty()) {
                currTime = taskArr[nextTaskIndex].startTime;
                continue;
            }

            Task currTask = minHeap.poll();
            resultArr[resIndex++] = currTask.index;
            currTime += currTask.processingTime;
        }

        return resultArr;
    }
}

class Task {
    int index;
    int startTime;
    int processingTime;

    Task(int i, int e, int p) {
        this.index = i;
        this.startTime = e;
        this.processingTime = p;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  Task)) return false;

        Task t2 = (Task) obj;
        return t2.index == index && t2.startTime == startTime && t2.processingTime == processingTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, startTime, processingTime);
    }
}
