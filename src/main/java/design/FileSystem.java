package design;

import java.util.*;

/**
 * Problem: https://leetcode.com/problems/design-in-memory-file-system/
 *
 * ### Problem Statement:
 * Design a basic in-memory file system to support operations like creating directories,
 * adding content to files, reading content, and listing directory contents.
 *
 * ### Example:
 * Input:
 * FileSystem fileSystem = new FileSystem();
 * fileSystem.mkdir("/a/b/c");
 * fileSystem.addContentToFile("/a/b/c/d", "hello");
 * List<String> list = fileSystem.ls("/"); // Returns ["a"]
 * String content = fileSystem.readContentFromFile("/a/b/c/d"); // Returns "hello"
 *
 * ### Follow-up Questions:
 * 1. Can you implement `rm` (remove) command?
 *    - Yes, you'd need to handle both file and directory deletion carefully, ensuring safe traversal and clean-up.
 * 2. How to implement `move` command?
 *    - It involves detaching a node from one path and attaching it under another, similar to renaming directories.
 * 3. Can we handle concurrency?
 *    - Use locks on nodes or global read-write locks for thread safety.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class FileSystem {

    private final FileNode root;

    public FileSystem() {
        this.root = new FileNode("/");
    }

    /**
     * Lists the contents of a given path.
     * If it's a file, returns a singleton list of the file name.
     * If it's a directory, returns the list of child names in lexicographic order.
     *
     * @param path Absolute path
     * @return List of file or directory names
     *
     * Time Complexity: O(k log k) where k is the number of children in the last directory
     * Space Complexity: O(k) for storing the result list
     */
    public List<String> ls(String path) {
        FileNode node = traversePath(path);

        if (node.isFile) {
            return Collections.singletonList(node.name);
        }

        List<String> contents = new ArrayList<>(node.children.keySet());
        Collections.sort(contents);
        return contents;
    }

    /**
     * Creates a directory path recursively.
     *
     * @param path Absolute path
     *
     * Time Complexity: O(n), n = number of directories in path
     * Space Complexity: O(1)
     */
    public void mkdir(String path) {
        String[] dirs = path.split("/");
        FileNode current = root;

        for (int i = 1; i < dirs.length; i++) {
            current.children.putIfAbsent(dirs[i], new FileNode(dirs[i]));
            current = current.children.get(dirs[i]);
        }
    }

    /**
     * Appends content to a file at the given path. Creates the file if it doesn't exist.
     *
     * @param filePath Absolute file path
     * @param content  Content to append
     *
     * Time Complexity: O(n), n = depth of file path
     * Space Complexity: O(1)
     */
    public void addContentToFile(String filePath, String content) {
        String[] parts = filePath.split("/");
        FileNode current = root;

        // Traverse to the directory containing the file
        for (int i = 1; i < parts.length - 1; i++) {
            current.children.putIfAbsent(parts[i], new FileNode(parts[i]));
            current = current.children.get(parts[i]);
        }

        String fileName = parts[parts.length - 1];
        current.children.putIfAbsent(fileName, new FileNode(fileName, true));
        FileNode fileNode = current.children.get(fileName);
        fileNode.content += content;
    }

    /**
     * Reads content from a file at the given path.
     *
     * @param filePath Absolute file path
     * @return File content
     *
     * Time Complexity: O(n), n = depth of file path
     * Space Complexity: O(1)
     */
    public String readContentFromFile(String filePath) {
        FileNode fileNode = traversePath(filePath);
        return fileNode.isFile ? fileNode.content : null;
    }

    /**
     * Traverses the given path and returns the last FileNode.
     *
     * @param path Absolute file/directory path
     * @return Final FileNode at the given path
     */
    private FileNode traversePath(String path) {
        String[] parts = path.split("/");
        FileNode current = root;

        for (int i = 1; i < parts.length; i++) {
            current = current.children.get(parts[i]);
        }
        return current;
    }

    public static void main(String[] args) {
        FileSystem fs = new FileSystem();
        fs.mkdir("/a/b/c");
        fs.addContentToFile("/a/b/c/d", "hello");
        System.out.println(fs.ls("/")); // [a]
        System.out.println(fs.readContentFromFile("/a/b/c/d")); // "hello"
    }
}

/**
 * Represents a file or directory in the file system.
 */
class FileNode {
    String name;
    boolean isFile;
    String content;
    Map<String, FileNode> children;

    // Constructor for directory
    public FileNode(String name) {
        this(name, false);
    }

    // Constructor for file/directory
    public FileNode(String name, boolean isFile) {
        this.name = name;
        this.isFile = isFile;
        this.content = isFile ? "" : null;
        this.children = new HashMap<>();
    }
}