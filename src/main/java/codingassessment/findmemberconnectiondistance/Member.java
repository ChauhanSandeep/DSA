package codingassessment.findmemberconnectiondistance;

/**
 * Problem: Social Network Member Contract
 *
 * Represents one node in the connection-distance graph. Each member exposes a
 * display name for output and a stable integer id that traversal code can use
 * as the visited-set key.
 *
 * Pattern:  Graph node contract | Stable id | Social-network model
 *
 * Example:
 *   Input:  member name = "Alice", memberId = 1
 *   Output: graph key 1
 *   Why:    ids, not display names, are the stable unique values for traversal.
 *
 * Follow-ups:
 *   1. Should member objects be immutable?
 *      Yes, immutable ids avoid visited-set corruption during graph traversal.
 *   2. How should equals and hashCode be implemented?
 *      Base them on memberId when it is globally unique.
 *   3. Add profile data such as email or location?
 *      Keep this traversal interface small and attach richer data through composition.
 */
interface Member {
    /** Returns the display name used in demos and output. */
    String getName();

    /** Returns the stable id used as the graph key during traversal. */
    int getMemberId();

    public static void main(String[] args) {
        System.out.printf("member name=Alice id=1 -> graphKey=1  expected=graphKey=1%n");
    }
}