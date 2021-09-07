package Graph;

import java.util.LinkedList;
import java.util.Queue;

public class CourseScheduleTopological {
    public boolean canFinish(int numCourses, int[][] prerequisites) {

        // create inDegreeArr (incoming vertices to the node) for each node
        // create queue with starting courses
        // traverse queue and reduce inDegree of the nodes whose prerequisites are in queue
        // if inDegree of any node becomes 0 then add it to queue
        // in the end inDegree of all the nodes should be 0

        Queue<Integer> queue = new LinkedList<>();
        int[] inDegreeArr = new int[numCourses];
        for(int[] relation: prerequisites) {
            inDegreeArr[relation[0]]++;
        }

        for(int i=0; i<numCourses; i++) {
            if(inDegreeArr[i] == 0) queue.offer(i); // find start courses
        }

        // begin traverse
        while(!queue.isEmpty()) {
            int startCourse = queue.poll();
            for (int[] relation : prerequisites) {
                if (relation[1] == startCourse) {
                    if (--inDegreeArr[relation[0]] == 0) {
                        queue.offer(relation[0]);
                    }
                }
            }
        }

        for(Integer inDegree: inDegreeArr) {
            if(inDegree != 0) return false;
        }
        return true;
    }
}
