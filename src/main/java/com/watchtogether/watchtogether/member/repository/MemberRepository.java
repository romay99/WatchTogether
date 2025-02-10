package com.watchtogether.watchtogether.member.repository;

import com.watchtogether.watchtogether.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

  boolean existsByMemberId(String memberId);

  Optional<Member> findByMemberId(String memberId);

  @Modifying
  @Query("UPDATE Member m SET m.point = :newValue WHERE m.memberId = :memberId")
  void updateMemberPoint(String memberId, int newValue);
}
