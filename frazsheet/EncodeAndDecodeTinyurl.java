package frazsheet;

import java.util.*;

/**
 * 535. Encode and Decode TinyURL
 * 
 * Problem: Design a URL shortening service like TinyURL that encodes a URL to 
 * a shortened URL and decodes it back to the original URL.
 * 
 * Example:
 * String url = "https://leetcode.com/problems/design-tinyurl";
 * String encoded = codec.encode(url);   // returns something like "http://tinyurl.com/4e9iAk"
 * String decoded = codec.decode(encoded); // returns original URL
 * 
 * LeetCode: https://leetcode.com/problems/encode-and-decode-tinyurl
 * 
 * Follow-up questions:
 * Q: How to ensure generated URLs are collision-free?
 * A: Use cryptographic hash or maintain global counter with base conversion.
 * 
 * Q: How to handle URL expiration?
 * A: Add timestamp tracking and cleanup processes for expired URLs.
 * 
 * Q: How to scale to billions of URLs?
 * A: Use distributed hash tables, database sharding, and caching layers.
 */
public class EncodeAndDecodeTinyurl {
    
    /**
     * Counter-based approach using base62 encoding.
     * Ensures unique short URLs and predictable length growth.
     * 
     * Time Complexity: O(1) for encode/decode
     * Space Complexity: O(n) where n is number of URLs
     */
    public static class Codec {
        private Map<String, String> codeToUrl = new HashMap<>();
        private Map<String, String> urlToCode = new HashMap<>();
        private static final String BASE_URL = "http://tinyurl.com/";
        private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private long counter = 1;
        
        public String encode(String longUrl) {
            if (urlToCode.containsKey(longUrl)) {
                return BASE_URL + urlToCode.get(longUrl);
            }
            
            String shortCode = toBase62(counter++);
            codeToUrl.put(shortCode, longUrl);
            urlToCode.put(longUrl, shortCode);
            
            return BASE_URL + shortCode;
        }
        
        public String decode(String shortUrl) {
            String shortCode = shortUrl.substring(BASE_URL.length());
            return codeToUrl.get(shortCode);
        }
        
        private String toBase62(long num) {
            StringBuilder sb = new StringBuilder();
            while (num > 0) {
                sb.append(CHARS.charAt((int)(num % 62)));
                num /= 62;
            }
            return sb.length() == 0 ? "0" : sb.reverse().toString();
        }
    }
    
    /**
     * Random string generation approach.
     * Uses random 6-character strings to avoid predictable patterns.
     */
    public static class CodecRandom {
        private Map<String, String> codeToUrl = new HashMap<>();
        private Map<String, String> urlToCode = new HashMap<>();
        private static final String BASE_URL = "http://tinyurl.com/";
        private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private Random random = new Random();
        
        public String encode(String longUrl) {
            if (urlToCode.containsKey(longUrl)) {
                return BASE_URL + urlToCode.get(longUrl);
            }
            
            String shortCode;
            do {
                shortCode = generateRandomCode();
            } while (codeToUrl.containsKey(shortCode));
            
            codeToUrl.put(shortCode, longUrl);
            urlToCode.put(longUrl, shortCode);
            
            return BASE_URL + shortCode;
        }
        
        public String decode(String shortUrl) {
            String shortCode = shortUrl.substring(BASE_URL.length());
            return codeToUrl.get(shortCode);
        }
        
        private String generateRandomCode() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
            }
            return sb.toString();
        }
    }
    
    /**
     * Hash-based approach using MD5 with collision handling.
     * More distributed but requires collision resolution.
     */
    public static class CodecHash {
        private Map<String, String> codeToUrl = new HashMap<>();
        private Map<String, String> urlToCode = new HashMap<>();
        private static final String BASE_URL = "http://tinyurl.com/";
        
        public String encode(String longUrl) {
            if (urlToCode.containsKey(longUrl)) {
                return BASE_URL + urlToCode.get(longUrl);
            }
            
            String shortCode = generateHashCode(longUrl);
            int suffix = 0;
            String originalCode = shortCode;
            
            // Handle collisions
            while (codeToUrl.containsKey(shortCode)) {
                shortCode = originalCode + (++suffix);
            }
            
            codeToUrl.put(shortCode, longUrl);
            urlToCode.put(longUrl, shortCode);
            
            return BASE_URL + shortCode;
        }
        
        public String decode(String shortUrl) {
            String shortCode = shortUrl.substring(BASE_URL.length());
            return codeToUrl.get(shortCode);
        }
        
        private String generateHashCode(String url) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
                byte[] digest = md.digest(url.getBytes());
                StringBuilder sb = new StringBuilder();
                
                // Take first 6 characters of hex representation
                for (int i = 0; i < 3 && i < digest.length; i++) {
                    sb.append(String.format("%02x", digest[i] & 0xff));
                }
                
                return sb.toString();
            } catch (Exception e) {
                return String.valueOf(url.hashCode());
            }
        }
    }
    
    /**
     * Database-simulation approach with expiration support.
     * More realistic for production systems.
     */
    public static class CodecWithExpiration {
        private Map<String, UrlEntry> codeToUrl = new HashMap<>();
        private Map<String, String> urlToCode = new HashMap<>();
        private static final String BASE_URL = "http://tinyurl.com/";
        private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final long DEFAULT_EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000L; // 7 days
        private long counter = 1;
        
        private static class UrlEntry {
            String url;
            long expirationTime;
            long creationTime;
            int accessCount;
            
            UrlEntry(String url) {
                this.url = url;
                this.creationTime = System.currentTimeMillis();
                this.expirationTime = creationTime + DEFAULT_EXPIRATION_MS;
                this.accessCount = 0;
            }
            
            boolean isExpired() {
                return System.currentTimeMillis() > expirationTime;
            }
        }
        
        public String encode(String longUrl) {
            cleanupExpiredEntries();
            
            if (urlToCode.containsKey(longUrl)) {
                String existingCode = urlToCode.get(longUrl);
                UrlEntry entry = codeToUrl.get(existingCode);
                if (entry != null && !entry.isExpired()) {
                    return BASE_URL + existingCode;
                } else {
                    // Remove expired entry
                    codeToUrl.remove(existingCode);
                    urlToCode.remove(longUrl);
                }
            }
            
            String shortCode = toBase62(counter++);
            UrlEntry entry = new UrlEntry(longUrl);
            
            codeToUrl.put(shortCode, entry);
            urlToCode.put(longUrl, shortCode);
            
            return BASE_URL + shortCode;
        }
        
        public String decode(String shortUrl) {
            String shortCode = shortUrl.substring(BASE_URL.length());
            UrlEntry entry = codeToUrl.get(shortCode);
            
            if (entry == null || entry.isExpired()) {
                return null; // URL not found or expired
            }
            
            entry.accessCount++;
            return entry.url;
        }
        
        private String toBase62(long num) {
            StringBuilder sb = new StringBuilder();
            while (num > 0) {
                sb.append(CHARS.charAt((int)(num % 62)));
                num /= 62;
            }
            return sb.length() == 0 ? "0" : sb.reverse().toString();
        }
        
        private void cleanupExpiredEntries() {
            Iterator<Map.Entry<String, UrlEntry>> iterator = codeToUrl.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, UrlEntry> entry = iterator.next();
                if (entry.getValue().isExpired()) {
                    urlToCode.remove(entry.getValue().url);
                    iterator.remove();
                }
            }
        }
        
        // Additional analytics methods
        public int getAccessCount(String shortUrl) {
            String shortCode = shortUrl.substring(BASE_URL.length());
            UrlEntry entry = codeToUrl.get(shortCode);
            return entry != null ? entry.accessCount : 0;
        }
        
        public long getCreationTime(String shortUrl) {
            String shortCode = shortUrl.substring(BASE_URL.length());
            UrlEntry entry = codeToUrl.get(shortCode);
            return entry != null ? entry.creationTime : 0;
        }
    }
    
    /**
     * Thread-safe version for concurrent access.
     * Uses concurrent data structures and synchronization.
     */
    public static class CodecThreadSafe {
        private final Map<String, String> codeToUrl = new java.util.concurrent.ConcurrentHashMap<>();
        private final Map<String, String> urlToCode = new java.util.concurrent.ConcurrentHashMap<>();
        private static final String BASE_URL = "http://tinyurl.com/";
        private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private final java.util.concurrent.atomic.AtomicLong counter = 
            new java.util.concurrent.atomic.AtomicLong(1);
        
        public String encode(String longUrl) {
            String existingCode = urlToCode.get(longUrl);
            if (existingCode != null) {
                return BASE_URL + existingCode;
            }
            
            synchronized (this) {
                // Double-check after acquiring lock
                existingCode = urlToCode.get(longUrl);
                if (existingCode != null) {
                    return BASE_URL + existingCode;
                }
                
                String shortCode = toBase62(counter.getAndIncrement());
                codeToUrl.put(shortCode, longUrl);
                urlToCode.put(longUrl, shortCode);
                
                return BASE_URL + shortCode;
            }
        }
        
        public String decode(String shortUrl) {
            String shortCode = shortUrl.substring(BASE_URL.length());
            return codeToUrl.get(shortCode);
        }
        
        private String toBase62(long num) {
            StringBuilder sb = new StringBuilder();
            while (num > 0) {
                sb.append(CHARS.charAt((int)(num % 62)));
                num /= 62;
            }
            return sb.length() == 0 ? "0" : sb.reverse().toString();
        }
    }
}