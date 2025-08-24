package codingassessment.findmemberconnectiondistance;

import java.util.*;

public class FindConnectionDistanceImpl implements FindConnectionDistance {
  private MemberConnections memberConnections;

  public FindConnectionDistanceImpl(MemberConnections memberConnections) {
    this.memberConnections = memberConnections;
  }

  @Override
  public int calculateConnectionDistance(Member member1, Member member2) {
    // If members are the same, distance is 0
    if (member1.getMemberId() == member2.getMemberId()) {
      return 0;
    }

    // Initialize data structures for BFS from member1
    Queue<Member> queue1 = new LinkedList<>();
    Set<Integer> visited1 = new HashSet<>();
    Map<Integer, Integer> distance1 = new HashMap<>();

    // Initialize data structures for BFS from member2
    Queue<Member> queue2 = new LinkedList<>();
    Set<Integer> visited2 = new HashSet<>();
    Map<Integer, Integer> distance2 = new HashMap<>();

    // Start BFS from member1
    queue1.add(member1);
    visited1.add(member1.getMemberId());
    distance1.put(member1.getMemberId(), 0);

    // Start BFS from member2
    queue2.add(member2);
    visited2.add(member2.getMemberId());
    distance2.put(member2.getMemberId(), 0);

    // Continue while there are nodes to explore in either direction
    while (!queue1.isEmpty() || !queue2.isEmpty()) {
      // Process one node from queue1 if available
      if (!queue1.isEmpty()) {
        Member current1 = queue1.poll();
        int currentId1 = current1.getMemberId();
        int dist1 = distance1.get(currentId1);

        for (Member neighbor : memberConnections.getConnections(current1)) {
          int neighborId = neighbor.getMemberId();
          if (!visited1.contains(neighborId)) {
            visited1.add(neighborId);
            distance1.put(neighborId, dist1 + 1);
            queue1.add(neighbor);
            // Check if this neighbor is visited by the other search
            if (visited2.contains(neighborId)) {
              return distance1.get(neighborId) + distance2.get(neighborId);
            }
          }
        }
      }

      // Process one node from queue2 if available
      if (!queue2.isEmpty()) {
        Member current2 = queue2.poll();
        int currentId2 = current2.getMemberId();
        int dist2 = distance2.get(currentId2);

        for (Member neighbor : memberConnections.getConnections(current2)) {
          int neighborId = neighbor.getMemberId();
          if (!visited2.contains(neighborId)) {
            visited2.add(neighborId);
            distance2.put(neighborId, dist2 + 1);
            queue2.add(neighbor);
            // Check if this neighbor is visited by the other search
            if (visited1.contains(neighborId)) {
              return distance1.get(neighborId) + distance2.get(neighborId);
            }
          }
        }
      }
    }

    // No path found between members
    return -1;
  }
}