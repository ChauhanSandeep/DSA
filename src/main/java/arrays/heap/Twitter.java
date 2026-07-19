package arrays.heap;

import java.util.*;

/**
 * Problem: Design Twitter
 *
 * Build a small Twitter-like service. Users can post tweets, follow or unfollow
 * other users, and ask for a news feed containing up to the 10 most recent tweet
 * ids from themselves and the people they follow.
 *
 * Leetcode: https://leetcode.com/problems/design-twitter/
 * Rating:   acceptance 45.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Design | Heap | K-way merge of timelines
 *
 * Example:
 *   Input:  postTweet(1,5), getNewsFeed(1), follow(1,2), postTweet(2,6), getNewsFeed(1)
 *   Output: [5], [6,5]
 *   Why:    user 1 first sees only their own tweet, then sees user 2's newer tweet
 *           before their older tweet after following user 2.
 *
 * Follow-ups:
 *   1. What if each user has millions of tweets?
 *      Store timelines append-only and read only the last needed suffix for feed merging.
 *   2. What if the feed must include likes or ranking features?
 *      Replace pure timestamp ordering with a scoring function and cached candidate sets.
 *   3. What if getNewsFeed must be extremely fast?
 *      Fan out tweets into precomputed feeds on write, trading post cost for read speed.
 */
class Twitter {

    public static void main(String[] args) {
        Twitter twitter = new Twitter();
        twitter.postTweet(1, 5);
        System.out.printf("feed(user=1) -> %s  expected=[5]%n", twitter.getNewsFeed(1));
        twitter.follow(1, 2);
        twitter.postTweet(2, 6);
        System.out.printf("feed(user=1) -> %s  expected=[6, 5]%n", twitter.getNewsFeed(1));
        twitter.unfollow(1, 2);
        System.out.printf("feed(user=1) -> %s  expected=[5]%n", twitter.getNewsFeed(1));
    }

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
     * Intuition: each user's timeline is already sorted by time because tweets are
     * appended in posting order. The news feed is therefore the same as merging the
     * latest ends of several sorted lists: the user's own list plus each followee's
     * list. A max-heap keeps the newest available tweet at the top. Whenever we take
     * one tweet from a timeline, we push the previous tweet from that same timeline,
     * stopping after at most 10 feed items.
     *
     * Time:  O((f + 10) log f) - f timelines are seeded, and at most 10 heap pops are performed.
     * Space: O(f) - the heap holds at most one candidate from each followed timeline plus the user.
     *
     * @param userId id of the user requesting a feed
     * @return up to 10 most recent tweet ids visible to the user
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
