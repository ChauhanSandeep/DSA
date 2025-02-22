package Graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Given an array of {course, prerequisite} find if we numOfCourses can be completed
 */
public class CourseSchedule {
    public static void main(String[] args) {
        int[][] arr = {
                {1, 0},
                {0, 1}
        };
        boolean canFinish = new CourseSchedule().canFinishUsingDFS(2, arr);
        System.out.println(canFinish);

    }

    public boolean canFinishUsingDFS(int numCourses, int[][] prerequisites) {
        HashMap<Integer, List<Integer>> courseMap = new HashMap<>();
        // create map
        for (int[] relation : prerequisites) {
            int pre = relation[1];
            int course = relation[0];
            if (courseMap.containsKey(pre)) {
                courseMap.get(pre).add(course);
            } else {
                List<Integer> nextCourses = new LinkedList<>();
                nextCourses.add(course);
                courseMap.put(pre, nextCourses);
            }
        }

        Boolean[] cycle = new Boolean[numCourses];
        // check for cycle
        for (int i = 0; i < numCourses; ++i) {
            if (this.isCyclic(i, courseMap, cycle)) {
                return false;
            }
        }

        return true;
    }

    protected boolean isCyclic(Integer current, HashMap<Integer, List<Integer>> courseMap, Boolean[] cycle) {
        if(cycle[current] != null) return cycle[current];
        if (!courseMap.containsKey(current)) return false;
        cycle[current] = true;

        boolean result;
        for (Integer nextCourse : courseMap.get(current)) {
            result = this.isCyclic(nextCourse, courseMap, cycle);
            if (result) {
                cycle[current] = true;
                return true;
            }
        }
        cycle[current] = false;
        return false;
    }
}
