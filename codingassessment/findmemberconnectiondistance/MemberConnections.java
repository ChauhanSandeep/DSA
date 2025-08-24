package codingassessment.findmemberconnectiondistance;

import java.util.List;


interface MemberConnections {
  // given a member, it will return all his/her connections, if no connections, the list is an empty list
  List<Member> getConnections(Member member);
}