package arrays.heap;

import java.util.*;

/**
 * Twitter.java (LeetCode 355 - Design Twitter)
 *
 * Problem: Design a simplified Twitter with:
 * - postTweet(userId, tweetId): Create a tweet
 * - getNewsFeed(userId): Get 10 most recent tweets from user + followees
 * - follow(followerId, followeeId): Follow a user
 * - unfollow(followerId, followeeId): Unfollow a user
 * 
 * Leetcode Link: https://leetcode.com/problems/design-twitter/
 *
 * Key Insights of Your Approach:
 * 1. Store tweets as [tweetId, timestamp] pairs per user → O(1) post
 * 2. Use global timestamp counter for ordering → simple & effective
 * 3. K-way merge with PriorityQueue → O(k log m) where k=10, m=followees
 * 4. Track tweet index to iterate backwards through each user's timeline
 *
 * Time Complexity:
 * - postTweet(): O(1)
 * - follow/unfollow(): O(1)
 * - getNewsFeed(): O(m + k log m) where m=followees, k=10 tweets
 *
 * Space Complexity: O(T + F) where T=total tweets, F=total follow relationships
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
class Twitter {
    Map<Integer, Set<Integer>> userFollowerMap; // followerId → set of followeeIds
    Map<Integer, List<int[]>> userTweetMap; // userId → list of [tweetId, timestamp]
    int time = 0; // Global timestamp counter

    public Twitter() {
        userFollowerMap = new HashMap<>();
        userTweetMap = new HashMap<>();
        time = 0;
    }

    public void postTweet(int userId, int tweetId) {
        userTweetMap.computeIfAbsent(userId, k -> new ArrayList<>())
                .add(new int[] { tweetId, time++ });
    }

    /**
     * Steps:
     * 1. Gather all tweet lists from user + followees
     * 2. Use a max-heap (PriorityQueue) to perform k-way merge
     * 3. Extract top 10 most recent tweets based on timestamp
     * 4. Maintain indices to track current position in each user's tweet list
     * 5. Return list of tweetIds
     * 
     * @param userId
     * @return
     */
    public List<Integer> getNewsFeed(int userId) {
        Set<Integer> followees = userFollowerMap.get(userId);

        // Collect all relevant tweet timelines
        List<List<int[]>> allTweetsList = new ArrayList<>();

        // Add user's own tweets
        if (userTweetMap.containsKey(userId)) {
            allTweetsList.add(new ArrayList<>(userTweetMap.get(userId)));
        }

        // Add followees' tweets
        if (followees != null) {
            for (Integer followee : followees) {
                if (userTweetMap.containsKey(followee)) {
                    allTweetsList.add(new ArrayList<>(userTweetMap.get(followee)));
                }
            }
        }

        // K-way merge using PriorityQueue
        // Entry: [listIndex, tweetId, timestamp, tweetIndexInList]
        PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> Integer.compare(b[2], a[2]) // Max-heap by timestamp
        );

        // Initialize queue with latest tweet from each user
        for (int i = 0; i < allTweetsList.size(); i++) {
            List<int[]> userTweets = allTweetsList.get(i);
            if (!userTweets.isEmpty()) {
                int lastIndex = userTweets.size() - 1;
                int[] latestTweet = userTweets.get(lastIndex);
                queue.offer(new int[] { i, latestTweet[0], latestTweet[1], lastIndex });
            }
        }

        // Extract top 10 most recent tweets
        List<Integer> feed = new ArrayList<>();
        for (int i = 0; i < 10 && !queue.isEmpty(); i++) {
            int[] entry = queue.poll();
            feed.add(entry[1]); // Add tweetId to feed

            // Add next tweet from same user's timeline
            int listIndex = entry[0];
            int tweetIndex = entry[3] - 1;
            if (tweetIndex >= 0) {
                List<int[]> userTweets = allTweetsList.get(listIndex);
                int[] nextTweet = userTweets.get(tweetIndex);
                queue.offer(new int[] { listIndex, nextTweet[0], nextTweet[1], tweetIndex });
            }
        }

        return feed;
    }

    public void follow(int followerId, int followeeId) {
        userFollowerMap.computeIfAbsent(followerId, k -> new HashSet<>())
                .add(followeeId);
    }

    public void unfollow(int followerId, int followeeId) {
        if (userFollowerMap.containsKey(followerId)) {
            userFollowerMap.get(followerId).remove(followeeId);
        }
    }
}
