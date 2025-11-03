package codingassessment.findmemberconnectiondistance;

import java.util.*;

/**
 * Problem: Find Shortest Path Between Two Members in Social Network
 *
 * Given a social network represented as a graph where members are nodes and connections
 * are edges, find the shortest connection distance (degrees of separation) between two members.
 * Uses bidirectional BFS for optimal performance in large networks.
 *
 * 📝 Example:
 * If member A is connected to B, B to C, and C to D:
 * - Distance(A, B) = 1
 * - Distance(A, C) = 2
 * - Distance(A, D) = 3
 *
 * 🎯 Constraints:
 * - Members are uniquely identified by integer IDs
 * - Network is undirected (connections are bidirectional)
 * - May contain disconnected components
 *
 * 💡 Follow-up Questions with Answers:
 * 1. Q: Why use bidirectional BFS instead of regular BFS?
 *    A: Bidirectional BFS explores from both ends simultaneously, meeting in the middle.
 *       For distance d, regular BFS explores O(b^d) nodes while bidirectional explores
 *       O(2 * b^(d/2)) nodes where b is branching factor. This is significantly faster.
 *
 * 2. Q: How would you handle weighted connections (e.g., friendship strength)?
 *    A: Use Dijkstra's algorithm instead of BFS. Maintain priority queue ordered by
 *       cumulative connection weight. Return minimum weighted path distance.
 *
 * 3. Q: What if we need to find all pairs shortest paths?
 *    A: For dense graphs, use Floyd-Warshall (O(n³)). For sparse graphs, run BFS from
 *       each node (O(n * (n + e))). Can cache results for frequently queried pairs.
 *
 * 4. Q: How would you optimize for millions of members and billions of connections?
 *    A: Use distributed BFS with graph partitioning, landmark-based distance estimation,
 *       or hierarchical clustering. Cache popular query results in Redis/Memcached.
 *
 * 5. Q: What if members can block each other (directed edges with restrictions)?
 *    A: Model as directed graph. Check edge validity before traversing. May need to
 *       maintain separate adjacency lists for each direction with access control checks.
 *
 * Related Problems:
 * - Six Degrees of Kevin Bacon
 * - Word Ladder (LeetCode #127)
 * - Shortest Path in Binary Matrix (LeetCode #1091)
 */
public class FindConnectionDistanceImpl implements FindConnectionDistance {
    private final MemberConnections memberConnections;

    public FindConnectionDistanceImpl(MemberConnections memberConnections) {
        this.memberConnections = memberConnections;
    }

    /**
     * Calculates shortest connection distance using bidirectional BFS.
     *
     * Algorithm:
     * 1. Initialize two BFS searches starting from both members
     * 2. Maintain separate visited sets and distance maps for each search
     * 3. Alternate between expanding from member1 and member2
     * 4. When searches meet (common node visited), return sum of distances
     * 5. If queues exhaust without meeting, return -1 (no connection)
     *
     * Key insight: Bidirectional search reduces time complexity from O(b^d) to O(2*b^(d/2))
     * where b is branching factor and d is distance. This is exponentially faster for
     * large distances.
     *
     * Time Complexity: O(b^(d/2)) where b is average connections per member, d is distance
     * Space Complexity: O(b^(d/2)) for visited sets and queues
     *
     * @param member1 First member
     * @param member2 Second member
     * @return Shortest connection distance, or -1 if no connection exists
     */
    @Override
    public int calculateConnectionDistance(Member member1, Member member2) {
        // Base case: same member has distance 0
        if (member1.getMemberId() == member2.getMemberId()) {
            return 0;
        }

        // Forward BFS from member1
        Queue<Member> forwardQueue = new LinkedList<>();
        Set<Integer> forwardVisited = new HashSet<>();
        Map<Integer, Integer> forwardDistance = new HashMap<>();

        // Backward BFS from member2
        Queue<Member> backwardQueue = new LinkedList<>();
        Set<Integer> backwardVisited = new HashSet<>();
        Map<Integer, Integer> backwardDistance = new HashMap<>();

        // Initialize forward search
        forwardQueue.add(member1);
        forwardVisited.add(member1.getMemberId());
        forwardDistance.put(member1.getMemberId(), 0);

        // Initialize backward search
        backwardQueue.add(member2);
        backwardVisited.add(member2.getMemberId());
        backwardDistance.put(member2.getMemberId(), 0);

        // Bidirectional BFS: alternate between forward and backward exploration
        while (!forwardQueue.isEmpty() || !backwardQueue.isEmpty()) {
            // Expand one level from member1's side
            if (!forwardQueue.isEmpty()) {
                Member currentMember = forwardQueue.poll();
                int currentMemberId = currentMember.getMemberId();
                int currentDistance = forwardDistance.get(currentMemberId);

                for (Member connectedMember : memberConnections.getConnections(currentMember)) {
                    int connectedMemberId = connectedMember.getMemberId();
                    
                    if (!forwardVisited.contains(connectedMemberId)) {
                        forwardVisited.add(connectedMemberId);
                        forwardDistance.put(connectedMemberId, currentDistance + 1);
                        forwardQueue.add(connectedMember);
                        
                        // Check if backward search has visited this node (searches meet)
                        if (backwardVisited.contains(connectedMemberId)) {
                            return forwardDistance.get(connectedMemberId) + backwardDistance.get(connectedMemberId);
                        }
                    }
                }
            }

            // Expand one level from member2's side
            if (!backwardQueue.isEmpty()) {
                Member currentMember = backwardQueue.poll();
                int currentMemberId = currentMember.getMemberId();
                int currentDistance = backwardDistance.get(currentMemberId);

                for (Member connectedMember : memberConnections.getConnections(currentMember)) {
                    int connectedMemberId = connectedMember.getMemberId();
                    
                    if (!backwardVisited.contains(connectedMemberId)) {
                        backwardVisited.add(connectedMemberId);
                        backwardDistance.put(connectedMemberId, currentDistance + 1);
                        backwardQueue.add(connectedMember);
                        
                        // Check if forward search has visited this node (searches meet)
                        if (forwardVisited.contains(connectedMemberId)) {
                            return forwardDistance.get(connectedMemberId) + backwardDistance.get(connectedMemberId);
                        }
                    }
                }
            }
        }

        // No connection path exists between members
        return -1;
    }
}