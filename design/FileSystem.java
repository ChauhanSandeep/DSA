package design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * https://leetcode.com/problems/design-in-memory-file-system/
 */
class FileSystem {

    Node root;

    public FileSystem() {
        root = new Node("/");
    }

    public List<String> ls(String path) {
        List<String> list;

        if ("/".equals(path)) {
            list = new ArrayList<>(root.children.keySet());
            Collections.sort(list);
            return list;
        }

        Node temp = root;
        String[] nodes = path.split("/");

        for (int i = 0; i < nodes.length; i++) {
            if (temp.children.containsKey(nodes[i])) {
                temp = temp.children.get(nodes[i]);
            }
        }
        if (temp.isFile) {
            return Arrays.asList(temp.name);
        }
        list = new ArrayList<>(temp.children.keySet());
        Collections.sort(list);
        return list;
    }

    public void mkdir(String path) {
        Node temp = root;
        String[] nodes = path.split("/");
        for (int i = 1; i < nodes.length; i++) {
            if (!temp.children.containsKey(nodes[i])) {
                temp.children.put(nodes[i], new Node(nodes[i]));
            }
            temp = temp.children.get(nodes[i]);
        }
    }

    public void addContentToFile(String filePath, String content) {
        Node temp = root;
        String[] nodes = filePath.split("/");
        for (int i = 1; i < nodes.length - 1; i++) {
            if (!temp.children.containsKey(nodes[i])) {
                temp.children.put(nodes[i], new Node(nodes[i]));
            }
            temp = temp.children.get(nodes[i]);
        }
        Node file = temp.children.get(nodes[nodes.length - 1]);
        if (file != null && file.isFile) {
            file.content = file.content + content;
        } else {
            Node curr = new Node(nodes[nodes.length - 1], true);
            curr.content = content;
            temp.children.put(nodes[nodes.length - 1], curr);
        }

    }

    public String readContentFromFile(String filePath) {
        Node temp = root;
        String[] nodes = filePath.split("/");

        for (int i = 1; i < nodes.length - 1; i++) {
            temp = temp.children.get(nodes[i]);
        }
        Node file = temp.children.get(nodes[nodes.length - 1]);
        if (file.isFile) return file.content;
        return null;

    }

    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        fileSystem.ls("/");
        fileSystem.mkdir("/a/b/c");
        fileSystem.addContentToFile("/a/b/c/d", "hello");
        fileSystem.ls("/");
        fileSystem.readContentFromFile("/a/b/c/d");
    }
}

class Node {
    String name;
    boolean isFile;
    String content;
    HashMap<String, Node> children;

    public Node(String name) { // create directory
        this.name = name;
        isFile = false;
        children = new HashMap<>();
        this.content = null;
    }

    public Node(String name, boolean isFile) {
        this.name = name;
        this.isFile = isFile;
        if (isFile) {
            this.children = new HashMap<>();
            content = "";
        } else {
            this.children = new HashMap<>();
            this.content = null;
        }
    }
}
