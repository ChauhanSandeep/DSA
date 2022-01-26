package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * https://leetcode.com/problems/word-search-ii/
 */
public class WordSearch2 {

    public static void main(String[] args) {
        char[][] board = {
                {'o','a','a','n'},
                {'e','t','a','e'},
                {'i','h','k','r'},
                {'i','f','l','v'}
        };
        String[] words = {"oath","pea","eat","rain"};
        System.out.println(new WordSearch2().findWords(board, words));
    }

    char[][] board;
    List<String> result;
    int rows;
    int cols;

    public List<String> findWords(char[][] board, String[] words) {
        // Initialize
        this.result = new ArrayList<>();
        if(board == null || board.length == 0 || board[0].length == 0) return result;
        this.rows = board.length;
        this.cols = board[0].length;
        this.board = board;

        // 1. Create TrieNode
        TrieNode root = new TrieNode();
        for(String word: words) {
            addWord(root, word);
        }


        // 2. Traverse board and backtrack
        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                if(root.children.containsKey(board[i][j])) {
                    boolean[][] visited = new boolean[rows][cols];
                    backTrack(i, j, root, visited);
                }
            }
        }

        return result;
    }

    /**
     * Traverse the trie and check for the words in trie
     */
    private void backTrack(int row, int col, TrieNode parent, boolean[][] visited) {
        if(row < 0 || row>= rows || col<0 || col>=cols || !parent.children.containsKey(board[row][col]) || visited[row][col]) {
            return;
        }

        TrieNode curr = parent.children.get(board[row][col]);
        if(curr.word != null) {
            result.add(curr.word);
            curr.word = null;
        }

        visited[row][col] = true;

        int[] dx = {-1, 0, 0, 1};
        int[] dy = {0, -1, 1, 0};

        for(int i=0; i<4; i++) {
            int nr = row + dx[i];
            int nc = col + dy[i];
            backTrack(nr, nc, curr, visited);
        }

        visited[row][col] = false;

        // Optimization: Prune the already tracked words from Trie
        if(curr.children.isEmpty()) {
            parent.children.remove(board[row][col]);
        }
    }

    /**
     * Adds word to the TrieNode
     */
    private void addWord(TrieNode root, String word) {
        TrieNode temp = root;

        for(Character c: word.toCharArray()) {
            if(!temp.children.containsKey(c)){
                TrieNode newNode = new TrieNode();
                temp.children.put(c, newNode);
            }
            temp = temp.children.get(c);
        }
        temp.word = word;
    }

}

class TrieNode {
    HashMap<Character, TrieNode> children;
    String word;

    public TrieNode() {
        children = new HashMap<>();
        word = null;
    }
}
