package stacksandqueues;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Problem: Simplify Path
 *
 * Given a string path, which is an absolute path (starting with a slash '/') to a file or directory
 * in a Unix-style file system, convert it to the simplified canonical path.
 *
 * In a Unix-style file system, a period '.' refers to the current directory, a double period '..'
 * refers to the directory up a level, and any multiple consecutive slashes (i.e. '//') are treated
 * as a single slash '/'. For this problem, any other format of periods such as '...' are treated as
 * file/directory names.
 *
 * The canonical path should have the following format:
 * - The path starts with a single slash '/'.
 * - Any two directories are separated by a single slash '/'.
 * - The path does not end with a trailing '/'.
 * - The path only contains the directories on the path from the root directory to the target file or
 *   directory (i.e., no period '.' or double period '..')
 *
 * Example:
 * Input: path = "/a/./b/../../c/"
 * Output: "/c"
 *
 * LeetCode: https://leetcode.com/problems/simplify-path
 *
 * Time Complexity: O(n) where n is the length of the path
 * Space Complexity: O(n) for the stack
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class SimplifyPath {
    public String simplifyPath(String path) {
        Deque<String> stack = new ArrayDeque<>();
        String[] components = path.split("/");

        for (String component : components) {
            // Skip empty components (from multiple slashes) and current directory (.)
            if (component.isEmpty() || component.equals(".")) {
                continue;
            }

            // Handle parent directory (..)
            if (component.equals("..")) {
                if (!stack.isEmpty()) {
                    stack.removeLast();
                }
            } else {
                // It's a valid directory name, add to stack
                stack.addLast(component);
            }
        }

        // Build the result from the stack
        StringBuilder result = new StringBuilder();
        for (String dir : stack) {
            result.append("/").append(dir);
        }

        // Handle empty path case
        return result.length() > 0 ? result.toString() : "/";
    }
}
