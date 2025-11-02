package codingassessment.findmemberconnectiondistance;

/**
 * Member Interface for Social Network
 *
 * Represents a member in a social network with unique identification and name.
 * This interface is part of the connection distance calculation system where
 * members are nodes in a graph and connections represent edges.
 *
 * 📝 Usage:
 * This interface is implemented by concrete member classes and used to:
 * - Identify unique members in the network by ID
 * - Display human-readable member information
 * - Build and traverse social network graphs
 *
 * 🎯 Design Considerations:
 * - Member IDs should be unique across the entire network
 * - Names are for display purposes and may not be unique
 * - Immutable implementation recommended for thread safety
 *
 * 💡 Follow-up Questions with Answers:
 * 1. Q: Should Member objects be immutable?
 *    A: Yes, recommended for thread safety and to prevent accidental modification during
 *       graph traversal. Make implementations final with private final fields.
 *
 * 2. Q: How would you handle member privacy settings?
 *    A: Add method like isVisibleTo(Member viewer) or hasPermission(Permission p).
 *       Could extend interface or use composition with PrivacyPolicy object.
 *
 * 3. Q: What if members have additional attributes (email, location, etc.)?
 *    A: Use composition: keep this interface minimal, create MemberProfile class
 *       for additional data. Follows Interface Segregation Principle.
 *
 * 4. Q: How would you implement equals() and hashCode()?
 *    A: Based on memberId only since it's unique identifier. This ensures members
 *       are equal regardless of name changes and works correctly in HashSet/HashMap.
 *
 * 5. Q: Should this interface extend Comparable?
 *    A: Depends on use case. If natural ordering needed (e.g., by ID or name),
 *       extend Comparable<Member>. Otherwise use separate Comparator for flexibility.
 *
 * Related Design Patterns:
 * - Identity Map: Ensure single instance per member ID
 * - Flyweight: Share member objects across network
 * - Value Object: Immutable member representation
 */
interface Member {
    /**
     * Returns the display name of this member.
     *
     * @return member's name as a string (may not be unique)
     */
    String getName();

    /**
     * Returns the unique identifier for this member.
     * This ID is used for equality checks and as key in graph structures.
     *
     * @return unique integer ID for this member
     */
    int getMemberId();
}