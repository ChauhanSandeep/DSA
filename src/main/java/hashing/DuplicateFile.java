package hashing;

import java.util.*;

/**
 * LeetCode Problem: https://leetcode.com/problems/find-duplicate-file-in-system/
 *
 * Given a list of directory paths, file names, and content, this program groups files
 * with duplicate content together.
 *
 * Example Input:
 *   paths = ["root/a 1.txt(abcd) 2.txt(efgh)",
 *            "root/c 3.txt(abcd)",
 *            "root/c/d 4.txt(efgh)",
 *            "root 4.txt(efgh)"]
 *
 * Output:
 *   [["root/a/1.txt", "root/c/3.txt"], ["root/a/2.txt", "root/c/d/4.txt", "root/4.txt"]]
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class DuplicateFile {

    public static void main(String[] args) {
        String[] paths = {
            "root/a 1.txt(abcd) 2.txt(efgh)",   // 1.txt and 2.txt in location root/a
            "root/c 3.txt(abcd)",               // 3.txt in location root/c
            "root/c/d 4.txt(efgh)",             // 4.txt in location root/c/d
            "root 4.txt(efgh)"};                // 4.txt in location root

        List<List<String>> duplicates = new DuplicateFile().findDuplicate(paths);
        System.out.println(duplicates);
    }

    /**
     * Finds duplicate files by grouping them based on identical content.
     * Approach:
     * 1. Parse each directory path and its files.
     * 2. For each file, extract the file name and its content.
     * 3. Use a map to associate file content with a list of file paths.
     * 4. Collect and return lists of file paths that have the same content.
     *
     * Time Complexity: O(N * M), where N is the number of directories and M is the average number of files per directory.
     * Space Complexity: O(N * M) for storing file paths.
     *
     * @param paths An array of directory paths with file names and contents.
     * @return A list of file groups where each group contains files with the same content.
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
