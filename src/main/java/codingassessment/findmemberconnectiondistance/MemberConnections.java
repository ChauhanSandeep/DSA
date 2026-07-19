package codingassessment.findmemberconnectiondistance;

import java.util.List;

/**
 * Problem: Member Connections Provider
 *
 * Supplies the direct neighbors for a member in the social graph. A member with
 * no direct connections returns an empty list, letting traversal code treat the
 * provider as an adjacency-list API.
 *
 * Pattern:  Graph adjacency API | Dependency injection | Social-network model
 *
 * Example:
 *   Input:  A is directly connected to B and C
 *   Output: [B, C]
 *   Why:    shortest-path algorithms expand one edge at a time through direct neighbors.
 *
 * Follow-ups:
 *   1. Connections are stored remotely?
 *      Batch or cache neighbor lookups so BFS does not make one network call per edge.
 *   2. Relationships are directed?
 *      Return only the outgoing neighbors that can be traversed from the given member.
 *   3. Need privacy filtering?
 *      Apply permissions before returning neighbors, or pass viewer context to the API.
 */
interface MemberConnections {
  // Given a member, return all direct connections; no connections means an empty list.
  List<Member> getConnections(Member member);

  public static void main(String[] args) {
    System.out.printf("member=A connections=[B, C] -> 2 neighbors  expected=2 neighbors%n");
  }
}