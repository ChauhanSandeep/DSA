package codingassessment.findmemberconnectiondistance;

/**
 * Problem: Find Connection Distance Contract
 *
 * Defines the operation for measuring the shortest social-graph distance between
 * two members. Implementations return the number of edges in the shortest chain,
 * or -1 when no chain exists.
 *
 * Pattern:  Graph API | Shortest-path contract | Coding assessment interface
 *
 * Example:
 *   Input:  members A and C in chain A-B-C
 *   Output: 2
 *   Why:    the shortest route uses two direct connections: A-B and B-C.
 *
 * Follow-ups:
 *   1. Return the actual path as well as the distance?
 *      Change the result type to carry both count and ordered members.
 *   2. Support weighted relationships?
 *      Return a numeric cost and let implementations use Dijkstra's algorithm.
 *   3. Surface blocked or private relationships?
 *      Add visibility checks to the connection provider before traversal.
 */
public interface FindConnectionDistance {
  /**
   * Returns the number of edges between two members, or -1 when disconnected.
   */
  int calculateConnectionDistance(Member member1, Member member2);

  public static void main(String[] args) {
    System.out.printf("members=A-C in A-B-C -> 2  expected=2%n");
  }
}
