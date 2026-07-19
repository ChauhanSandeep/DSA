package codingassessment.findmemberconnectiondistance;

import java.util.*;

/**
 * Problem: Find Connection Distance Between Members
 *
 * Given a social graph where members are nodes and connections are undirected
 * edges, return the smallest number of connections needed to move from one
 * member to another. If the two members are in different components, return -1.
 *
 * Pattern:  Graph | Bidirectional BFS | Shortest path in an unweighted graph
 *
 * Example:
 *   Input:  A-B-C-D and A-E, query E to D
 *   Output: 4
 *   Why:    the shortest route is E-A-B-C-D, and the counted edges are E-A,
 *           A-B, B-C, and C-D.
 *
 * Follow-ups:
 *   1. Support weighted connection strength?
 *      Replace BFS with Dijkstra's algorithm and store cumulative weights.
 *   2. Answer many repeated queries quickly?
 *      Cache hot pair distances or precompute landmark distances for approximate lookup.
 *   3. Handle directed or blocked relationships?
 *      Traverse only outgoing edges that pass the visibility or permission check.
 *   4. Return the actual chain of members?
 *      Keep parent maps on both sides and stitch the two partial paths at the meeting member.
 *
 * Related: Word Ladder (127), Shortest Path in Binary Matrix (1091), Open the Lock (752).
 */
public class FindConnectionDistanceImpl implements FindConnectionDistance {
    private final MemberConnections memberConnections;

    /** Creates a distance calculator over the provided connection source. */
    public FindConnectionDistanceImpl(MemberConnections memberConnections) {
        this.memberConnections = memberConnections;
    }

    /**
     * Intuition: a one-sided BFS is already correct for an unweighted social graph,
     * but it can fan out badly when the people are far apart. Bidirectional BFS is
     * the same shortest-path idea started from both endpoints: instead of exploring
     * every member within distance d from one side, each frontier usually grows only
     * to about d / 2. Each side stores the shortest distance at which it first
     * reached a member. When a newly discovered member was already seen by the
     * opposite side, the two partial paths meet there.
     *
     * Algorithm:
     *   1. Return 0 immediately when the two member ids are the same.
     *   2. Create forward and backward queues, visited sets, and distance maps.
     *   3. Seed one search from member1 and one from member2.
     *   4. Alternate expanding one queued member from each side.
     *   5. Return the distance sum when a discovered member appears in the other side.
     *
     * Time:  O(b^(d/2)) - two frontiers usually grow to about half the shortest distance.
     * Space: O(b^(d/2)) - queues, visited sets, and distance maps hold both frontiers.
     *
     * @param member1 first endpoint in the social graph
     * @param member2 second endpoint in the social graph
     * @return shortest connection distance in edges, or -1 if no connection exists
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

    public static void main(String[] args) {
        class DemoMember implements Member {
            private final int memberId;
            private final String name;

            DemoMember(int memberId, String name) {
                this.memberId = memberId;
                this.name = name;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public int getMemberId() {
                return memberId;
            }
        }

        class DemoConnections implements MemberConnections {
            private final Map<Integer, List<Member>> idToConnections = new HashMap<>();

            void connect(Member firstMember, Member secondMember) {
                if (!idToConnections.containsKey(firstMember.getMemberId())) {
                    idToConnections.put(firstMember.getMemberId(), new ArrayList<Member>());
                }
                if (!idToConnections.containsKey(secondMember.getMemberId())) {
                    idToConnections.put(secondMember.getMemberId(), new ArrayList<Member>());
                }
                idToConnections.get(firstMember.getMemberId()).add(secondMember);
                idToConnections.get(secondMember.getMemberId()).add(firstMember);
            }

            @Override
            public List<Member> getConnections(Member member) {
                List<Member> connections = idToConnections.get(member.getMemberId());
                return connections == null ? Collections.<Member>emptyList() : connections;
            }
        }

        DemoMember alice = new DemoMember(1, "Alice");
        DemoMember bob = new DemoMember(2, "Bob");
        DemoMember carol = new DemoMember(3, "Carol");
        DemoMember dan = new DemoMember(4, "Dan");
        DemoMember eve = new DemoMember(5, "Eve");
        DemoMember frank = new DemoMember(6, "Frank");
        DemoMember gina = new DemoMember(7, "Gina");

        DemoConnections connections = new DemoConnections();
        connections.connect(alice, bob);
        connections.connect(bob, carol);
        connections.connect(carol, dan);
        connections.connect(dan, eve);
        connections.connect(alice, frank);

        FindConnectionDistanceImpl solver = new FindConnectionDistanceImpl(connections);
        Member[][] inputs = {
            {alice, eve},
            {bob, frank},
            {alice, gina},
            {carol, carol}
        };
        int[] expected = {4, 2, -1, 0};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.calculateConnectionDistance(inputs[i][0], inputs[i][1]);
            System.out.printf("pair=%s-%s -> %d  expected=%d%n",
                inputs[i][0].getName(), inputs[i][1].getName(), output, expected[i]);
        }
    }
}