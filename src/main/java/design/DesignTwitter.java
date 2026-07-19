package design;

import java.util.*;

/**
 * Problem: Design Twitter
 *
 * Design a miniature social feed with posting, following, unfollowing, and reading
 * the 10 most recent visible tweets. A user's feed includes their own tweets and
 * tweets from everyone they currently follow.
 *
 * Leetcode: https://leetcode.com/problems/design-twitter/ (Medium)
 * Rating:   not available (design problem)
 * Pattern:  Design | Hash map | K-way merge with priority queue
 *
 * Example:
 *   Input:  postTweet(1,5), getNewsFeed(1), follow(1,2), postTweet(2,6), getNewsFeed(1)
 *   Output: [[5], [6,5]]
 *   Why:    after following user 2, the newer tweet 6 appears before user 1's tweet 5.
 *
 * Follow-ups:
 *   1. How would you scale feed reads for celebrity accounts?
 *      Fan out normal users on write and generate celebrity feeds on read.
 *   2. How would you support deletes?
 *      Mark tweets deleted and skip them during feed merge or cleanup.
 *   3. How would you rank by relevance instead of recency?
 *      Replace timestamp ordering with a scoring model and maintain top-k candidates.
 *
 * Related: Merge k Sorted Lists (23), Design Log Storage System (635).
 */
public class DesignTwitter {
    
    /**
     * Twitter design using hash maps and priority queue for news feed.
     * 
     * Algorithm:
     * - Store tweets with timestamp for ordering
     * - Maintain follower relationships in adjacency list
     * - Use merge k sorted lists approach for news feed generation
     * 
     * Time Complexity: postTweet/follow/unfollow: O(1), getNewsFeed: O(k log k)
     * Space Complexity: O(U + T) where U is users, T is tweets
     */
    public static class Twitter {
        private int timestamp;
        private Map<Integer, List<Tweet>> tweets;
        private Map<Integer, Set<Integer>> following;
        
        private static class Tweet {
            int id;
            int time;
            
            Tweet(int id, int time) {
                this.id = id;
                this.time = time;
            }
        }
        
        public Twitter() {
            timestamp = 0;
            tweets = new HashMap<>();
            following = new HashMap<>();
        }
        
        /**
         * Records a new tweet for the user with the next timestamp.
         *
         * Time:  O(1) - appends to one user's tweet list.
         * Space: O(1) - stores one tweet object.
         *
         * @param userId author id
         * @param tweetId tweet id
         */
        public void postTweet(int userId, int tweetId) {
            tweets.computeIfAbsent(userId, k -> new ArrayList<>()).add(new Tweet(tweetId, timestamp++));
        }
        
        /**
         * Returns up to 10 most recent tweet ids visible to the user.
         *
         * Time:  O(F log F + 10 log F) - F followed users may seed the merge heap.
         * Space: O(F) - heap stores one latest tweet per visible author.
         *
         * @param userId user requesting the feed
         * @return most recent visible tweet ids
         */
        public List<Integer> getNewsFeed(int userId) {
            List<Integer> feed = new ArrayList<>();
            
            // Priority queue to merge tweets from multiple users
            PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(b[1], a[1]));
            
            // Add user's own tweets
            List<Tweet> userTweets = tweets.get(userId);
            if (userTweets != null && !userTweets.isEmpty()) {
                int lastIndex = userTweets.size() - 1;
                Tweet tweet = userTweets.get(lastIndex);
                pq.offer(new int[]{tweet.id, tweet.time, userId, lastIndex});
            }
            
            // Add tweets from followed users
            Set<Integer> followees = following.get(userId);
            if (followees != null) {
                for (int followeeId : followees) {
                    List<Tweet> followeeTweets = tweets.get(followeeId);
                    if (followeeTweets != null && !followeeTweets.isEmpty()) {
                        int lastIndex = followeeTweets.size() - 1;
                        Tweet tweet = followeeTweets.get(lastIndex);
                        pq.offer(new int[]{tweet.id, tweet.time, followeeId, lastIndex});
                    }
                }
            }
            
            // Get top 10 tweets
            while (!pq.isEmpty() && feed.size() < 10) {
                int[] current = pq.poll();
                int tweetId = current[0];
                int authorId = current[2];
                int tweetIndex = current[3];
                
                feed.add(tweetId);
                
                // Add next tweet from same user if exists
                if (tweetIndex > 0) {
                    List<Tweet> authorTweets = tweets.get(authorId);
                    Tweet nextTweet = authorTweets.get(tweetIndex - 1);
                    pq.offer(new int[]{nextTweet.id, nextTweet.time, authorId, tweetIndex - 1});
                }
            }
            
            return feed;
        }
        
        /**
         * Makes one user follow another user.
         *
         * Time:  O(1) average - updates one hash set.
         * Space: O(1) - stores one follow edge when new.
         *
         * @param followerId user who follows
         * @param followeeId user being followed
         */
        public void follow(int followerId, int followeeId) {
            if (followerId != followeeId) {
                following.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
            }
        }
        
        /**
         * Removes one follow relationship when it exists.
         *
         * Time:  O(1) average - removes from one hash set.
         * Space: O(1) - no extra storage.
         *
         * @param followerId user who unfollows
         * @param followeeId user being unfollowed
         */
        public void unfollow(int followerId, int followeeId) {
            Set<Integer> followees = following.get(followerId);
            if (followees != null) {
                followees.remove(followeeId);
            }
        }
    }
    
    /**
     * Alternative implementation using single global tweet list.
     * Simpler but less efficient for large scale.
     */
    public static class TwitterSimple {
        private List<int[]> globalTweets; // [userId, tweetId, timestamp]
        private Map<Integer, Set<Integer>> following;
        private int timestamp;
        
        public TwitterSimple() {
            globalTweets = new ArrayList<>();
            following = new HashMap<>();
            timestamp = 0;
        }
        
        public void postTweet(int userId, int tweetId) {
            globalTweets.add(new int[]{userId, tweetId, timestamp++});
        }
        
        public List<Integer> getNewsFeed(int userId) {
            List<Integer> feed = new ArrayList<>();
            Set<Integer> validUsers = new HashSet<>();
            validUsers.add(userId);
            
            if (following.containsKey(userId)) {
                validUsers.addAll(following.get(userId));
            }
            
            // Iterate from most recent tweets
            for (int i = globalTweets.size() - 1; i >= 0 && feed.size() < 10; i--) {
                int[] tweet = globalTweets.get(i);
                if (validUsers.contains(tweet[0])) {
                    feed.add(tweet[1]);
                }
            }
            
            return feed;
        }
        
        public void follow(int followerId, int followeeId) {
            if (followerId != followeeId) {
                following.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
            }
        }
        
        public void unfollow(int followerId, int followeeId) {
            if (following.containsKey(followerId)) {
                following.get(followerId).remove(followeeId);
            }
        }
    }
    
    /**
     * Advanced implementation with tweet capacity limits per user.
     * Prevents memory issues from users posting too many tweets.
     */
    public static class TwitterOptimized {
        private static final int MAX_TWEETS_PER_USER = 10;
        private int timestamp;
        private Map<Integer, Deque<Tweet>> tweets;
        private Map<Integer, Set<Integer>> following;
        
        private static class Tweet {
            int id;
            int time;
            
            Tweet(int id, int time) {
                this.id = id;
                this.time = time;
            }
        }
        
        public TwitterOptimized() {
            timestamp = 0;
            tweets = new HashMap<>();
            following = new HashMap<>();
        }
        
        public void postTweet(int userId, int tweetId) {
            tweets.computeIfAbsent(userId, k -> new ArrayDeque<>());
            Deque<Tweet> userTweets = tweets.get(userId);
            
            if (userTweets.size() >= MAX_TWEETS_PER_USER) {
                userTweets.removeFirst(); // Remove oldest tweet
            }
            
            userTweets.addLast(new Tweet(tweetId, timestamp++));
        }
        
        public List<Integer> getNewsFeed(int userId) {
            List<Integer> feed = new ArrayList<>();
            PriorityQueue<Tweet> pq = new PriorityQueue<>((a, b) -> Integer.compare(b.time, a.time));
            
            // Add user's tweets
            if (tweets.containsKey(userId)) {
                pq.addAll(tweets.get(userId));
            }
            
            // Add tweets from followed users
            if (following.containsKey(userId)) {
                for (int followeeId : following.get(userId)) {
                    if (tweets.containsKey(followeeId)) {
                        pq.addAll(tweets.get(followeeId));
                    }
                }
            }
            
            // Get top 10
            while (!pq.isEmpty() && feed.size() < 10) {
                feed.add(pq.poll().id);
            }
            
            return feed;
        }
        
        public void follow(int followerId, int followeeId) {
            if (followerId != followeeId) {
                following.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
            }
        }
        
        public void unfollow(int followerId, int followeeId) {
            if (following.containsKey(followerId)) {
                following.get(followerId).remove(followeeId);
            }
        }
    }

    public static void main(String[] args) {
        Twitter twitter = new Twitter();
        twitter.postTweet(1, 5);
        List<Integer> firstFeed = twitter.getNewsFeed(1);
        System.out.printf("ops=postTweet(1,5),getNewsFeed(1) -> %s  expected=%s%n",
                firstFeed, Arrays.toString(new int[]{5}));

        twitter.follow(1, 2);
        twitter.postTweet(2, 6);
        List<Integer> followedFeed = twitter.getNewsFeed(1);
        twitter.unfollow(1, 2);
        List<Integer> unfollowedFeed = twitter.getNewsFeed(1);
        System.out.printf("ops=follow,postTweet(2,6),feed,unfollow,feed -> %s  expected=%s%n",
                Arrays.asList(followedFeed, unfollowedFeed), "[[6, 5], [5]]");
    }
}