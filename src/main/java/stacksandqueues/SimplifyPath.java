package stacksandqueues;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Problem: Simplify Path
 *
 * Convert an absolute Unix-style path into its canonical form. Multiple slashes
 * collapse to one, "." means stay in the current directory, ".." moves to the
 * parent when possible, and names like "..." are ordinary directory names.
 *
 * Leetcode: https://leetcode.com/problems/simplify-path/ (Medium)
 * Rating:   acceptance 51.0% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Stack | Path normalization | Unix filesystem
 *
 * Example:
 *   Input:  path = "/a/./b/../../c/"
 *   Output: "/c"
 *   Why:    ".." cancels b, then another ".." cancels a, leaving only c under root.
 *
 * Follow-ups:
 *   1. Support relative paths?
 *      Keep unresolved leading ".." components instead of discarding them at root.
 *   2. Resolve symbolic links too?
 *      Requires filesystem metadata and cycle detection for visited link targets.
 *   3. Normalize Windows paths?
 *      Add drive roots, backslash separators, and case-sensitivity rules.
 *   4. Need to process path components as a stream?
 *      Maintain the directory stack online and emit only after all components are read.
 *
 * Related: Design File System (1166), Design In-Memory File System (588).
 */
public class SimplifyPath {
        /**
     * Intuition: the canonical path is the stack of directories from root to the
     * target. Empty components and '.' do nothing, '..' pops one directory when
     * possible, and every other component is a real directory name.
     *
     * Algorithm:
     *   1. Split the path by '/'.
     *   2. Skip empty components and '.'.
     *   3. On '..', remove the last directory if present.
     *   4. Push normal names and join the stack with single slashes.
     *
     * Time:  O(n) - split and rebuild touch the path linearly.
     * Space: O(n) - the stack stores path components.
     *
     * @param path absolute Unix-style path
     * @return simplified canonical path
     */
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

    public static void main(String[] args) {
        SimplifyPath solver = new SimplifyPath();
        String[] inputs = {"", "/home//foo/", "/a/./b/../../c/", "/../", "/.../a/../b"};
        String[] expected = {"/", "/home/foo", "/c", "/", "/.../b"};
        for (int i = 0; i < inputs.length; i++) {
            String got = solver.simplifyPath(inputs[i]);
            System.out.printf("path=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }
}
