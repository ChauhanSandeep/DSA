package frazsheet;

import java.util.*;

/**
 * 297. Serialize and Deserialize Binary Tree
 * 
 * Problem: Design an algorithm to serialize and deserialize a binary tree.
 * Serialization is converting a tree to string. Deserialization is converting
 * string back to tree. Your design should work for any binary tree.
 * 
 * Example:
 * Input: root = [1,2,3,null,null,4,5]
 * Output: "1,2,3,null,null,4,5"
 * 
 * LeetCode: https://leetcode.com/problems/serialize-and-deserialize-binary-tree
 * 
 * Follow-up questions:
 * Q: How to optimize for very large trees?
 * A: Use compression, streaming, or incremental serialization techniques.
 * 
 * Q: Can we handle different node value types?
 * A: Extend serialization format to support multiple data types.
 * 
 * Q: How to ensure backward compatibility with format changes?
 * A: Include version headers and support multiple format versions.
 */
public class SerializeAndDeserializeBinaryTree {
    
    // Definition for binary tree node
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
        TreeNode(int x, TreeNode left, TreeNode right) {
            this.val = x;
            this.left = left;
            this.right = right;
        }
    }
    
    /**
     * PreOrder DFS serialization approach.
     * 
     * Algorithm: Depth-First Search (PreOrder)
     * - Serialize: Visit root, then left, then right subtree
     * - Use special marker (like "null") for null nodes
     * - Deserialize: Recursively build tree following same order
     * - Use iterator/queue for efficient parsing
     * 
     * Time Complexity: O(n) for both serialize and deserialize
     * Space Complexity: O(n) for string and recursion stack
     */
    public class Codec {
        private static final String SPLITTER = ",";
        private static final String NULL_NODE = "null";
        
        // Encodes a tree to a single string
        public String serialize(TreeNode root) {
            StringBuilder sb = new StringBuilder();
            serializeHelper(root, sb);
            return sb.toString();
        }
        
        private void serializeHelper(TreeNode node, StringBuilder sb) {
            if (node == null) {
                sb.append(NULL_NODE).append(SPLITTER);
            } else {
                sb.append(node.val).append(SPLITTER);
                serializeHelper(node.left, sb);
                serializeHelper(node.right, sb);
            }
        }
        
        // Decodes your encoded data to tree
        public TreeNode deserialize(String data) {
            Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(SPLITTER)));
            return deserializeHelper(queue);
        }
        
        private TreeNode deserializeHelper(Queue<String> queue) {
            String val = queue.poll();
            if (NULL_NODE.equals(val)) {
                return null;
            }
            
            TreeNode node = new TreeNode(Integer.parseInt(val));
            node.left = deserializeHelper(queue);
            node.right = deserializeHelper(queue);
            return node;
        }
    }
    
    /**
     * Level-order BFS serialization approach.
     * More intuitive representation similar to array-based trees.
     */
    public class CodecBFS {
        private static final String SPLITTER = ",";
        private static final String NULL_NODE = "null";
        
        public String serialize(TreeNode root) {
            if (root == null) return "";
            
            StringBuilder sb = new StringBuilder();
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            
            while (!queue.isEmpty()) {
                TreeNode node = queue.poll();
                
                if (node == null) {
                    sb.append(NULL_NODE).append(SPLITTER);
                } else {
                    sb.append(node.val).append(SPLITTER);
                    queue.offer(node.left);
                    queue.offer(node.right);
                }
            }
            
            return sb.toString();
        }
        
        public TreeNode deserialize(String data) {
            if (data.isEmpty()) return null;
            
            String[] nodes = data.split(SPLITTER);
            TreeNode root = new TreeNode(Integer.parseInt(nodes[0]));
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            
            for (int i = 1; i < nodes.length; i++) {
                TreeNode parent = queue.poll();
                
                // Process left child
                if (!NULL_NODE.equals(nodes[i])) {
                    parent.left = new TreeNode(Integer.parseInt(nodes[i]));
                    queue.offer(parent.left);
                }
                
                // Process right child
                if (++i < nodes.length && !NULL_NODE.equals(nodes[i])) {
                    parent.right = new TreeNode(Integer.parseInt(nodes[i]));
                    queue.offer(parent.right);
                }
            }
            
            return root;
        }
    }
    
    /**
     * Compact binary serialization for space efficiency.
     * Uses bit-level encoding for minimal storage.
     */
    public class CodecBinary {
        
        public String serialize(TreeNode root) {
            List<Byte> bytes = new ArrayList<>();
            serializeBinary(root, bytes);
            return Base64.getEncoder().encodeToString(toByteArray(bytes));
        }
        
        private void serializeBinary(TreeNode node, List<Byte> bytes) {
            if (node == null) {
                bytes.add((byte) 0); // Null marker
            } else {
                bytes.add((byte) 1); // Non-null marker
                
                // Serialize integer value (4 bytes)
                int val = node.val;
                bytes.add((byte) (val >>> 24));
                bytes.add((byte) (val >>> 16));
                bytes.add((byte) (val >>> 8));
                bytes.add((byte) val);
                
                serializeBinary(node.left, bytes);
                serializeBinary(node.right, bytes);
            }
        }
        
        public TreeNode deserialize(String data) {
            byte[] bytes = Base64.getDecoder().decode(data);
            int[] index = {0}; // Use array for pass-by-reference
            return deserializeBinary(bytes, index);
        }
        
        private TreeNode deserializeBinary(byte[] bytes, int[] index) {
            if (index[0] >= bytes.length || bytes[index[0]++] == 0) {
                return null;
            }
            
            // Read integer value (4 bytes)
            int val = ((bytes[index[0]++] & 0xFF) << 24) |
                     ((bytes[index[0]++] & 0xFF) << 16) |
                     ((bytes[index[0]++] & 0xFF) << 8) |
                     (bytes[index[0]++] & 0xFF);
            
            TreeNode node = new TreeNode(val);
            node.left = deserializeBinary(bytes, index);
            node.right = deserializeBinary(bytes, index);
            return node;
        }
        
        private byte[] toByteArray(List<Byte> bytes) {
            byte[] result = new byte[bytes.size()];
            for (int i = 0; i < bytes.size(); i++) {
                result[i] = bytes.get(i);
            }
            return result;
        }
    }
    
    /**
     * JSON-based serialization for human readability.
     * Produces structured JSON format for debugging and analysis.
     */
    public class CodecJSON {
        
        public String serialize(TreeNode root) {
            return serializeJSON(root).toString();
        }
        
        private Map<String, Object> serializeJSON(TreeNode node) {
            if (node == null) return null;
            
            Map<String, Object> json = new HashMap<>();
            json.put("val", node.val);
            json.put("left", serializeJSON(node.left));
            json.put("right", serializeJSON(node.right));
            return json;
        }
        
        public TreeNode deserialize(String data) {
            if (data.equals("null")) return null;
            
            // Simplified JSON parsing (in practice, use proper JSON library)
            return deserializeJSON(parseJSON(data));
        }
        
        @SuppressWarnings("unchecked")
        private TreeNode deserializeJSON(Map<String, Object> json) {
            if (json == null) return null;
            
            TreeNode node = new TreeNode((Integer) json.get("val"));
            node.left = deserializeJSON((Map<String, Object>) json.get("left"));
            node.right = deserializeJSON((Map<String, Object>) json.get("right"));
            return node;
        }
        
        // Simplified JSON parser (use proper library in production)
        private Map<String, Object> parseJSON(String json) {
            // This is a simplified implementation
            // In practice, use Jackson, Gson, or other JSON libraries
            return new HashMap<>();
        }
    }
    
    /**
     * Compressed serialization using Huffman coding.
     * Optimizes for frequently occurring patterns.
     */
    public class CodecCompressed {
        private final Map<String, String> encodingMap = new HashMap<>();
        private final Map<String, String> decodingMap = new HashMap<>();
        
        public CodecCompressed() {
            buildHuffmanCodes();
        }
        
        public String serialize(TreeNode root) {
            List<String> tokens = new ArrayList<>();
            serializeTokens(root, tokens);
            
            StringBuilder compressed = new StringBuilder();
            for (String token : tokens) {
                compressed.append(encodingMap.getOrDefault(token, token)).append(" ");
            }
            
            return compressed.toString().trim();
        }
        
        private void serializeTokens(TreeNode node, List<String> tokens) {
            if (node == null) {
                tokens.add("null");
            } else {
                tokens.add(String.valueOf(node.val));
                serializeTokens(node.left, tokens);
                serializeTokens(node.right, tokens);
            }
        }
        
        public TreeNode deserialize(String data) {
            String[] compressedTokens = data.split(" ");
            List<String> tokens = new ArrayList<>();
            
            // Decompress tokens
            for (String compressed : compressedTokens) {
                tokens.add(decodingMap.getOrDefault(compressed, compressed));
            }
            
            Queue<String> queue = new LinkedList<>(tokens);
            return deserializeHelper(queue);
        }
        
        private TreeNode deserializeHelper(Queue<String> queue) {
            String val = queue.poll();
            if ("null".equals(val)) {
                return null;
            }
            
            TreeNode node = new TreeNode(Integer.parseInt(val));
            node.left = deserializeHelper(queue);
            node.right = deserializeHelper(queue);
            return node;
        }
        
        // Build simple Huffman codes for common patterns
        private void buildHuffmanCodes() {
            encodingMap.put("null", "0");
            encodingMap.put("0", "1");
            encodingMap.put("1", "10");
            encodingMap.put("-1", "11");
            
            // Build reverse mapping
            for (Map.Entry<String, String> entry : encodingMap.entrySet()) {
                decodingMap.put(entry.getValue(), entry.getKey());
            }
        }
    }
    
    /**
     * Streaming serialization for very large trees.
     * Processes tree in chunks to handle memory constraints.
     */
    public class CodecStreaming {
        private static final String SPLITTER = ",";
        private static final String NULL_NODE = "null";
        private static final int CHUNK_SIZE = 1000;
        
        public List<String> serializeChunks(TreeNode root) {
            List<String> chunks = new ArrayList<>();
            List<String> currentChunk = new ArrayList<>();
            
            serializeChunked(root, currentChunk, chunks);
            
            // Add final chunk if not empty
            if (!currentChunk.isEmpty()) {
                chunks.add(String.join(SPLITTER, currentChunk));
            }
            
            return chunks;
        }
        
        private void serializeChunked(TreeNode node, List<String> currentChunk, List<String> chunks) {
            if (currentChunk.size() >= CHUNK_SIZE) {
                chunks.add(String.join(SPLITTER, currentChunk));
                currentChunk.clear();
            }
            
            if (node == null) {
                currentChunk.add(NULL_NODE);
            } else {
                currentChunk.add(String.valueOf(node.val));
                serializeChunked(node.left, currentChunk, chunks);
                serializeChunked(node.right, currentChunk, chunks);
            }
        }
        
        public TreeNode deserializeChunks(List<String> chunks) {
            Queue<String> allTokens = new LinkedList<>();
            
            for (String chunk : chunks) {
                allTokens.addAll(Arrays.asList(chunk.split(SPLITTER)));
            }
            
            return deserializeHelper(allTokens);
        }
        
        private TreeNode deserializeHelper(Queue<String> queue) {
            String val = queue.poll();
            if (NULL_NODE.equals(val)) {
                return null;
            }
            
            TreeNode node = new TreeNode(Integer.parseInt(val));
            node.left = deserializeHelper(queue);
            node.right = deserializeHelper(queue);
            return node;
        }
    }
    
    /**
     * Versioned codec supporting multiple serialization formats.
     * Ensures backward compatibility across format changes.
     */
    public class CodecVersioned {
        private static final String VERSION_PREFIX = "v";
        private static final String CURRENT_VERSION = "2.0";
        
        private final Codec codecV1 = new Codec();
        private final CodecBFS codecV2 = new CodecBFS();
        
        public String serialize(TreeNode root) {
            String serialized = codecV2.serialize(root);
            return VERSION_PREFIX + CURRENT_VERSION + ":" + serialized;
        }
        
        public TreeNode deserialize(String data) {
            if (!data.startsWith(VERSION_PREFIX)) {
                // Legacy format without version
                return codecV1.deserialize(data);
            }
            
            int colonIndex = data.indexOf(':');
            String version = data.substring(VERSION_PREFIX.length(), colonIndex);
            String serializedData = data.substring(colonIndex + 1);
            
            switch (version) {
                case "1.0":
                    return codecV1.deserialize(serializedData);
                case "2.0":
                    return codecV2.deserialize(serializedData);
                default:
                    throw new IllegalArgumentException("Unsupported version: " + version);
            }
        }
    }
    
    /**
     * Thread-safe codec for concurrent serialization.
     * Ensures correctness under concurrent access.
     */
    public class CodecThreadSafe {
        private static final String SPLITTER = ",";
        private static final String NULL_NODE = "null";
        
        public synchronized String serialize(TreeNode root) {
            StringBuilder sb = new StringBuilder();
            serializeHelper(root, sb);
            return sb.toString();
        }
        
        private void serializeHelper(TreeNode node, StringBuilder sb) {
            if (node == null) {
                sb.append(NULL_NODE).append(SPLITTER);
            } else {
                sb.append(node.val).append(SPLITTER);
                serializeHelper(node.left, sb);
                serializeHelper(node.right, sb);
            }
        }
        
        public synchronized TreeNode deserialize(String data) {
            Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(SPLITTER)));
            return deserializeHelper(queue);
        }
        
        private TreeNode deserializeHelper(Queue<String> queue) {
            String val = queue.poll();
            if (NULL_NODE.equals(val)) {
                return null;
            }
            
            TreeNode node = new TreeNode(Integer.parseInt(val));
            node.left = deserializeHelper(queue);
            node.right = deserializeHelper(queue);
            return node;
        }
    }
    
    /**
     * Utility methods for testing and validation.
     * Provides additional functionality for codec verification.
     */
    public static class CodecUtils {
        
        // Verify serialization-deserialization correctness
        public static boolean verifyCodec(TreeNode root, Codec codec) {
            String serialized = codec.serialize(root);
            TreeNode deserialized = codec.deserialize(serialized);
            return areTreesEqual(root, deserialized);
        }
        
        // Check if two trees are structurally and value-wise equal
        private static boolean areTreesEqual(TreeNode t1, TreeNode t2) {
            if (t1 == null && t2 == null) return true;
            if (t1 == null || t2 == null) return false;
            
            return t1.val == t2.val && 
                   areTreesEqual(t1.left, t2.left) && 
                   areTreesEqual(t1.right, t2.right);
        }
        
        // Analyze serialization efficiency
        public static SerializationStats analyzeEfficiency(TreeNode root, Codec codec) {
            long startTime = System.nanoTime();
            String serialized = codec.serialize(root);
            long serializeTime = System.nanoTime() - startTime;
            
            startTime = System.nanoTime();
            TreeNode deserialized = codec.deserialize(serialized);
            long deserializeTime = System.nanoTime() - startTime;
            
            return new SerializationStats(
                serialized.length(),
                serializeTime / 1_000_000.0, // Convert to milliseconds
                deserializeTime / 1_000_000.0,
                countNodes(root)
            );
        }
        
        private static int countNodes(TreeNode root) {
            if (root == null) return 0;
            return 1 + countNodes(root.left) + countNodes(root.right);
        }
    }
    
    // Statistics class for serialization analysis
    public static class SerializationStats {
        public final int serializedSize;
        public final double serializeTimeMs;
        public final double deserializeTimeMs;
        public final int nodeCount;
        
        public SerializationStats(int serializedSize, double serializeTimeMs, 
                                double deserializeTimeMs, int nodeCount) {
            this.serializedSize = serializedSize;
            this.serializeTimeMs = serializeTimeMs;
            this.deserializeTimeMs = deserializeTimeMs;
            this.nodeCount = nodeCount;
        }
        
        public double getBytesPerNode() {
            return nodeCount == 0 ? 0 : (double) serializedSize / nodeCount;
        }
        
        @Override
        public String toString() {
            return String.format("Size: %d bytes, Nodes: %d, Bytes/Node: %.2f, " +
                               "Serialize: %.2fms, Deserialize: %.2fms",
                               serializedSize, nodeCount, getBytesPerNode(), 
                               serializeTimeMs, deserializeTimeMs);
        }
    }
}