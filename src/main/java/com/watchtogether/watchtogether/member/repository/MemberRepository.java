package com.watchtogether.watchtogether.member.repository;

import com.watchtogether.watchtogether.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  boolean existsByMemberId(String memberId);

}
