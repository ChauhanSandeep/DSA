package hashing;

import java.util.*;

/**
 * Problem: Find Duplicate File in System
 *
 * Given directory descriptions containing filenames and file contents, group
 * full file paths whose contents are identical. Only groups with at least two
 * files are returned.
 *
 * Leetcode: https://leetcode.com/problems/find-duplicate-file-in-system/ (Medium)
 * Rating:   not available (not a contest problem)
 * Pattern:  Hashing | String parsing | Group by content
 *
 * Example:
 *   Input:  ["root/a 1.txt(abcd) 2.txt(efgh)", "root/c 3.txt(abcd)"]
 *   Output: [["root/a/1.txt", "root/c/3.txt"]]
 *   Why:    both files store the same content token "(abcd)", so their paths
 *           belong in the same duplicate group.
 *
 * Follow-ups:
 *   1. How would you handle huge files without reading all content into memory?
 *      First group by size, then hash chunks, and finally verify byte-by-byte.
 *   2. How would you avoid false positives from hash collisions?
 *      Treat hashes as candidates and compare the original contents before returning.
 *   3. How would you stream directory entries from a filesystem crawler?
 *      Update the content-to-path map incrementally and emit groups after traversal.
 *   4. How would you handle paths whose filenames contain spaces?
 *      Use a parser for the exact input grammar instead of splitting on whitespace alone.
 *
 * Related: Group Anagrams (49), Find Duplicate Subtrees (652).
 */
public class DuplicateFile {

    public static void main(String[] args) {
        DuplicateFile solver = new DuplicateFile();
        String[][] cases = {
            {
                "root/a 1.txt(abcd) 2.txt(efgh)",
                "root/c 3.txt(abcd)",
                "root/c/d 4.txt(efgh)",
                "root 4.txt(efgh)"
            },
            { "root/a 1.txt(abcd)", "root/b 2.txt(efgh)" }
        };
        String[] expected = {
            "[[root/4.txt, root/a/2.txt, root/c/d/4.txt], [root/a/1.txt, root/c/3.txt]]",
            "[]"
        };

        for (int i = 0; i < cases.length; i++) {
            List<List<String>> got = solver.findDuplicate(cases[i]);
            for (List<String> group : got) {
                Collections.sort(group);
            }
            got.sort(Comparator.comparing(group -> group.get(0)));
            System.out.printf("paths=%s -> %s  expected=%s%n",
                Arrays.toString(cases[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: files with equal content are duplicates regardless of where
     * they live. Use the content token as a hash-map key and collect every full
     * path that points to that same content.
     *
     * Algorithm:
     *   1. Parse each directory entry into its root path and file(content) tokens.
     *   2. Extract each filename and content, then append the full path to that content's group.
     *   3. Return only groups whose size is greater than one.
     *
     * Time:  O(T) - each character in the directory descriptions is parsed a constant number of times.
     * Space: O(T) - the map stores the generated file paths grouped by content.
     *
     * @param paths directory descriptions containing files and content tokens
     * @return groups of full paths that share identical content
     */
    public List<List<String>> findDuplicate(String[] paths) {
        List<List<String>> result = new ArrayList<>();
        if (paths == null || paths.length == 0) return result;

        Map<String, List<String>> contentToFileMap = new HashMap<>();

        for (String path : paths) {
            String[] directoryInfo = path.split("\\s+"); // regex for splitting by whitespace
            String directoryPath = directoryInfo[0];

            for (int i = 1; i < directoryInfo.length; i++) {
                int contentStartIdx = directoryInfo[i].indexOf("(");
                String fileName = directoryInfo[i].substring(0, contentStartIdx);
                String content = directoryInfo[i].substring(contentStartIdx);

                String filePath = directoryPath + "/" + fileName;

                // Store file paths based on content
                contentToFileMap.computeIfAbsent(content, k -> new ArrayList<>()).add(filePath);
            }
        }

        // Collect only those groups where there are duplicate files
        for (List<String> fileList : contentToFileMap.values()) {
            if (fileList.size() > 1) {
                result.add(fileList);
            }
        }

        return result;
    }
}
